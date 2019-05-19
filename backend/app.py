# import os
from . import create_app, db
# from sqlalchemy import create_engine

app = create_app()
# app.app_context().push()
# engine = create_engine(os.environ.get('DATABASE_URL'))
# db.engine = engine
# db.create_all(app=app)
