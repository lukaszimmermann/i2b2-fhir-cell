#!/bin/sh
/opt/jboss/wildfly/bin/jboss-cli.sh --controller=fhir-wildfly:11000 -c '/subsystem=naming/binding=java\:global\/i2b2Url:add(binding-type=simple, type=java.lang.String, value="$1")' 
