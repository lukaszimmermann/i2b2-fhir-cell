FROM jboss/wildfly:10.0.0.Final as build
LABEL maintainer="luk.zim91@gmail.com"

###############################################################################
# Installation has to be performed as root
###############################################################################
USER root

###############################################################################
# Copy all the sources into the container, for building
###############################################################################
COPY . /tmp/fhir-cell

###############################################################################
# Install Maven as build dependency
###############################################################################
RUN yum install -y maven

###############################################################################
# Use Maven to deploy the web archive
###############################################################################
WORKDIR /tmp/fhir-cell
RUN mvn clean install
WORKDIR /tmp/fhir-cell/dstu21
RUN mvn install:install-file \
      -DartifactId=validator \
      -DgroupId=org.hl7.fhir.tools \
      -Dfile=core/src/main/resources/org.hl7.fhir.validator.jar \
      -Dversion=1.0 \
      -Dpackaging=jar && \
    mvn clean install -Dmaven.test.skip=true && sync && \
    mkdir -p /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war && \
    mv /tmp/fhir-cell/dstu21/srv/target/srv-dstu21-0.3.war \
       /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war/srv-dstu21-0.3.zip && \
    touch /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war.dodeploy && sync
WORKDIR /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war
RUN unzip srv-dstu21-0.3.zip && rm srv-dstu21-0.3.zip && sync

############################################################################
FROM jboss/wildfly:10.0.0.Final as production
COPY --from=build /opt /opt
COPY entrypoint.sh /opt/entrypoint.sh
USER root
RUN chmod +x /opt/entrypoint.sh && chown -R jboss:jboss /opt && sync
USER jboss

ENTRYPOINT [ "/opt/entrypoint.sh" ]
