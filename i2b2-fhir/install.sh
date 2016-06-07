I2B2_QS=$1
source $I2B2_QS/install.sh


MVN_FILE=apache-maven-3.3.3-bin.tar.gz 

#check if the home directories are found as specified by user, or use default dirs
[ -d $MVN_HOME ] || MVN_HOME=$LOCAL/${MVN_FILE/-bin\.tar\.gz/}
alias mvn="$MAVEN_HOME/bin/mvn"

install_maven(){
	cd $LOCAL
        if [ -f $MVN_FILE ] then
		echo "found Maven file"
	else
                wget http://apache.mirrors.ionfish.org/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
                tar -xzf $MVN_FILE
        fi
}


configure_wildfly(){
	export JBOSS_HOME="$(pwd -P)/wildfly-9.0.1.Final"
	export DEPLOY_DIR="$JBOSS_HOME/standalone/deployments/"

	if [ -d $JBOSS_HOME ]
	then echo ""
	else
		export CMD=" cat \"$JBOSS_HOME/bin/standalone.conf\"| sed -e 's/MaxPermSize=256m/MaxPermSize=1024m/' | sed -e 's/Xmx512m/Xmx1024m/' > result; mv result $JBOSS_HOME/bin/standalone.conf"
		echo $CMD
		cat "$JBOSS_HOME/bin/standalone.conf"| sed -e 's/MaxPermSize=256m/MaxPermSize=1024m/'| sed -e 's/Xmx512m/Xmx1024m/' > result; mv result "$JBOSS_HOME/bin/standalone.conf"
		#setting srv as default servlet
		##
		mkdir -p $JBOSS_HOME/modules/system/layers/base/com/mysql/driver/main
		wget http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.9/mysql-connector-java-5.1.9.jar
		mv mysql-connector-java-5.1.9.jar $JBOSS_HOME/modules/system/layers/base/com/mysql/driver/main/
	
		cp i2b2-fhir/install/standalone-with-dbs/standalone.xml $JBOSS_HOME/standalone/configuration/
		cp i2b2-fhir/install/standalone-with-dbs/server.keystore $JBOSS_HOME/standalone/configuration/
		cp i2b2-fhir/install/standalone-with-dbs/module.xml $JBOSS_HOME/modules/system/layers/base/com/mysql/driver/main/ 
	fi
}


compile_fhir_cell(){

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
}

check_homes_for_install()

