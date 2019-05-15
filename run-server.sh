#!/usr/bin/env bash
export FLASK_APP=backend
export FLASK_DEBUG=1
export DATABASE_URL=postgres://docker:docker@127.0.0.1:5432/docker
flask run