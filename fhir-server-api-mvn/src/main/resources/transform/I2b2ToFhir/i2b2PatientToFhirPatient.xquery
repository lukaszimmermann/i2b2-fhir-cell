declare namespace  ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";
declare namespace map="http://www.w3.org/2005/xpath-functions/map";


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
else "UNK"
};

for $p in //ns2:patient_set/patient
let $id:=$p/patient_id/text()
let $zip:=$p/param[(@column='zip_cd')]/text()
let $gender:=$p/param[(@column='sex_cd')]/text()
let $gender_expanded:=if ($gender='M') then 'Male' else 'Female'
let $marital_status_raw:=$p/param[(@column='marital_status_cd')]/text()
let $marital_status:=local:fnrace(fn:lower-case($marital_status_raw))
let $race_code:=local:fnrace(fn:lower-case($p/param[(@column='race')]/text()))
let $birthdate:=$p/param[(@column='birth_date')]/text()
return
<Patient xmlns="http://hl7.org/fhir">
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
            <td>Marital Status</td>
            <td>{$marital_status_raw}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </text>

  <!--   MRN assigned by ACME healthcare on 6-May 2001   -->
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

  
  <!--   use FHIR code system for male / female   -->
  <gender>
    <coding>
      <system value="http://hl7.org/fhir/v3/AdministrativeGender"/>
      <code value="{$gender}"/>
      <display value="{$gender_expanded}"/>
    </coding>
  </gender>
  <birthDate value="{$birthdate}"/>
  <deceasedBoolean value="false"/>
  
  <extension url="http://hl7.org/fhir/StructureDefinition/us-core-race">
    <valueCodeableConcept>
      <coding>
        <system value="http://hl7.org/fhir/v3/Race"/>
        <code value="{$race_code}"/>
        
      </coding>
    </valueCodeableConcept>
  </extension>
  <address>
    <use value="home"/>
    <zip value="${zip}"/>
  </address>

  <managingOrganization>
    <reference value="Organization/1"/>
  </managingOrganization>
  
   <maritalStatus value="{$marital_status}"/>
   
  <active value="true"/>

</Patient>