from werkzeug.utils import redirect
from backend import db, api
from flask_restful import Resource, reqparse
from backend import models

_BAD_REQUEST = {'message': 'unvalid data', 'status': 400}
_GOOD_REQUEST = {'message': 'ok', 'status': 200}

session = db.session


class Books(Resource):

    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')
        self.parser.add_argument('rate')
        self.parser.add_argument('status')

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
        if models.User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        rate = args['rate']
        user_id = models.User.verify_auth_token(token)['user_id']
        user = models.User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST

        book = models.Books.query.filter_by(id=book_id).first()
        if book is None:
            return {'message': 'Book not found', 'status': 404}
        user_book = models.UsersBooks.query.filter_by(user_id=user.id).filter_by(books_id=book_id).first()
        print(user_book)
        if user_book is None:
            user_book = models.UsersBooks(user_id=user.id, books_id=book_id)
            session.add(user_book)
        if rate is not None:
            user_book.rate = rate
            books_list = models.UsersBooks.query.filter_by(books_id=book_id).all()
            rate, amount = 0, 0
            for elem in books_list:
                rate += float(elem.rate)
                amount += 1
            if amount > 0:
                average_rate = round(rate/amount, 2)
                book.rate = average_rate
                session.add(book)
            session.add(user_book)
            session.commit()
            return _GOOD_REQUEST
        else:
            return _BAD_REQUEST

    def put(self, book_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if models.User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        status = args['status']
        user_id = models.User.verify_auth_token(token)['user_id']
        user = models.User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        book = models.Books.query.filter_by(id=book_id).first()
        if book is None:
            return {'message': 'Book not found', 'status': 404}
        user_book = models.UsersBooks.query.filter_by(user_id=user.id).filter_by(books_id=book_id).first()
        if status is not None:
            if status not in ['DN', 'IP', 'WR']:
                return _BAD_REQUEST
            elif user_book is None:
                new = models.UsersBooks(user_id=user.id,
                                        books_id=book_id,
                                        list=status)
                session.add(new)
            else:
                user_book.list = status
                session.add(user_book)
            session.commit()
            return _GOOD_REQUEST
        else:
            return _BAD_REQUEST

    def delete(self, book_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if models.User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = models.User.verify_auth_token(token)['user_id']
        user = models.User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        user_book = models.UsersBooks.query.filter_by(
            user_id=user.id).filter_by(books_id=book_id).first()
        if user_book is not None:
            session.delete(user_book)
            session.commit()
            return _GOOD_REQUEST
        else:
            return _BAD_REQUEST


api.add_resource(Books, '/books/<int:book_id>')


class Notes(Resource):

    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')
        self.parser.add_argument('title')
        self.parser.add_argument('text')


    def get(self, book_id):
        book = models.Books.query.filter_by(id=book_id).first()
        if book is None:
            return _BAD_REQUEST
        notes = models.Notes.query.filter_by(books_id=book_id).all()

        response = []
        for note in notes:
            username = models.User.query.filter_by(id=note.user_id).first().username
            elem = {
                'id': note.id,
                'title': note.title,
                'text': note.text,
                'author': username,
                'created': note.data_added.strftime(format='%d/%m/%Y')
            }
            response.append(elem)

        return {'notes': response, 'status': 200}

    def post(self, book_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        text = args['text']
        title = args['title']
        user_id = models.User.verify_auth_token(token)['user_id']
        user = models.User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        book = models.Books.query.filter_by(id=book_id).first()
        if book is None:
            return _BAD_REQUEST
        note = models.Notes(user_id=user.id,
                            books_id=book_id,
                            text=text,
                            title=title)
        session.add(note)
        session.commit()
        return {'message': 'Successfully created', 'status': 201}


api.add_resource(Notes, '/books/<int:book_id>/notes')

class OneNote(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')
        self.parser.add_argument('title')
        self.parser.add_argument('text')

    def put(self, note_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        text = args['text']
        title = args['title']
        user_id = models.User.verify_auth_token(token)['user_id']
        user = models.User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        new_note = models.Notes.query.filter_by(id=note_id).first()
        if new_note is None:
            return _BAD_REQUEST
        elif new_note.user_id == user.id:
            if text is not None:
                new_note.text = text
            if title is not None:
                new_note.title = title
            session.add(new_note)
            session.commit()
            return _GOOD_REQUEST
        else:
            return {'message': 'Forbidden', 'status': 403}

    def delete(self, note_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        text = args['text']
        title = args['title']
        user_id = models.User.verify_auth_token(token)['user_id']
        user = models.User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        note = models.Notes.query.filter_by(id=note_id).first()
        if note is None:
            return _BAD_REQUEST
        elif note.user_id == user.id:
            session.delete(note)
            session.commit()
            return _GOOD_REQUEST
        else:
            return {'message': 'Forbidden', 'status': 403}


api.add_resource(OneNote, '/books/notes/<int:note_id>')
