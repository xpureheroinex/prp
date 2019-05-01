from flask import Blueprint

bp = Blueprint('books', __name__)

from backend.books import routes
