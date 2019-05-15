from backend import db, api
from flask_restful import Resource
from . import bp


class Login(Resource):
    def get(self):
        return "<h1>test url</h1>"


api.add_resource(Login, '/login')
