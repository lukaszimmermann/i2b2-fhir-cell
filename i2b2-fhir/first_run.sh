export INSTALL_DIR=i2b2-fhir-installdir
export GIT_NAME=cell-i2b2-fhir  #REPONAME
export GIT_URL=https://github.com/i2b2plugins/$GIT_NAME

export ABS_INSTALL_DIR=$PWD/$INSTALL_DIR
export CELL_PATH=$ABS_INSTALL_DIR/$GIT_NAME


sudo -u nobody bash -c : && RUNAS="sudo -u $SUDO_USER"

echo "$USER"
#wget https://raw.githubusercontent.com/i2b2plugins/cell-i2b2-fhir/master/i2b2-fhir/install.sh 


#GET GIT_REPO

if [ -d $ABS_INSTALL_DIR/ ]
then echo ""
else sudo yum -y install git
fi

$RUNAS bash << _
echo \$USER
if [ -d $ABS_INSTALL_DIR/ ]
then echo "DIR ALREADY EXISTS";
else mkdir $ABS_INSTALL_DIR
echo "running installation for first time"
fi
cd $ABS_INSTALL_DIR
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


if [ -f $ABS_INSTALL_DIR/ran_sudo ]
then echo ""
else echo "Running sudo script"
cd $ABS_INSTALL_DIR/../
echo ">>PWD:$PWD"
sh $CELL_PATH/i2b2-fhir/sudo-install.sh
echo ""> $ABS_INSTALL_DIR/ran_sudo

fi

 
#sudo echo "Hi" > /etc/hi

#RUN_UPDATE_AND_SERVER

$RUNAS bash << _
echo \$USER
cd $ABS_INSTALL_DIR/../
sh $CELL_PATH/i2b2-fhir/install.sh master
_
echo "$USER"
