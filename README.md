The FHIR cell serves data from i2b2 instance, in FHIR format on per patient basis. 


==================================
Installation 

(on amazon web instance. ssh as ec2-user and run the following)

wget https://raw.githubusercontent.com/i2b2plugins/cell-i2b2-fhir/master/i2b2-fhir/first_run.sh

sudo sh first_run.sh

===================================

Test Installation

The installation will start FHIR server at endpoint: http://IP_ADDRESS/srv-dstu21-0.3/api/open/

see demo at : http://IP_ADDRESS/srv-dstu21-0.3/demo/

and http://IP_ADDRESS/srv-dstu21-0.3/api/open/Patient

===================================

