export INSTALL_DIR=i2b2-fhir-installdir
export GIT_NAME=cell-i2b2-fhir  #REPONAME
export GIT_URL=https://github.com/i2b2plugins/$GIT_NAME

#ABSOLUTE PATH TO INSTALL DIR
export AID=$PWD/$INSTALL_DIR

mkdir $INSTALL_DIR
cd $INSTALL_DIR

#export BRANCH=$(git symbolic-ref HEAD | sed -e 's,.*/\(.*\),\1,'); 
export BRANCH=$1

#export CONFIG_FILE_PATH=$2

export IPADD=$(ifconfig eth0 | grep inet | awk '{print $2}' | sed 's/addr://'|head -n 1)

export WILDFLY_DIR="$(pwd -P)/wildfly-9.0.1.Final"
export DEPLOY_DIR="$WILDFLY_DIR/standalone/deployments/"

export JAVA_HOME="$(pwd)/jdk1.8.0_60"
echo "DEPLOY_DIR:$DEPLOY_DIR"


export java="$JAVA_HOME/bin/java"
echo "java:$java" 

if [ "2" -eq "1" ]; then echo "hi"
fi

#check if java is installed

$(java 2>/dev/null )
if [ $? == 0 ]
  then echo "Java was not found. Please install java 1.7 or higher";
fi

JAVA_VER=$(java -version 2>&1 | sed 's/java version "\(.*\)\.\(.*\)\..*"/\1\2/; 1q');
echo "java version:$JAVA_VER" 

#if [ $JAVA_VER -lt 17 ]; then echo " Java 1.7 or higher is required. Installing .."
if [ -f jdk-8u60-linux-x64.tar.gz ] 
then echo ""
else
	#wget --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/7u79-b15/jdk-7u79-linux-x64.tar.gz
	wget --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u60-b27/jdk-8u60-linux-x64.tar.gz
	tar -xzf jdk-8u60-linux-x64.tar.gz
fi

#check if maven is installed
$(mvn 1>/dev/null );
echo $?

if [ $? == 0 ]
  then echo "mvn was not found. Installing maven" 
	
	if [ -f apache-maven-3.3.3-bin.tar.gz ] 
	then echo "mvn tar found"
	else

		wget http://apache.mirrors.ionfish.org/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
		tar -xzf apache-maven-3.3.3-bin.tar.gz
	fi

	export MAVEN_HOME="$(pwd)/apache-maven-3.3.3"
	echo "MAVEN_HOME=$MAVEN_HOME"
	export MVN="$MAVEN_HOME/bin/mvn"
	alias mvn="$MAVEN_HOME/bin/mvn"
	echo "MVN:$MVN" 

fi

#$MVN -version 1>/dev/null  
#echo $?
#if [ $? -ne 0 ];  then echo "mvn was not found. Please install maven  or higher"; fi

if [ -f wildfly-9.0.1.Final.tar.gz ]
then echo ""
else
	wget http://download.jboss.org/wildfly/9.0.1.Final/wildfly-9.0.1.Final.tar.gz
fi

if [ -d $WILDFLY_DIR ]
then echo ""
else
	tar -xzf wildfly-9.0.1.Final.tar.gz
	export CMD=" cat \"$WILDFLY_DIR/bin/standalone.conf\"| sed -e 's/MaxPermSize=256m/MaxPermSize=1024m/' | sed -e 's/Xmx512m/Xmx1024m/' > result; mv result $WILDFLY_DIR/bin/standalone.conf"
	echo $CMD
	cat "$WILDFLY_DIR/bin/standalone.conf"| sed -e 's/MaxPermSize=256m/MaxPermSize=1024m/'| sed -e 's/Xmx512m/Xmx1024m/' > result; mv result "$WILDFLY_DIR/bin/standalone.conf"
	#setting srv as default servlet
	##
	mkdir -p $WILDFLY_DIR/modules/system/layers/base/com/mysql/driver/main
	wget http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.9/mysql-connector-java-5.1.9.jar
	mv mysql-connector-java-5.1.9.jar $WILDFLY_DIR/modules/system/layers/base/com/mysql/driver/main/
	
	cp $AID/$GIT_NAME/i2b2-fhir/install/standalone-with-dbs/standalone.xml $WILDFLY_DIR/standalone/configuration/
	cp $AID/$GIT_NAME/i2b2-fhir/install/standalone-with-dbs/server.keystore $WILDFLY_DIR/standalone/configuration/
	cp $AID/$GIT_NAME/i2b2-fhir/install/standalone-with-dbs/module.xml $WILDFLY_DIR/modules/system/layers/base/com/mysql/driver/main/ 
fi

cd "$INSTALL_DIR"
echo "Installing source code from githib repository"
echo ">PWD:$PWD"

if [ -d $GIT_NAME ]
then echo "pulling from git repo";
cd $GIT_NAME; git --git-dir=.git pull; cd ..;

else echo "ERROR: repository not cloned in first install/run"; exit 1

fi

alias mvn=$MVN

echo "Compiling and deploying war"

export PATH="$PATH:$MAVEN_HOME/bin:$JAVA_HOME/bin"
cd $GIT_NAME/i2b2-fhir;
mvn clean install -Dmaven.test.skip=true; 
#echo PWD:$PWD
cd dstu21 ;
mvn install:install-file -DartifactId=validator -DgroupId=org.hl7.fhir.tools -Dfile=core/src/main/resources/org.hl7.fhir.validator.jar -Dversion=1.0 -Dpackaging=jar
mvn clean install -Dmaven.test.skip=true; 


#deploy
cp srv/target/*.war $DEPLOY_DIR
cp applib/target/*.war $DEPLOY_DIR

echo "running server"


export RUN_WF="$WILDFLY_DIR/bin/standalone.sh" #-b=$IPADD"
sh $RUN_WF


