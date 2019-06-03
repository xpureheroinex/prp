import re

from sqlalchemy import or_
from werkzeug.security import generate_password_hash
from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify

from backend.models import Books, User
from . import bp

_BAD_REQUEST = {'status': 'error'}, 400, 500
_GOOD_REQUEST = {'status': 'ok'}, 200


class Search(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('search')
        self.parser.add_argument('Authorization', location='headers')

    def post(self):
        args = self.parser.parse_args()
        search = args['search']
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        elif search is not None:
            info = []
            research = search.strip()
            s = f'%{research}%'
            print(research)
            result = Books.query.filter(or_(Books.genre.ilike(s), Books.title.ilike(s), Books.author.ilike(s))).all()
            print(result)

            for book in result:
                listbook = {
                    "id": book.id,
                    "Title": book.title,
                    "Author": book.author,
                    "Genre": book.genre
                }
                info.append(listbook)
        return {'count': len(info), 'Results of searching': info, 'status': 200}


api.add_resource(Search, '/books/search')
