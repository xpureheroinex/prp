import os
basedir = os.path.abspath(os.path.dirname(__file__))


class Config(object):
    DEBUG = True
    TESTING = False
    CSRF_ENABLED = False
    SECRET_KEY = 'very-secret-key-ur-welcome'
