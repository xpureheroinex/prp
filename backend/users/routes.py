from backend import db
from . import bp

@bp.route('/login', methods=['GET'])
def login():
    return "LOGIN HERE"