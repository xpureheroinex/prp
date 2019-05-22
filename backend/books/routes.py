from backend import db
from . import bp

@bp.route('/', methods=['GET'])
def login():
    return 'Hello'