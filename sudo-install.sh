export INSTALL_DIR=i2b2-fhir-installdir
export GIT_NAME=cell-i2b2-fhir  #REPONAME
export GIT_URL=https://github.com/i2b2plugins/$GIT_NAME
export CELL_PATH=$INSTALL_DIR/$GIT_NAME

echo "CELL_PATH:$CELL_PATH"

timestamp(){
        date +"%Y-%m-%d_%H-%M-%S"
}
export TS=$(timestamp)

yum -y install git mysql-server httpd mod_ssl


mv  /etc/httpd/conf/httpd.conf  /etc/httpd/conf/httpd.conf_$TS
mv  /etc/httpd/conf.d/ssl.conf  /etc/httpd/conf.d/ssl_.conf_$TS
cp $CELL_PATH/i2b2-fhir/install/conf/httpd/httpd.conf  /etc/httpd/conf/httpd.conf
cp $CELL_PATH/i2b2-fhir/install/conf/httpd/ssl.conf  /etc/httpd/conf.d/ssl.conf

service httpd start
service mysqld start
chkconfig httpd on
chkconfig mysql on

cat $CELL_PATH/i2b2-fhir/install/conf/db/create_db.sql | /usr/bin/mysql -u root



