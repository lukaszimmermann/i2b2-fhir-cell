PASS=$1
SQLST="create database i2b2fhir;create user ifcu identified by 'ifcp';grant all privileges on i2b2fhir.* to ifcu;"

echo $SQLST| mysql -u root --password=$PASS

