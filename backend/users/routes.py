from werkzeug.security import generate_password_hash
from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify
from operator import xor

from backend.models import User, UsersBooks, Stats, Books
from . import bp

_BAD_REQUEST = {'status': 'error'}, 400, 500
_GOOD_REQUEST = {'status': 'ok'}, 200

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
        if user.check_password(password):
            token = user.generate_auth_token(expiration=10000)

            return _GOOD_REQUEST, {'auth_token': token}


api.add_resource(Login, '/login')


class Register(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('password')
        self.parser.add_argument('email')
        self.parser.add_argument('week')
        self.parser.add_argument('month')
        self.parser.add_argument('year')
        self.parser.add_argument('auth_token')

    def post(self):
        args = self.parser.parse_args()
        email = args['email']
        password = args['password']
        week = args['week']
        month = args['month']
        year = args['year']

        if email is not None and password is not None:
            username = email[0:email.find('@')]
            user = User(
                email=email,
                username=username,
            )
            user.set_password(password)
            session.add(user)
            session.commit()
            status = Stats(
                user_id=user.id,
                week=week,
                month=month,
                year=year
            )
            session.add(status)
            session.commit()
            return _GOOD_REQUEST
        else:
            return _BAD_REQUEST


api.add_resource(Register, '/register')


class Test(Resource):

    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('token', location='headers')

    def post(self):
        args = self.parser.parse_args()
        token = args['token']

        user = User.verify_auth_token(token)

        return {'token': token, 'user': user['username']}


api.add_resource(Test, '/test')


class UserProfile(Resource):
    def get(self):
        user = User.query.get(4)
        if user is None:
            return _BAD_REQUEST
        else:
            user_stats = Stats.query.filter_by(user_id=user.id)
            done = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='DN').count()
            progres = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='IP').count()
            future = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='WR').count()
            user_profile = {
                "username": user.email[0:user.email.find('@')],
                "email": user.email,
                "week": user_stats[0].week,
                "year": user_stats[0].year,
                "month": user_stats[0].month,
                "DN": done,
                "IP": progres,
                "WR": future
            }
        return user_profile


api.add_resource(UserProfile, '/profile')


class LogOut(Resource):
    def post(self):
        session = db.session
        session.pop('email', None)
        return redirect('/login')


class DoneBooks(Resource):
    def get(self):
        global info_book
        user = User.query.get(4)
        done_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='DN').all()
        # count = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='DN').count()
        for book in done_book:
            book_id = book.books_id
            current_book = Books.query.get(book_id)
            info_book = {
                "Count": len(done_book),
                "Title": current_book.title,
                "Author": current_book.author,
                "Genre": current_book.genre
            }
        return info_book


api.add_resource(DoneBooks, '/books/read')


class ProgressBooks(Resource):
    def get(self):
        global info_book
        user = User.query.get(4)
        progress_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='IP').all()
        # count = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='IP').count()
        for book in progress_book:
            book_id = book.books_id
            current_book = Books.query.get(book_id)
            info_book = {
                "Count": len(progress_book),
                "Title": current_book.title,
                "Author": current_book.author,
                "Genre": current_book.genre
            }
            print(info_book)
        return info_book


api.add_resource(ProgressBooks, '/books/progres')


class FutureBooks(Resource):
    def get(self):
        global info_book
        user = User.query.get(4)
        future_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='WR').all()
        # count = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='WR').count()
        digits = []
        for book in future_book:
            book_id = book.books_id
            current_book = Books.query.get(book_id)
            info_book = {
                "Count": len(future_book),
                "Title": current_book.title,
                "Author": current_book.author,
                "Genre": current_book.genre
            }
            digits.append(info_book)
        return digits


api.add_resource(FutureBooks, '/books/future')
