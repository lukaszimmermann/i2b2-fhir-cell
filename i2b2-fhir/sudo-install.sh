yum -y install git mysqld-server httpd mod_ssl

cat i2b2-fhir/install/conf/httpd/append_httpd.conf >> /etc/httpd/conf/httpd.conf
cat i2b2-fhir/install/conf/httpd/append_httpd.conf >> /etc/httpd/conf.d/ssl.conf

service httpd start
service mysqld start
chkconfig httpd on
chkconfig mysql on

cat i2b2-fhir/install/conf/db/create_db.sql | mysql -u root

