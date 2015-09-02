export INSTALL_DIR=i2b2-fhir-cell
mkdir $INSTALL_DIR
cd $INSTALL_DIR

export IPADD=$(ifconfig eth0 | grep inet | awk '{print $2}' | sed 's/addr://'|head -n 1)

export WILDFLY_DIR="$(pwd -P)/wildfly-8.2.0.Final"
export DEPLOY_DIR="$WILDFLY_DIR/standalone/deployments/"

export JAVA_HOME="$(pwd)/jdk1.7.0_79"
echo "DEPLOY_DIR:$DEPLOY_DIR"

#export PATH="$PATH;$MAVEN_HOME/bin;$JAVA_HOME/bin"
#echo "PATH:$PATH"

export MVN="$MAVEN_HOME/bin/mvn"
echo "MVN:$MVN" 
export java="$JAVA_HOME/bin/java"
echo "java:$java" 

if [ "1" -eq "1" ]; then echo "hi"

#check if java is installed

$(java 2>/dev/null );if [ $? == 0 ];  then echo "Java was not found. Please install java 1.7 or higher"; fi
JAVA_VER=$(java -version 2>&1 | sed 's/java version "\(.*\)\.\(.*\)\..*"/\1\2/; 1q');
echo "java version:$JAVA_VER" 

if [ $JAVA_VER -lt 17 ]; then echo " Java 1.7 or higher is required" ;fi
wget --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/7u79-b15/jdk-7u79-linux-x64.tar.gz
tar -xzf jdk-7u79-linux-x64.tar.gz


#check if maven is installed
$(mvn 1>/dev/null );
#echo $?
if [ $? == 0 ];  then echo "mvn was not found. Installing maven"; 

wget http://apache.mirrors.ionfish.org/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
tar -xzf apache-maven-3.3.3-bin.tar.gz

export MAVEN_HOME="$(pwd)/apache-maven-3.3.3"
echo "MAVEN_HOME=$MAVEN_HOME"

#$MVN -version 1>/dev/null  
#echo $?
#if [ $? -ne 0 ];  then echo "mvn was not found. Please install maven  or higher"; fi

fi

wget http://download.jboss.org/wildfly/8.2.0.Final/wildfly-8.2.0.Final.tar.gz
tar -xzf wildfly-8.2.0.Final.tar.gz


echo "Installing source code from githib repository"

wget https://github.com/waghsk/i2b2-fhir/archive/master.zip 
unzip master.zip
#git clone https://github.com/waghsk/i2b2-fhir.git

fi
alias mvn=$MVN

echo "Compiling and deploying war"

cd i2b2-fhir-master/i2b2-fhir/;
mvn clean install -Dmaven.test.skip=true; 
cd dstu2 ;
mvn clean install -Dmaven.test.skip=true; 

#deploy
cp srv-2/target/*.war $DEPLOY_DIR

echo "running server on port 8080"

export RUN_WF="$WILDFLY_DIR/bin/standalone.sh" #-b=$IPADD"
sh $RUN_WF
