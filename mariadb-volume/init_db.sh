#!/usr/bin/env bash
set -e

DATA_DIR=/tmp/datadir
DATABASE_NAME=i2b2fhir
USER_NAME=ifcu
USER_PASSWORD=ifcp

SOCKET=/var/run/mysqld/mysqld.sock

# Create database and start mysqld
mysql_install_db --datadir="${DATA_DIR}"
/usr/bin/mysqld_safe --datadir="${DATA_DIR}" &

# Wait for socket to appear
while [ ! -S "${SOCKET}" ]; do sleep 1; done

echo "CREATE DATABASE ${DATABASE_NAME};" | mysql -u root
echo "CREATE USER ${USER_NAME} IDENTIFIED BY '${USER_PASSWORD}';" | mysql -u root -D "${DATABASE_NAME}"
echo "GRANT ALL PRIVILEGES ON \`${DATABASE_NAME}\`.* TO '${USER_NAME}'@'%';" | mysql -u root -D "${DATABASE_NAME}"
echo "SHUTDOWN;" | mysql -u root


# Wait for the socket do disappear
while [ -S "${SOCKET}" ]; do sleep 1; done
