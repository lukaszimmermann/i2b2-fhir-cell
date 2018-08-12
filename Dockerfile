FROM jboss/wildfly:10.0.0.Final as build1
LABEL maintainer="luk.zim91@gmail.com"

###############################################################################
# Installation has to be performed as root
###############################################################################
USER root

###############################################################################
# Install Maven as build dependency
###############################################################################
RUN yum install -y maven

###############################################################################
# Copy all the sources into the container, for building
###############################################################################
COPY . /tmp/fhir-cell

###############################################################################
# Create the war archive in the  /tmp dir
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
    mvn clean install -Dmaven.test.skip=true && sync

############################################################################
FROM jboss/wildfly:10.0.0.Final as build2
USER root
COPY --from=build1 /tmp /tmp

###############################################################################
# Create deployment
###############################################################################
RUN mkdir -p /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war && \
    mv /tmp/fhir-cell/dstu21/srv/target/srv-dstu21-0.3.war \
       /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war/srv-dstu21-0.3.zip && \
    touch /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war.dodeploy && \
    cd /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war && \
    unzip srv-dstu21-0.3.zip && rm srv-dstu21-0.3.zip && sync \
    rm /opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war/WEB-INF/faces-config.xml && sync

############################################################################
FROM jboss/wildfly:10.0.0.Final as production
COPY --from=build /opt /opt
COPY entrypoint.sh /opt/entrypoint.sh
USER root
RUN chmod +x /opt/entrypoint.sh && chown -R jboss:jboss /opt && sync
USER jboss

ENTRYPOINT [ "/opt/entrypoint.sh" ]
