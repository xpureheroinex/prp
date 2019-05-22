from werkzeug.security import generate_password_hash
from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify

from backend.models import User, UsersBooks, Stats, Books
from . import bp

_BAD_REQUEST = {'status': 'error'}, 400
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
                user_id=user.id
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
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('password')
        self.parser.add_argument('username')
        self.parser.add_argument('auth_token')

    def get(self):
        user = User.query.get(4)
        if user is None:
            return _BAD_REQUEST
        else:
            user_stats = Stats.query.filter_by(user_id=user.id)
            done = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='DN').count()
            progress = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='IP').count()
            future = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='WR').count()
            user_profile = {
                "username": user.email[0:user.email.find('@')],
                "email": user.email,
                "week": user_stats[0].week,
                "year": user_stats[0].year,
                "month": user_stats[0].month,
                "DN": done,
                "IP": progress,
                "WR": future
            }
        return user_profile

    def put(self):
        args = self.parser.parse_args()
        username = args['username']
        password = args['password']
        user = User.query.get(4)
        if user is None:
            return _BAD_REQUEST
        else:
            update_user = User.query.filter_by(id=user.id).first()
            if username is not None:
                update_user.username = username
            if password is not None:
                update_user.set_password(password)
            session.commit()


api.add_resource(UserProfile, '/profile')


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
        user = User.query.get(58)
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

