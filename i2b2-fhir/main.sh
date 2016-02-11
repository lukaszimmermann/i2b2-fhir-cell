sudo -u nobody bash -c : && RUNAS="sudo -u ec2-user"

echo "$USER"
#wget https://raw.githubusercontent.com/i2b2plugins/cell-i2b2-fhir/master/i2b2-fhir/install.sh 
wget https://raw.githubusercontent.com/i2b2plugins/cell-i2b2-fhir/master/i2b2-fhir/sudo-install.sh 
sh sudo-install.sh


 
sudo echo "Hi" > /etc/hi

$RUNAS bash << _
echo \$USER
sh install.sh
_
echo "$USER"
