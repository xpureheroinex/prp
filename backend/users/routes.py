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
            session = db.session
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


class UserProfile(Resource):
    def get(self):
        user = User.query.get(7)
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
        user = User.query.get(4)
        done_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='DN').all()


api.add_resource(DoneBooks, '/books/read')


class ProgresBooks(Resource):
    def get(self):
        user = User.query.get(id=4)
        progres_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(UsersBooks.list == 'IP').all()

        print(progres_book)


api.add_resource(ProgresBooks, '/books/progres')


class FutureBooks(Resource):
    def get(self):
        user = User.query.get(id=4)
        future_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list == 'WR').all()

        print(future_book)


api.add_resource(FutureBooks, '/books/future')
