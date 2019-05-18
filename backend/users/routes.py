from werkzeug.security import generate_password_hash

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request
from operator import xor

from backend.models import User
from . import bp

_BAD_REQUEST = {'status': 'error'}, 400, 500
_GOOD_REQUEST = {'status': 'ok'}, 200


class Login(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('username')
        self.parser.add_argument('password')
        self.parser.add_argument('email')

    def post(self):
        args = self.parser.parse_args()
        email = args['email']
        username = args['username']
        password = args['password']

        if xor(bool(username is None), bool(email is None)) and password is not None:
            user = User(
                email=email,
                password=generate_password_hash(password)
            )
            session = db.session
            session.add(user)
            session.commit()

            return _GOOD_REQUEST
        else:
            return _BAD_REQUEST
            # return flask.abort(400)


api.add_resource(Login, '/login')


class Register(Resource):
    def post(self):
        return 'hello, world!'


api.add_resource(Register, '/register')
