#!/usr/bin/env bash
export FLASK_APP=backend/app.py
export FLASK_DEBUG=1
export DATABASE_URL=postgres:postgresql://user:password@localhost:5432/prp
flask run