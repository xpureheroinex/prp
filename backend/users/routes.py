from backend import db, api
from flask_restful import Resource, reqparse
from flask import request
from werkzeug.security import generate_password_hash

from backend.models import User
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


api.add_resource(Register, '/register')


class Login(Resource):
    def post(self):
        return 'hello, world!'


api.add_resource(Login, '/login')
