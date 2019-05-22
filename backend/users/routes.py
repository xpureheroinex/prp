from werkzeug.security import generate_password_hash
from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse
from flask import request, jsonify

from backend.models import Books
from . import bp

_BAD_REQUEST = {'status': 'error'}, 400, 500
_GOOD_REQUEST = {'status': 'ok'}, 200


class Search(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('search')

    def post(self):
        args = self.parser.parse_args()
        search = args['search']
        info = []
        list_books_genre = Books.query.filter_by(genre=search).all()
        list_books_author = Books.query.filter_by(author=search).all()
        list_books_title = Books.query.filter_by(title=search).all()
        for book1 in list_books_genre:
            genre = {
                "Title": book1.title,
                "Author": book1.author,
                "Genre": book1.genre
            }
            info.append(genre)
        for book2 in list_books_author:
            author = {
                "Title": book2.title,
                "Author": book2.author,
                "Genre": book2.genre
            }
            info.append(author)
        for book3 in list_books_title:
            title = {
                "Title": book3.title,
                "Author": book3.author,
                "Genre": book3.genre
            }
            info.append(title)
        return {'len': len(list_books_genre), 'genre': info}


api.add_resource(Search, '/books/search')
