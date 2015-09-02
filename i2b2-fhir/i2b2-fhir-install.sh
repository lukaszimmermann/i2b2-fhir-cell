
export IPADD=$(ifconfig eth0 | grep inet | awk '{print $2}' | sed 's/addr://'|head -n 1)

#check if java is installed

$(java 2>/dev/null );if [ $? == 0 ];  then echo "Java was not found. Please install java 1.7 or higher"; fi
JAVA_VER=$(java -version 2>&1 | sed 's/java version "\(.*\)\.\(.*\)\..*"/\1\2/; 1q');
echo "java version:$JAVA_VER" 

if [ $JAVA_VER -lt 17 ]; then echo " Java 1.7 or higher is required" ;fi
wget --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/7u79-b15/jdk-7u79-linux-x64.tar.gz
tar -xvzf jdk-7u79-linux-x64.tar.gz



export JAVA_HOME="$(pwd)/jdk1.7.0_79"
export java="$JAVA_HOME/bin/java"

echo "java:$java"

#check if maven is installed
$(mvn 1>/dev/null );
#echo $?
if [ $? == 0 ];  then echo "mvn was not found. Please install maven  or higher"; fi

#wget http://apache.mirrors.ionfish.org/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
#tar -xvzf apache-maven-3.3.3-bin.tar.gz
export MAVEN_HOME="$(pwd)/apache-maven-3.3.3"

echo "MAVEN_HOME=$MAVEN_HOME"

#wget http://download.jboss.org/wildfly/9.0.1.Final/wildfly-9.0.1.Final.tar.gz
#tar -xvzf wildfly-9.0.1.Final.tar.gz
export WILDFLY_DIR="$(pwd)/wildfly-9.0.1.Final"
export DEPLOY_DIR="$WILDFLY_DIR/standalone/deployments/"

echo "DEPLOY_DIR:$DEPLOY_DIR"

export PATH="$PATH;$MAVEN_HOME/bin;$JAVA_HOME/bin"

echo "PATH:$PATH"

export MVN="$MAVEN_HOME/bin/mvn"

echo "MVN:$MVN" 
$MVN -version 1>/dev/null  
#echo $?
if [ $? -ne 0 ];  then echo "mvn was not found. Please install maven  or higher"; fi


git clone https://github.com/waghsk/i2b2-fhir.git

alias mvn=$MVN

cd i2b2-fhir/i2b2-fhir/;
mvn clean install -Dmaven.test.skip=true; 
cd dstu2 ;
mvn clean install -Dmaven.test.skip=true; 
cp srv-2/target/*.war $DEPLOY_DIR
"$WILDFLY_DIR/bin/standalone.sh -b=$IPADD"
