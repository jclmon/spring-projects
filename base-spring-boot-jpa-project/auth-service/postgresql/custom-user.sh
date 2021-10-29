#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
        CREATE USER docker PASSWORD root;
        CREATE DATABASE user;
        GRANT ALL PRIVILEGES ON DATABASE docker TO user;
EOSQL