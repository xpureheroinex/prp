from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify
from operator import xor

from backend.models import User, UsersBooks, Stats, Books
from . import bp

_BAD_REQUEST = {'status': 'error'}, 400, 500
_GOOD_REQUEST = {'status': 'ok'}, 200


class DoneBooks(Resource):
    def get(self):
        global info_book
        user = User.query.get(4)
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
    def get(self):
        global info_book
        user = User.query.get(4)
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
    def get(self):
        global info_book
        user = User.query.get(4)
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


class Search(Resource):
    def post(self):
        args = self.parser.parse_args()
        search = args['search']


api.add_resource(Search, '/books/search')
