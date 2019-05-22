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


class Statistics(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('week')
        self.parser.add_argument('month')
        self.parser.add_argument('year')

    def get(self):
        user = User.query.get(4)
        if user is None:
            return _BAD_REQUEST
        else:
            status = Stats.query.filter_by(user_id=user.id).first()
            done_book = UsersBooks.query.filter_by(user_id=user.id).filter_by(list='DN').all()
            count = len(done_book)
            percent = count * 100 / status.week
            info = {
                "Read": len(done_book)
            }
            plan = {
                "Want to read": status.week,
                "Read": len(done_book),
                "%": percent
            }
        return {"Info": info, "Plan": plan}

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
