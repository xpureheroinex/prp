from werkzeug.security import generate_password_hash
from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify

from backend.models import User, UsersBooks, Stats, Books
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
