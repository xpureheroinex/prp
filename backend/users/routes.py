import datetime
from werkzeug.security import generate_password_hash
from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify

from backend.models import User, UsersBooks, Stats, Books, Reviews
from . import bp

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
            return {'status': 404, 'message': f'User with email {email} does not exist'}
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
            done = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='DN').count()
            progress = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='IP').count()
            future = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='WR').count()
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
        self.parser.add_argument('week')
        self.parser.add_argument('month')
        self.parser.add_argument('year')
        self.parser.add_argument('Authorization', location='headers')

    def put(self):
        args = self.parser.parse_args()
        week = args['week']
        month = args['month']
        year = args['year']
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
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


api.add_resource(Statistics, '/stats')


class LogOut(Resource):
    def post(self):
        session.pop('email', None)
        return redirect('/login')


api.add_resource(LogOut, '/logout')


class DoneBooks(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        global info_book
        if user is None:
            return _BAD_REQUEST
        else:
            done_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='DN').all()
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
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        global info_book
        if user is None:
            return _BAD_REQUEST
        else:
            info = []
            progress_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='IP').all()
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
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        global info_book
        if user is None:
            return _BAD_REQUEST
        else:
            future_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='WR').all()
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


class AddReviews(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('text')
        self.parser.add_argument('Authorization', location='headers')

    def get(self, books_id):
        list_reviews = Reviews.query.filter_by(books_id=books_id).all()
        count = len(list_reviews)
        info = []
        if count != 0:
            for review in list_reviews:
                user_id = review.user_id
                user = User.query.get(user_id)
                info_review = {
                    "username": user.username,
                    "text": review.text,
                    'created': review.data_added.strftime(format='%d/%m/%Y'),
                }
                info.append(info_review)
            return {'len': count, 'info': info}
        else:
            return {'message': 'No reviews about this book', 'status': 201}

    def post(self, books_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        text = args['text']
        exist_user = Reviews.query.filter_by(user_id=user_id).filter_by(books_id=books_id).first()
        if exist_user is not None:
            return {'status': 400,
                    'message': f'User {user.username} already left review on this book'}
        elif text is not None:
            review = Reviews(
                user_id=user.id,
                books_id=books_id,
                text=text
            )
            session.add(review)
            session.commit()
            return {'message': 'Successfully created', 'status': 201}
        else:
            return _BAD_REQUEST


api.add_resource(AddReviews, '/books/<int:books_id>/reviews')
