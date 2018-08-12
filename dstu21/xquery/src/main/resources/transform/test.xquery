declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";
let $A:=doc("/Users/kbw19/tmp/new_git/res/i2b2-fhir/xquery/src/main/resources/example/i2b2/AllPatients.xml")
return $A//ns2:patient_set/patient/param[(@column='marital_status_cd')]/text()