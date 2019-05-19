import os
from backend.config import Config
from flask import Flask
from flask_migrate import Migrate
from flask_restful import Resource, Api
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()
migrate = Migrate()
api = Api()


def create_app(config_class=Config):
    from . import db, users
    app = Flask(__name__)
    app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get('DATABASE_URL')
    # app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = True
    app.config.from_object(config_class)
    db.init_app(app)
    migrate.init_app(app, db)
    api.init_app(app)

    from backend.users import bp as users_bp
    app.register_blueprint(users_bp, url_prefix='/')

    from backend.books import bp as books_bp
    app.register_blueprint(books_bp, url_prefix='/books')

    return app


app = create_app()
