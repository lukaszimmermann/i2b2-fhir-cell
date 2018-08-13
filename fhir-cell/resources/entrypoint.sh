#!/usr/bin/env bash
set -e

##############################################################################
# Setup the environment
##############################################################################
APP_CONF=/opt/jboss/wildfly/standalone/deployments/srv-dstu21-0.3.war/WEB-INF/classes/application.properties



##############################################################################
# Startup of Hive fails if the host of the Hive database has not been set
##############################################################################
if [ -z ${HIVE_HOST+x} ]; then

  cat << EOF
  #############################################################################
  FATAL ERROR: Variable HIVE_HOST is unset. This is not supported.
               The value should be somehing like: hive:9090
  #############################################################################
EOF
exit 1
fi

##############################################################################
# Startup of Hive fails if the host of the MariaDB is not set
##############################################################################
if [ -z ${MARIADB_HOST+x} ]; then

  cat << EOF
  #############################################################################
  FATAL ERROR: Variable MARIADB_HOST is unset. This is not supported.
               The value should be something like: mariadb:3306
  #############################################################################
EOF
exit 1
fi

if [ ! -f "${APP_CONF}" ]; then

  cat << EOF
  #############################################################################
  FATAL ERROR: The application file ${APP_CONF} does not exist!
  #############################################################################
EOF
exit 2
fi


# Setup the application conf
cat >"${APP_CONF}" <<EOL

i2b2Url=http://${HIVE_HOST}/i2b2
i2b2Domain=i2b2demo
openAccess=true
openAccessToken=1f4ffead29414d1977fba44e2bf4d8b7
openI2b2User=demo
openI2b2Password=demouser
openI2b2Project=Demo
openClientId=my_web_app
demoPatientId=1000000005
demoConfidentialClientId=webclient
demoConfidentialClientSecret=1b6sg3bs72bs73bd73h3bs8ok8fb3bbftd7
maxQueryThreads=2
patientBundleTimeOut=300
enrichEnabled=true
createDiagnosticReportsFromObservations=true
nonExpiringTokenList=2f4ffead29414d1977fba44e2bf4d8b7|3fhftfead29414d1977fba44e2bf4d8b7
fhirbaseSSL=false
patientFetchMin=false

ontologyType=default
#atleast one resourceCategoriesList is required
resourceCategoriesList=medications-labs-diagnoses
#-reports
medicationsPath=\\\\i2b2_MEDS\\i2b2\\Medications\\
labsPath=\\\\i2b2_LABS\\i2b2\\Labtests\\
diagnosesPath=\\\\i2b2_DIAG\\i2b2\\Diagnoses\\
reportsPath=\\\\i2b2_REP\\i2b2\\Reports\\

#PCORI PATHS
#ontologyType=pcori
#resourceCategoriesList=labs
#labs-diagnoses-medications
#labsPath=\\\\PCORI_LAB\\PCORI\\LAB_RESULT_CM\\LAB_NAME\\
#diagnosesPath=\\\\PCORI_DIAG\\PCORI\\DIAGNOSIS\\09\\
#medicationsPath=\\\\PCORI_MED\\PCORI\\MEDICATION\\RXNORM_CUI\\
#vitalsPath=\\\\PCORI_VITAL\\PCORI\\VITAL\\

#openI2b2User=pcori
#openI2b2Project=pcori
EOL

##############################################################################
# Wait for MariaDB to come up
##############################################################################
/opt/bin/dockerize -wait "tcp://${MARIADB_HOST}"


##############################################################################
# Start the Wildfly standalone server
##############################################################################
exec /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0
