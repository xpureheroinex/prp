from backend import db, api
from flask_restful import Resource
from . import bp


class Login(Resource):
    def get(self):
        return "test url"

api.add_resource(Login, '/login')