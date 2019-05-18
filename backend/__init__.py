import os
from . import config
from flask import Flask
from flask_migrate import Migrate, MigrateCommand
# from flask_script import Manager
from flask_restful import Resource, Api
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()
migrate = Migrate()
api = Api()
# manager = Manager()


def create_app(config_class=config.Config):
    app = Flask(__name__)
    app.config.from_object(config_class)
    db.init_app(app)
    migrate.init_app(app, db)
    api.init_app(app)
    # manager.init_app(app)
    # manager.add_command('db', MigrateCommand)

    from .users import bp as users_bp
    app.register_blueprint(users_bp, url_prefix='/')

    from .books import bp as books_bp
    app.register_blueprint(books_bp, url_prefix='/books')

    return app
