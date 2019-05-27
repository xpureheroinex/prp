from werkzeug.utils import redirect
from backend import db, api
from flask_restful import Resource, reqparse

# from backend.models import User, UsersBooks, Stats, Books, Reviews
from backend import models
from sqlalchemy import func, desc, and_
import datetime

_BAD_REQUEST = {'message': 'unvalid data', 'status': 400}
_GOOD_REQUEST = {'message': 'ok', 'status': 200}

session = db.session


class Books(Resource):

    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')
        self.parser.add_argument('rate')

    def get(self, book_id):
        book = models.Books.query.filter_by(id=book_id).first()
        if book is None:
            return {'message': 'Book not found', 'status': 404}
        else:
            result = {'title': book.title,
                      'author': book.author,
                      'genre': book.genre,
                      'pages': book.pages,
                      'rate': book.rate}
            return {'book': result, 'status': 200}


    def post(self, book_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        rate = args['rate']
        user_id = models.User.verify_auth_token(token)['user_id']
        user = models.User.query.get(user_id)

        book = models.Books.query.filter_by(id=book_id).first()
        if book is None:
            return {'message': 'Book not found', 'status': 404}
        user_book = models.UsersBooks.query.filter_by(user_id=user.id).filter_by(books_id=book_id).first()
        if user_book is None:
            return _BAD_REQUEST
        if rate is not None:
            user_book.rate = rate
            books_list = models.UsersBooks.query.filter_by(books_id=book_id).all()
            rate, amount = 0, 0
            for elem in books_list:
                rate += float(elem.rate)
                amount += 1
            if amount > 0:
                average_rate = rate/amount
                book.rate = average_rate
                session.add(book)
            session.add(user_book)
            session.commit()
            return _GOOD_REQUEST
        return _BAD_REQUEST





api.add_resource(Books, '/books/<int:book_id>')