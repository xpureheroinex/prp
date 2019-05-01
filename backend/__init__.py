import os
from backend.config import Config
from flask import Flask
from flask_migrate import Migrate
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()
migrate = Migrate()

def create_app(config_class=Config):
    from . import db, users
    app = Flask(__name__)
    app.config.from_object(config_class)
    db.init_app(app)
    migrate.init_app(app, db)

    from backend.users import bp as users_bp
    app.register_blueprint(users_bp, url_prefix='/')

    from backend.books import bp as books_bp
    app.register_blueprint(books_bp, url_prefix='/books')

    return app
