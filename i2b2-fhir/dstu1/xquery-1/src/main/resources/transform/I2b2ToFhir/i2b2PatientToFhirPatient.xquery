declare namespace  ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";
declare namespace map="http://www.w3.org/2005/xpath-functions/map";

declare function local:fnI2b2TimeToFhirTime($r as xs:string?) as xs:string{ 
fn:replace($r,'.000Z$','') 
};

declare function local:getIdentifier() as node(){ 

 <identifier>
    <use value="usual"/>
    <label value="MRN"/>
    <system value="urn:oid:1.2.36.146.595.217.0.1"/>
    <value value="12345"/>
    <period>
      <start value="2001-05-06"/>
    </period>
    <assigner>
      <display value="Acme Healthcare"/>
    </assigner>
  </identifier>

};

declare function local:fnrace($r as xs:string?) as xs:string
{ 
(:let $map := map:map()
let $key := map:put($map, "black", "B")
let $f:=map:get($map,$r)
return $f
:)
if ($r="aleutian")  then  "2009-9"
    else  if ($r="american indian") then "1004-1"
    else  if ($r="asian") then "2028-9"
    else  if ($r="asian pacific islander") then "2076-8"
    else  if ($r="eskimo") then "1840-8"
    else  if ($r="hispanic") then "UNK"
    (:XXX unlcear what race to map hispanics to 
    https://hl7-fhir.github.io/valueset-daf-race.html
    current unknown
    :)
    else  if ($r="indian") then "1004-1"
    else  if ($r="middle eastern") then "2118-8"
    else  if ($r="multiracial") then "UNK"
    else if ($r="native american") then "1004-1"
    else  if ($r="navajo") then "UNK"
    else  if ($r="not recorded") then "UNK"
    else  if ($r="oriental") then "UNK"
    else   if ($r="white") then "2106-3"
    else "UNK"
};

declare function local:fnMaritalStatus($r as xs:string?) as xs:string
{ 
if ($r="common law")  then  "T"
(:XXX mapped to domestic partner) :)
else if ($r="divorced")  then  "D"
else if ($r="married")  then  "M"
else if ($r="other")  then  "UNK"
else if ($r="partner")  then  "T"
else if ($r="seperated")  then  "L"
else if ($r="unknown")  then  "UNK" 
else if ($r="widowed") then "W"
else if ($r="single") then "S"
else "UNK"
};

declare function local:fnMetaData($class as xs:string,$id as xs:string?,$last_updated as xs:string? ) as node(){
<ns3:MetaData xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
    <ns3:id>{concat($class,'/',$id)}</ns3:id>
    <ns3:lastUpdated>{$last_updated}</ns3:lastUpdated>
</ns3:MetaData>
};

declare function local:fnPatient($zip as xs:string?,
                                 $id as xs:string?,
                                  $gender as xs:string?,
                                 $gender_expanded as xs:string?,
                                 $birthdate as xs:string?,
                                 $marital_status as xs:string?,
                                 $marital_status_raw as xs:string?,
                                 $race_code as xs:string?
) as node()?{
        <ns3:Resource xmlns:ns3="http://i2b2.harvard.edu/fhir/core" xsi:type="Patient" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://hl7.org/fhir">
  <identifier>
    <value value="Patient/{$id}"/>
  </identifier>
  <text>
    <status value="generated"/>
    <div xmlns="http://www.w3.org/1999/xhtml">
      <table>
        <tbody>
          <tr>
            <td>Zip</td>
            <td>{$zip}</td>
          </tr>
          <tr>
            <td>Id</td>
            <td>{$id}</td>
          </tr>
           <tr>
            <td>Gender</td>
            <td>{$gender_expanded}</td>
          </tr>
           <tr>
            <td>Birthdate</td>
            <td>{$birthdate}</td>
          </tr>
           <tr>
           <td>Marital Status</td>
            <td>{$marital_status_raw}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </text>

<!--  {$local:getIdentifier()}-->
  
  <!--   use FHIR code system for male / female   -->
  <gender>
    <coding>
      <system value="http://hl7.org/fhir/v3/AdministrativeGender"/>
      <code value="{$gender}"/>
      <display value="{$gender_expanded}"/>
      <primary value="true"/>
    </coding>
  </gender>
  <birthDate value="{$birthdate}"/>
  <deceasedBoolean value="false"/>
  
  <extension url="http://hl7.org/fhir/StructureDefinition/us-core-race">
    <valueCodeableConcept>
      <coding>
        <system value="http://hl7.org/fhir/v3/Race"/>
        <code value="{$race_code}"/>
        <primary value="true"/>
      </coding>
    </valueCodeableConcept>
  </extension>
  <address>
    <use value="home"/>
    <zip value="{$zip}"/>
  </address>

  <managingOrganization>
    <reference value="Organization/1"/>
  </managingOrganization>
  
  <maritalStatus>
    <coding>
      <system value="http://hl7.org/fhir/v3/MaritalStatus"/>
      <code value="{$marital_status}"/>
      <display value="{$marital_status_raw}"/>
    </coding>
   </maritalStatus>
   
   
  <active value="true"/>

</ns3:Resource>
};

 let $I:= root()(: doc('/Users/***REMOVED***/tmp/new_git/res/i2b2-fhir/dstu1/xquery-1/src/main/resources/example/i2b2/AllPatients.xml')
 :)
let $O:= 
for $p in $I//ns2:patient_set/patient
let $id:=$p/patient_id/text()
let $zip:=$p/param[(@column='zip_cd')]/text()
let $gender:=$p/param[(@column='sex_cd')]/text()
let $gender_expanded:=if ($gender='M') then 'Male' else 'Female'
let $marital_status_raw:=$p/param[(@column='marital_status_cd')]/text()
let $marital_status:=local:fnMaritalStatus(fn:lower-case($marital_status_raw))
let $race_code:=local:fnrace(fn:lower-case($p/param[(@column='race')]/text()))
let $birthdate:=$p/param[(@column='birth_date')]/text()
let $updateDate := local:fnI2b2TimeToFhirTime($p/@update_date)

return <set>
<ns3:MetaResource xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
{local:fnPatient($zip, $id,$gender,$gender_expanded,$birthdate,$marital_status,$marital_status_raw,$race_code )}
{local:fnMetaData('Patient',$id,$updateDate)}
</ns3:MetaResource>
</set>

return <ns3:MetaResourceSet xmlns:ns2="http://www.w3.org/1999/xhtml" xmlns="http://hl7.org/fhir" xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
    {$O/ns3:MetaResource}
</ns3:MetaResourceSet>
