from werkzeug.security import generate_password_hash
from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify

from backend.models import User, UsersBooks, Stats, Books
from . import bp
from sqlalchemy import func, desc, and_
import csv, datetime

_BAD_REQUEST = {'message': 'unvalid data', 'status': 400}
_GOOD_REQUEST = {'message': 'ok', 'status': 200}

session = db.session


class Login(Resource):

    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('email', required=True)
        self.parser.add_argument('password', required=True)

    def post(self):
        args = self.parser.parse_args()
        email = args['email']
        password = args['password']

        user = User.query.filter_by(email=email).first()
        if user is None:
            return {'status': 404,
                    'message': f'User with email {email} does not exist'}
        else:
            if user.check_password(password):
                token = user.generate_auth_token(expiration=10000)
                return _GOOD_REQUEST, {'Bearer': token}
            return _BAD_REQUEST


api.add_resource(Login, '/login')


class Register(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('password', required=True)
        self.parser.add_argument('email', required=True)

    def post(self):
        args = self.parser.parse_args()
        email = args['email']
        password = args['password']

        user = User.query.filter_by(email=email).first()
        if user is not None:
            return {'status': 400,
                    'message': f'User with email {email} already exists'}
        elif email is not None and password is not None:
            username = email[0:email.find('@')]
            user = User(
                email=email,
                username=username,
            )
            user.set_password(password)
            session.add(user)
            session.commit()
            status = Stats(
                user_id=user.id
            )
            session.add(status)
            session.commit()
            return {'message': 'Successfully created', 'status': 201}
        else:
            return _BAD_REQUEST


api.add_resource(Register, '/register')


# class Test(Resource):
#
#     def __init__(self):
#         self.parser = reqparse.RequestParser()
#         self.parser.add_argument('Authorization', location='headers')
#
#     def post(self):
#         args = self.parser.parse_args()
#         token = args['Authorization'].split(' ')[1]
#         print(token)
#
#         user = User.verify_auth_token(token)
#
#         return {'token': token, 'user': user['username']}
#
#
# api.add_resource(Test, '/test')


class UserProfile(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('password')
        self.parser.add_argument('username')
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        else:
            user_stats = Stats.query.filter_by(user_id=user.id).first()
            print(user_stats, 'HERE')
            done = UsersBooks.query.filter_by(user_id=user.id).filter_by(
                list='DN').count()
            progress = UsersBooks.query.filter_by(user_id=user.id).filter_by(
                list='IP').count()
            future = UsersBooks.query.filter_by(user_id=user.id).filter_by(
                list='WR').count()
            user_profile = {
                "username": user.username,
                "email": user.email,
                "week": user_stats.week,
                "year": user_stats.year,
                "month": user_stats.month,
                "DN": done,
                "IP": progress,
                "WR": future
            }
        return user_profile

    def put(self):
        args = self.parser.parse_args()
        username = args['username']
        password = args['password']
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        else:
            if username is not None:
                user.username = username
            if password is not None:
                user.set_password(password)
            session.commit()
            return {'message': 'successfully updated', 'status': 200}


api.add_resource(UserProfile, '/profile')


class Statistics(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('range')
        self.parser.add_argument('month')
        self.parser.add_argument('year')
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}

        token = args['Authorization'].split(' ')[1]
        range = args['range']
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        status = Stats.query.filter_by(user_id=user.id).first()
        range_books = []

        date_to = datetime.date.today()
        date_from = datetime.date.today()
        if range == 'week':
            date_from = datetime.date.today() - datetime.timedelta(days=7)
        elif range == 'month':
            date_from = datetime.date.today() - datetime.timedelta(days=30)
        elif range == 'year':
            date_from = datetime.date.today() - datetime.timedelta(days=365)

        books = UsersBooks.query.filter_by(user_id=user.id).\
            filter(and_(func.date(UsersBooks.data_added) >= date_from),
                   func.date(UsersBooks.data_added) <= date_to).\
            filter_by(list='DN').all()

        count = len(books)
        if count  <= 0:
            fav_author = '-'
            fav_genre = '-'
        else:
            for book in books:
                range_books.append(book.books_id)

        fav_author = Books.query.with_entities(Books.author,
                                               func.count(Books.author)). \
            group_by(Books.author). \
            filter(Books.id.in_(range_books)). \
            order_by(desc(func.count(Books.author))). \
            first()[0]

        fav_genre = Books.query.with_entities(Books.genre,
                                              func.count(Books.genre)). \
            group_by(Books.genre). \
            filter(Books.id.in_(range_books)).order_by(
            desc(func.count(Books.genre))). \
            first()[0]

        print(fav_author, fav_genre, count)\

        divide = 0
        if range == 'week':
            divide = status.week
        elif range == 'month':
            divide = status.month
        elif range == 'year':
            divide = status.year

        if divide > 0:
            percent = f'{round(count * 100 / divide, 2)}%'
        else:
            percent = 'no info provided'
        info = {
            "count": count,
            "fav_author": fav_author,
            "fav_genre": fav_genre,
        }
        plan = {
            "plan": divide,
            "count": count,
            "percent": percent
        }
        return {"info": info, "plan": plan, 'status': 200}

    def put(self):
        args = self.parser.parse_args()
        week = args['week']
        month = args['month']
        year = args['year']
        user = User.query.get(4)
        if user is None:
            return _BAD_REQUEST
        else:
            update_status = Stats.query.filter_by(user_id=user.id).first()
            if week is not None:
                update_status.week = week
            if month is not None:
                update_status.month = month
            if year is not None:
                update_status.year = year
            session.commit()
            return _GOOD_REQUEST


api.add_resource(Statistics, '/stats')


class LogOut(Resource):
    def post(self):
        session.pop('email', None)
        return redirect('/login')


class DoneBooks(Resource):
    def get(self):
        global info_book
        user = User.query.get(4)
        if user is None:
            return _BAD_REQUEST
        else:
            done_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(
                list='DN').all()
            count = len(done_book)
            info = []
            for book in done_book:
                book_id = book.books_id
                current_book = Books.query.get(book_id)
                info_book = {
                    "Title": current_book.title,
                    "Author": current_book.author,
                    "Genre": current_book.genre
                }
                info.append(info_book)
        return {'len': count, 'info': info}


api.add_resource(DoneBooks, '/books/read')


class ProgressBooks(Resource):
    def get(self):
        global info_book
        user = User.query.get(4)
        if user is None:
            return _BAD_REQUEST
        else:
            info = []
            progress_book = UsersBooks.query.filter_by(
                user_id=user.id).filter_by(list='IP').all()
            count = len(progress_book)
            for book in progress_book:
                book_id = book.books_id
                current_book = Books.query.get(book_id)
                info_book = {
                    "Title": current_book.title,
                    "Author": current_book.author,
                    "Genre": current_book.genre
                }
                info.append(info_book)
        return {'len': count, 'info': info}


api.add_resource(ProgressBooks, '/books/progress')


class FutureBooks(Resource):
    def get(self):
        global info_book
        user = User.query.get(4)
        if user is None:
            return _BAD_REQUEST
        else:
            future_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(
                list='WR').all()
            count = len(future_book)
            info = []
            for book in future_book:
                book_id = book.books_id
                current_book = Books.query.get(book_id)
                info_book = {
                    "Title": current_book.title,
                    "Author": current_book.author,
                    "Genre": current_book.genre
                }
                info.append(info_book)
        return {'len': count, 'info': info}


api.add_resource(FutureBooks, '/books/future')
