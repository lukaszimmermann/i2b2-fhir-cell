BASE=$1
IP=$2
IFQS=$3

MYSQL_PASS=mariai2b2fhir


sudo docker stop fhir-mariadb; sudo docker rm fhir-mariadb;
#sudo docker rmi -f i2b2/fhir-mariadb
sudo docker build -t i2b2/fhir-mariadb $BASE/conf/docker/fhir-mariadb/;
sudo docker run --name=fhir-mariadb -d -p 3306:3306  -e "MYSQL_ROOT_PASSWORD=$MYSQL_PASS" --net i2b2-net i2b2/fhir-mariadb
sleep 5;
sudo docker exec -it fhir-mariadb bash -c "/i2b2fhir_db.sh $MYSQL_PASS; "


#sudo docker stop fhir-mysql; sudo docker rm fhir-mysql;
#sudo docker rmi -f i2b2/fhir-mysql
#docker build -t i2b2/fhir-mysql $BASE/conf/docker/fhir-mysql/;
#docker run --name=fhir-mysql -d -p 3306:3306  -e "MYSQL_ROOT_PASSWORD=$MYSQL_PASS" i2b2/fhir-mysql
#sleep 10;
#sudo docker exec -it fhir-mysql bash -c "/i2b2fhir_db.sh $MYSQL_PASS; "
#exit

#sed -i "s/services.i2b2.org:9090/i2b2-web/" $BASE/dstu21/srv/src/main/resources/application.properties
#sed -i "s/localhost:3306/fhir-mariadb:3306/" $BASE/local/
sh $BASE/scripts/install/install.sh $IFQS 

cp -rv $BASE/conf/docker/fhir-wildfly/* $BASE/local/docker/fhir-wildfly/
cp $IFQS/packages/jdk-8u60-linux-x64.tar.gz $BASE/local/docker/fhir-wildfly/
sudo docker stop fhir-wildfly; sudo docker rm fhir-wildfly;
docker build -t i2b2/fhir-wildfly $BASE/local/docker/fhir-wildfly/;
docker run --name=fhir-wildfly -d -p 9090:9090 -p 11000:11000 --net i2b2-net i2b2/fhir-wildfly
#docker exec -it fhir-wildfly bash -c /confI2b2Url.sh  "http://192.168.254.144/i2b2"
