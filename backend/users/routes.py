from werkzeug.security import generate_password_hash
from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify
from operator import xor

from backend.models import User, UsersBooks, Stats
from . import bp

_BAD_REQUEST = {'status': 'error'}, 400, 500
_GOOD_REQUEST = {'status': 'ok'}, 200
session = db.session

class Register(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('password')
        self.parser.add_argument('email')

    def post(self):
        args = self.parser.parse_args()
        email = args['email']
        password = args['password']

        if email is not None and password is not None:
            username = email[0:email.find('@')]
            user = User(
                email=email,
                username=username,
            )
            user.set_password(password)
            session.add(user)
            session.commit()

            return _GOOD_REQUEST
        else:
            return _BAD_REQUEST
            # return flask.abort(400)


api.add_resource(Register, '/register')


class Login(Resource):
    def post(self):
        return 'hello, world!'


api.add_resource(Login, '/login')


class Statistics(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('week')
        self.parser.add_argument('month')
        self.parser.add_argument('year')

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


api.add_resource(Statistics, '/stats')


class UserProfile(Resource):
    def get(self):
        user = User.query.get(4)
        user_book = UsersBooks.query.filter_by(user_id='4').all()
        user_stats = Stats.guery.get(user=user).all()

        userprofile = {
            "username": user.email[0:user.email.find('@')],
            "email": user.email,
            "week": user_stats.week,
            "year": user_stats.year,
            "month": user_stats.month,
            "DN": user_book.query.filter_by(UsersBooks.list == 'DN').all().count(),
            "IP": user_book.query.filter_by(UsersBooks.list == 'IP').all().count(),
            "WR": user_book.query.filter_by(UsersBooks.list == 'WR').all().count()
        }
        return userprofile


api.add_resource(UserProfile, '/profile/1')


class LogOut(Resource):
    def post(self):
        session = db.session
        session.pop('email', None)
        return redirect('/login')


class DoneBooks(Resource):
    def get(self):
        # args = self.parser.parse_args()
        # user_id = args['user_id']
        user = User.query.get(4)
        done_book = UsersBooks.query.filter_by(list='DN', user_id=user.id).first()

        # print(done_book)
        return user.email
        # return done_book
        # print(user.email)


api.add_resource(DoneBooks, '/books/read')


class ProgresBooks(Resource):
    def get(self, user_id):
        # args = self.parser.parse_args()
        # user_id = args['user_id']
        user = User.query.get(id=user_id)
        progres_book = UsersBooks.query.filter_by(UsersBooks.list == 'IP', user=user).all()

        print(progres_book)


api.add_resource(ProgresBooks, '/books/progres')


class FutureBooks(Resource):
    def get(self, user_id):
        # args = self.parser.parse_args()
        # user_id = args['user_id']
        user = User.query.get(id=user_id)
        future_book = UsersBooks.query.filter_by(list == 'WR', user=user).all()

        print(future_book)


api.add_resource(FutureBooks, '/books/future')
