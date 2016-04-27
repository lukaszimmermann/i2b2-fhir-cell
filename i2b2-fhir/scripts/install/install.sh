I2B2_QS=$1
source $I2B2_QS/scripts/install/install.sh

export MAVEN_HOME=$LOCAL/mvn

MVN_FILE=apache-maven-3.3.3-bin.tar.gz 

#check if the home directories are found as specified by user, or use default dirs
[ -d $MAVEN_HOME ] || MAVEN_HOME=$LOCAL/${MVN_FILE/-bin\.tar\.gz/}
echo "MAVEN_HOME:$MAVEN_HOME"
alias mvn="$MAVEN_HOME/bin/mvn"
#exit

install_maven(){
	cd $LOCAL
        if [ -f $MVN_FILE ] 
	then
		echo "found Maven file"
	else
                wget http://apache.mirrors.ionfish.org/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
                tar -xzf $MVN_FILE
        fi
}


configure_wildfly(){

	if [ -d $JBOSS_HOME ]
	then
		export CMD=" cat \"$JBOSS_HOME/bin/standalone.conf\"| sed -e 's/MaxPermSize=256m/MaxPermSize=1024m/' | sed -e 's/Xmx512m/Xmx1024m/' > result; mv result $JBOSS_HOME/bin/standalone.conf"
		echo $CMD
		cat "$JBOSS_HOME/bin/standalone.conf"| sed -e 's/MaxPermSize=256m/MaxPermSize=1024m/'| sed -e 's/Xmx512m/Xmx1024m/' > result; mv result "$JBOSS_HOME/bin/standalone.conf"
		#setting srv as default servlet
		##
		mkdir -p $JBOSS_HOME/modules/system/layers/base/com/mysql/driver/main
		wget http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.9/mysql-connector-java-5.1.9.jar
		mv mysql-connector-java-5.1.9.jar $JBOSS_HOME/modules/system/layers/base/com/mysql/driver/main/

		echo "PWD:$(pwd)"	
		cp $BASE/conf/standalone-with-dbs/standalone.xml $JBOSS_HOME/standalone/configuration/
		cp $BASE/conf/standalone-with-dbs/server.keystore $JBOSS_HOME/standalone/configuration/
		cp $BASE/conf/standalone-with-dbs/module.xml $JBOSS_HOME/modules/system/layers/base/com/mysql/driver/main/ 
	fi
}


compile_fhir_cell(){
	cd $BASE
	export DEPLOY_DIR="$JBOSS_HOME/standalone/deployments/"
	echo "Compiling and deploying war"
	echo "PWD:$(pwd)"
	export PATH="$PATH:$MAVEN_HOME/bin:$JAVA_HOME/bin"
	mvn clean install -Dmaven.test.skip=true; 
	cd dstu21 ;
	echo "PWD:$(pwd)"
	mvn install:install-file -DartifactId=validator -DgroupId=org.hl7.fhir.tools -Dfile=core/src/main/resources/org.hl7.fhir.validator.jar -Dversion=1.0 -Dpackaging=jar
	mvn clean install -Dmaven.test.skip=true; 


	#deploy
	cp srv/target/*.war $DEPLOY_DIR
}

check_homes_for_install
install_maven
#configure_wildfly
compile_fhir_cell

cd $JBOSS_HOME/
sed -i "s/localhost:3306/fhir-mariadb:3306/" standalone/configuration/standalone.xml
tar -cvjf wildfly-fhir.tar.bz2 standalone/configuration* standalone/deployments* modules/system/layers/base/com/mysql/*  --exclude=*i2b2*
#tar -cvjf standalone.tar.bz2 configuration* deployments* --exclude=*i2b2*

DAP=$LOCAL/docker/fhir-wildfly
[ -d $DAP ] || mkdir -p $DAP
cp wildfly-fhir.tar.bz2 $DAP
cp $BASE/conf/docker/fhir-wildfly/Dockerfile $DAP/
 

