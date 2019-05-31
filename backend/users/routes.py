from werkzeug.utils import redirect

from backend import db, api
from flask_restful import Resource, reqparse

from backend.models import User, UsersBooks, Stats, Books, Reviews, Tokens
from sqlalchemy import func, desc, and_
import datetime

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
            return {'status': 404,
                    'message': f'User with email {email} does not exist'}
        else:
            if user.check_password(password):
                check_login = Tokens.query.filter_by(user_id=user.id).first()
                if check_login is None:
                    token = user.generate_auth_token(expiration=10000)
                    tkn = str(token)
                    new = Tokens(token=tkn[2:len(tkn) - 1], user_id=user.id)
                    session.add(new)
                    session.commit()
                    return _GOOD_REQUEST, {'Bearer': token}
                else:
                    return {'message': 'User already logged in', 'status': '400'}
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
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('password')
        self.parser.add_argument('username')
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        else:
            user_stats = Stats.query.filter_by(user_id=user.id).first()
            print(user_stats, 'HERE')
            done = UsersBooks.query.filter_by(user_id=user.id).filter_by(
                list='DN').count()
            progress = UsersBooks.query.filter_by(user_id=user.id).filter_by(
                list='IP').count()
            future = UsersBooks.query.filter_by(user_id=user.id).filter_by(
                list='WR').count()
            user_profile = {
                "username": user.username,
                "email": user.email,
                "week": user_stats.week,
                "year": user_stats.year,
                "month": user_stats.month,
                "done": done,
                "progress": progress,
                "future": future
            }
        return {'user': user_profile, 'status': 200}

    def put(self):
        args = self.parser.parse_args()
        username = args['username']
        password = args['password']
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        if user is None:
            return _BAD_REQUEST
        else:
            if username is not None:
                user.username = username
            if password is not None:
                user.set_password(password)
            session.commit()
            return {'message': 'successfully updated', 'status': 200}


api.add_resource(UserProfile, '/profile')


class Statistics(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('range')
        self.parser.add_argument('week')
        self.parser.add_argument('month')
        self.parser.add_argument('year')

        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}

        token = args['Authorization'].split(' ')[1]
        range = args['range']
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        status = Stats.query.filter_by(user_id=user.id).first()
        range_books = []

        date_to = datetime.date.today()
        date_from = datetime.date.today()
        if range == 'week':
            date_from = datetime.date.today() - datetime.timedelta(days=7)
        elif range == 'month':
            date_from = datetime.date.today() - datetime.timedelta(days=30)
        elif range == 'year':
            date_from = datetime.date.today() - datetime.timedelta(days=365)

        books = UsersBooks.query.filter_by(user_id=user.id).\
            filter(and_(func.date(UsersBooks.data_added) >= date_from),
                   func.date(UsersBooks.data_added) <= date_to).\
            filter_by(list='DN').all()

        count = len(books)
        if count  <= 0:
            fav_author = '-'
            fav_genre = '-'
        else:
            for book in books:
                range_books.append(book.books_id)

        fav_author = Books.query.with_entities(Books.author,
                                               func.count(Books.author)). \
            group_by(Books.author). \
            filter(Books.id.in_(range_books)). \
            order_by(desc(func.count(Books.author))). \
            first()[0]

        fav_genre = Books.query.with_entities(Books.genre,
                                              func.count(Books.genre)). \
            group_by(Books.genre). \
            filter(Books.id.in_(range_books)).order_by(
            desc(func.count(Books.genre))). \
            first()[0]

        divide = 0
        if range == 'week':
            divide = status.week
        elif range == 'month':
            divide = status.month
        elif range == 'year':
            divide = status.year

        if divide > 0:
            percent = f'{round(count * 100 / divide, 2)}%'
        else:
            percent = 'no info provided'
        info = {
            "count": count,
            "fav_author": fav_author,
            "fav_genre": fav_genre,
        }
        plan = {
            "plan": divide,
            "count": count,
            "percent": percent
        }
        return {"info": info, "plan": plan, 'status': 200}

    def put(self):
        args = self.parser.parse_args()
        week = args['week']
        month = args['month']
        year = args['year']
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}

        token = args['Authorization'].split(' ')[1]
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
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


class LogOut(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')

    def post(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}

        token = args['Authorization'].split(' ')[1]
        new = Tokens.query.filter_by(token=token).first()
        if new is not None:
            session.delete(new)
            session.commit()
            return {'message': 'User loged out', 'status': 200}
        else:
            return _BAD_REQUEST


api.add_resource(LogOut, '/logout')


class DoneBooks(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
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
                    "title": current_book.title,
                    "author": current_book.author,
                    "genre": current_book.genre
                }
                info.append(info_book)
        return {'count': count, 'info': info, 'status': 200}


api.add_resource(DoneBooks, '/books/read')


class ProgressBooks(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
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
                    "title": current_book.title,
                    "author": current_book.author,
                    "genre": current_book.genre
                }
                info.append(info_book)
        return {'count': count, 'info': info, 'status': 200}


api.add_resource(ProgressBooks, '/books/progress')


class FutureBooks(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('Authorization', location='headers')

    def get(self):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
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
                    "title": current_book.title,
                    "author": current_book.author,
                    "genre": current_book.genre
                }
                info.append(info_book)
        return {'count': count, 'info': info, 'status': 200}


api.add_resource(FutureBooks, '/books/future')


class AddReviews(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('text')
        self.parser.add_argument('Authorization', location='headers')

    def get(self, books_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}

        list_reviews = Reviews.query.filter_by(books_id=books_id).all()
        count = len(list_reviews)
        info = []
        if count != 0:
            for review in list_reviews:
                user_id = review.user_id
                user = User.query.get(user_id)
                info_review = {
                    "username": user.username,
                    "text": review.text,
                    'created': review.data_added.strftime(format='%d/%m/%Y'),
                }
                info.append(info_review)
            return {'count': count, 'info': info, 'status': 200}
        else:
            return {'message': 'No reviews about this book', 'status': 404}

    def post(self, books_id):
        args = self.parser.parse_args()
        if args['Authorization'] is None:
            return {'message': 'Unauthorized', 'status': 401}
        token = args['Authorization'].split(' ')[1]
        if User.verify_auth_token(token) is None:
            return {'message': 'Unauthorized', 'status': 401}
        user_id = User.verify_auth_token(token)['user_id']
        user = User.query.get(user_id)
        text = args['text']
        exist_user = Reviews.query.filter_by(user_id=user_id).filter_by(books_id=books_id).first()
        if exist_user is not None:
            return {'status': 400,
                    'message': f'User {user.username} already left review on this book'}
        elif text is not None:
            review = Reviews(
                user_id=user.id,
                books_id=books_id,
                text=text
            )
            session.add(review)
            session.commit()
            return {'message': 'Successfully created', 'status': 201}
        else:
            return _BAD_REQUEST


api.add_resource(AddReviews, '/books/<int:books_id>/reviews')


class RestorePass(Resource):

    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('email', required=True)

    def post(self):
        args = self.parser.parse_args()
        email = args['email']
        




api.add_resource(RestorePass, '/login/restore')