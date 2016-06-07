#!/bin/sh
sudo docker exec -it fhir-wildfly /opt/jboss/wildfly/bin/jboss-cli.sh --controller=localhost:11000 -c '/subsystem=naming/binding=java\:global\/i2b2Url:add(binding-type=simple, type=java.lang.String, value="$1")' -u=admin -p=demoadmin

