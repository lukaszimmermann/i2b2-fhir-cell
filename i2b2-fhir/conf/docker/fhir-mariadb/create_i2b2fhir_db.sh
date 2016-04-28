PASS=$1
SQLST="create database i2b2fhir;
docker exec echo $SQLST| mysql -h $IP -D i2b2fhir -u ifcu --password=$MYSQL_PASS
create user ifcu identified by 'ifcp';
grant all privileges on i2b2fhir to ifcu;"

echo $SQLST| mysql -D i2b2fhir -u ifcu --password=$PASS

