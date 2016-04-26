BASE=$1
IP=$2
MYSQL_PASS=mariai2b2fhir
sudo docker stop fhir-mariadb; sudo docker rm fhir-mariadb;
sudo docker rmi -f i2b2/i2b2-mariadb

docker build -t i2b2/i2b2-mariadb $BASE/install/conf/docker/maria-db/;
docker run --name=fhir-mariadb -d -p 3306:3306  -e "MYSQL_ROOT_PASSWORD=$MYSQL_PASS" i2b2/i2b2-mariadb
sleep 5;
sudo docker exec -it fhir-mariadb bash -c "/i2b2fhir_db.sh $MYSQL_PASS; "
exit
sed -i "s/services.i2b2.org:9090/i2b2-wildfly/" $BASE/dstu21/srv/src/main/resources/application.properties
sh $BASE/install.sh 


