export INSTALL_DIR=i2b2-fhir-installdir
export GIT_NAME=cell-i2b2-fhir  #REPONAME
export GIT_URL=https://github.com/i2b2plugins/$GIT_NAME
export CELL_PATH=$INSTALL_DIR/$GIT_NAME

export ABS_INSTALL_DIR=$PWD/$INSTALL_DIR

sudo -u nobody bash -c : && RUNAS="sudo -u ec2-user"

echo "$USER"
#wget https://raw.githubusercontent.com/i2b2plugins/cell-i2b2-fhir/master/i2b2-fhir/install.sh 


#GET GIT_REPO

$RUNAS bash << _
echo \$USER
cd $ABS_INSTALL_DIR/../
if [ -d $GIT_NAME ]
then echo "pulling from git repo";
cd $GIT_NAME; git --git-dir=.git pull; cd ..;
else echo "cloning from git repo";
git clone $GIT_URL;
cd $GIT_NAME;
git --git-dir=.git checkout "$BRANCH";
cd ../;
fi 
_

cd $ABS_INSTALL_DIR/../
echo ">>PWD:$PWD"
sh sudo-install.sh

 
#sudo echo "Hi" > /etc/hi

#RUN_UPDATE_AND_SERVER

$RUNAS bash << _
echo \$USER
cd $ABS_INSTALL_DIR/../
sh install.sh
_
echo "$USER"
