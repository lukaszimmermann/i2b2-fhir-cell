FROM jboss/wildfly:10.0.0.Final as build
LABEL maintainer="luk.zim91@gmail.com"

###############################################################################
# Installation has to be performed as root
###############################################################################
USER root

###############################################################################
# Install Maven and wget as build dependency
###############################################################################
RUN yum install -y maven wget

###############################################################################
# Copy all the sources into the container, for building
###############################################################################
COPY . /tmp/fhir-cell

###############################################################################
# Create the war archive in the  /tmp dir
###############################################################################
WORKDIR /tmp/fhir-cell/library
RUN mvn clean install
WORKDIR /tmp/fhir-cell/dstu21
RUN mvn install:install-file \
      -DartifactId=validator \
      -DgroupId=org.hl7.fhir.tools \
      -Dfile=core/src/main/resources/org.hl7.fhir.validator.jar \
      -Dversion=1.0 \
      -Dpackaging=jar && \
    mvn clean install -Dmaven.test.skip=true && sync

###############################################################################
# Create deployment and download mysql driver
###############################################################################
RUN mkdir -p /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war && \
    mv /tmp/fhir-cell/dstu21/srv/target/srv-dstu21-0.3.war \
       /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war/srv-dstu21-0.3.zip && \
    touch /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war.dodeploy && \
    cd /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war && \
    unzip srv-dstu21-0.3.zip && rm srv-dstu21-0.3.zip && sync && \
    rm /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war/WEB-INF/faces-config.xml && sync
WORKDIR /opt/jboss/wildfly/standalone/deployments
RUN wget -q http://central.maven.org/maven2/mysql/mysql-connector-java/8.0.12/mysql-connector-java-8.0.12.jar

###############################################################################
# Install standalone.xml, persistence.xml and keystore
###############################################################################
RUN cp /tmp/fhir-cell/resources/standalone.xml  /opt/jboss/wildfly/standalone/configuration/standalone.xml && \
    cp /tmp/fhir-cell/resources/server.keystore /opt/jboss/wildfly/standalone/configuration/server.keystore && \
    sync

###############################################################################
# Install dockerize, such that the FHIR cell can wait for the other services
# to come up
###############################################################################
WORKDIR /opt/bin
RUN wget -q https://github.com/jwilder/dockerize/releases/download/v0.6.1/dockerize-linux-amd64-v0.6.1.tar.gz && \
    tar xf dockerize-linux-amd64-v0.6.1.tar.gz && \
    rm dockerize-linux-amd64-v0.6.1.tar.gz && \
    chmod +x /opt/bin/dockerize && sync

#############################################################################
#############################################################################
FROM jboss/wildfly:10.0.0.Final as production
COPY --from=build /opt /opt
COPY resources/entrypoint.sh /opt/entrypoint.sh
USER root
RUN chmod +x /opt/entrypoint.sh && chown -R jboss:jboss /opt && sync
USER jboss

ENTRYPOINT [ "/opt/entrypoint.sh" ]
