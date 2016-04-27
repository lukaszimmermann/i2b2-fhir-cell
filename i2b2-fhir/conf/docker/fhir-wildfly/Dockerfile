FROM jboss/wildfly:10.0.0.Final
MAINTAINER <Kavishwar (Kavi)  Wagholikar waghsk@gmail.com>

#ADD srv-dstu21-0.3.war  /opt/jboss/wildfly/standalone/deployments/
ADD wildfly-fhir.tar.bz2 /opt/jboss/wildfly/

#ADD jbh/standalone/deploy.tar.bz2  /opt/jboss/wildfly/standalone/

COPY confI2b2Url.sh /
#RUN chmod 777 /confI2b2Url.sh

RUN /opt/jboss/wildfly/bin/add-user.sh admin demoadmin --silent

# Set the JBOSS_HOME env variable
ENV JBOSS_HOME /opt/jboss/wildfly



# Expose the ports we're interested in
EXPOSE 9090 11000

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
