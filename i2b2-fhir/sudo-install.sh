timestamp(){
	date +"%Y-%m-%d_%H-%M-%S" 
}
export TS=$(timestamp)

yum -y install git mysql-server httpd mod_ssl

mv  /etc/httpd/conf/httpd.conf  /etc/httpd/conf/httpd.conf_$TS
mv  /etc/httpd/conf.d/ssl.conf  /etc/httpd/conf.d/ssl_.conf_$TS
cp i2b2-fhir/install/conf/httpd/httpd.conf  /etc/httpd/conf/httpd.conf
cp i2b2-fhir/install/conf/httpd/ssl.conf  /etc/httpd/conf.d/ssl.conf

service httpd start
service mysqld start
chkconfig httpd on
chkconfig mysql on

cat i2b2-fhir/install/conf/db/create_db.sql | /usr/bin/mysql -u root

