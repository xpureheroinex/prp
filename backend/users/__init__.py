from flask import Blueprint

bp = Blueprint('users', __name__)

from backend.users import routes
