import enum
from time import time
from datetime import datetime
from . import db, create_app
from werkzeug.security import generate_password_hash, check_password_hash
# from flask_login import UserMixin


class ListChoices(enum.Enum):
    DN = 'done'
    IP = 'in progress'
    WR = 'want to read'


class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(64), index=True, unique=False)
    email = db.Column(db.String(64), index=True, unique=True)
    password = db.Column(db.String(128))

    def repr(self):
        return f'<User {self.username}>'

    def set_password(self, password):
        self.password = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password, password)


class Books(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(128), unique=False)
    author = db.Column(db.String(128))
    genre = db.Column(db.String(64))
    pages = db.Column(db.Integer(32))
    rate = db.Column(db.Float())

    def repr(self):
        return f'<Books {self.title}>'


class Notes(db.Model):
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    books_id = db.Column(db.Integer, db.ForeignKey('books.id'))
    title = db.Column(db.String(64))
    text = db.Column(db.String)
    data_added = db.Column(db.DateTime, default=datetime.utcnow)

    def repr(self):
        return f'<Notes {self.title}>'


class Reviews(db.Model):
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    books_id = db.Column(db.Integer, db.ForeignKey('books.id'))
    text = db.Column(db.String)
    data_added = db.Column(db.DateTime, default=datetime.utcnow)

    def repr(self):
        return f'<Reviews {self.books_id}>'


class UsersBooks(db.Model):
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))
    books_id = db.Column(db.Integer, db.ForeignKey('books.id'))
    list = db.Column(db.Enum(ListChoices))
    data_added = db.Column(db.DateTime, default=datetime.utcnow)
    rate = db.Column(db.Integer)

    def repr(self):
        return f'<Users_Books {self.user_id}>'


