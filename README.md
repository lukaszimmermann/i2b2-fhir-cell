The FHIR cell serves data from i2b2 instance, in FHIR format on per patient basis. 


==================================
Installation 

The best way to install is by using docker (see Wiki for docker)

Alternatively, the following steps are for installation from the source code:

(on amazon web instance. ssh as ec2-user and run the following)

wget https://raw.githubusercontent.com/i2b2plugins/i2b2-fhir-cell/master/i2b2-fhir/first_run.sh

sudo sh first_run.sh

===================================

Test Installation

The installation will start FHIR server at endpoint: http://IP_ADDRESS/srv-dstu21-0.3/api/open/

see demo at : http://IP_ADDRESS/srv-dstu21-0.3/demo/

and http://IP_ADDRESS/srv-dstu21-0.3/api/open/Patient

===================================

