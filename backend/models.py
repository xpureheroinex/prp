import enum
from time import time
from datetime import datetime
from . import db, create_app, config
from werkzeug.security import generate_password_hash, check_password_hash

from itsdangerous import (TimedJSONWebSignatureSerializer
                          as Serializer, BadSignature, SignatureExpired)

# from flask_login import UserMixin

app = create_app()


class ListChoices(enum.Enum):
    DN = 'done'
    IP = 'in progress'
    WR = 'want to read'


class User(db.Model):

    __tablename__ = 'user'

    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(64), index=True, unique=True)
    username = db.Column(db.String(64), index=True, unique=False)
    password = db.Column(db.String(128))

    def __init__(self, email, username, **kwargs):
        self.email = email
        self.username = username

    @staticmethod
    def is_authenticated():
        return True

    def set_password(self, password):
        self.password = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password, password)

    def get_id(self):
        return self.id

    def repr(self):
        return f'<User {self.email}>'

    def generate_auth_token(self, expiration=600):
        s = Serializer(app.config['SECRET_KEY'], expires_in=expiration)
        return s.dumps({'id': self.id, 'username': self.username})


    @staticmethod
    def verify_auth_token(token):
        s = Serializer(app.config['SECRET_KEY'])
        try:
            data = s.loads(token)
        except SignatureExpired:
            return None  # valid token, but expired
        except BadSignature:
            return None  # invalid token
        user_id = data['id']
        username = data['username']
        tkn = Tokens.query.filter_by(user_id=user_id).first()
        if tkn is None:
            return None
        elif tkn.token == token:
            return {'user_id': user_id, 'username': username}
        else:
            return None


class Books(db.Model):

    __tablename__ = 'books'

    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(128), unique=False)
    author = db.Column(db.String(128))
    genre = db.Column(db.String(64))
    pages = db.Column(db.Integer)
    rate = db.Column(db.Float(), default=0)

    def repr(self):
        return f'<Books {self.title}>'


class Notes(db.Model):

    __tablename__ = 'notes'

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    books_id = db.Column(db.Integer, db.ForeignKey('books.id'))
    title = db.Column(db.String(64))
    text = db.Column(db.String)
    data_added = db.Column(db.DateTime, default=datetime.utcnow)

    def repr(self):
        return f'<Notes {self.title}>'


class Reviews(db.Model):

    __tablename__ = 'reviews'

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    books_id = db.Column(db.Integer, db.ForeignKey('books.id'))
    text = db.Column(db.String)
    data_added = db.Column(db.DateTime, default=datetime.utcnow)

    def repr(self):
        return f'<Reviews {self.books_id}>'

    def __init__(self, user_id, books_id, text, **kwargs):
        self.user_id = user_id
        self.books_id = books_id
        self.text = text


class UsersBooks(db.Model):

    __tablename__ = 'user_books'

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    books_id = db.Column(db.Integer, db.ForeignKey('books.id'))
    list = db.Column(db.Enum(ListChoices))
    data_added = db.Column(db.DateTime, default=datetime.utcnow)
    rate = db.Column(db.Integer, default=0)

    def repr(self):
        return f'<UsersBooks {self.user.username}>'


class Stats(db.Model):

    __tablename__ = 'stats'

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    week = db.Column(db.Integer, default=0)
    month = db.Column(db.Integer, default=0)
    year = db.Column(db.Integer, default=0)

    def repr(self):
        return f'<Stats of {self.user_id} user>'

class Tokens(db.Model):

    __tablename__ = 'tokens'

    id = db.Column(db.Integer, primary_key=True)
    token = db.Column(db.String, unique=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
