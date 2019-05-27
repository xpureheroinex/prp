from . import create_app, db
import csv
from socket import gethostname

app = create_app()
app.app_context().push()
if __name__ == '__main__':
    if 'liveconsole' not in gethostname():
        app.run()

# db.create_all(app=app)

session = db.session()

######## DO NOT DELETE  ###########

# @app.route('/add')
# def import_books():
#     file = open("/home/d/files/prp/books_cleared.csv")
#     reader = csv.reader(file)
#     for id, title, author, genre, pages in reader:
#         session.execute("""INSERT INTO Books (id, title, author, genre, pages, rate)
#         VALUES (:id, :title, :author, :genre, :pages, :rate)""", {'id': id, 'title': title,
#         'author': author, 'genre': genre, 'pages': pages, 'rate': 0})
#     session.commit()
#     return 'ok'
