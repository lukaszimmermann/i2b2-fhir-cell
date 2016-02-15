declare namespace  ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";
declare namespace map="http://www.w3.org/2005/xpath-functions/map";


declare function local:fnGetDate($r as xs:string?) as xs:string{ 
let $x :=substring-before($r,'T')
return $x
};
declare function local:fnI2b2TimeToFhirTime($r as xs:string?) as xs:string{ 
let $x :=fn:replace($r,'.000Z$','') 
let $y:=
if(fn:contains($x,'-')) then $x
else if(fn:contains($x,'+')) then $x
else fn:concat($x,'05:00')
return $y
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

declare function local:fnMaritalStatusDisplay($r as xs:string?) as xs:string
{ 
if ($r="T")  then  "Domestic partner"
(:XXX mapped to domestic partner) :)
else if ($r="D")  then  "Divorced"
else if ($r="M")  then  "Married"
else if ($r="UNK")  then  "unknown"
else if ($r="L")  then  "Legally seperated"
else if ($r="W") then "Widowed"
else if ($r="S") then "Never married"
else if ($r="A") then "Annulled"
else if ($r="I") then "Interlocutory"
else if ($r="P") then "Polygamous"
else "unknown"
};

declare function local:fnMetaData($last_updated as xs:string? ) as node(){
 <meta>
      <versionId value="1"/>
      <lastUpdated value="{$last_updated}"/>
 </meta>
};

declare function local:fnTxt($label as xs:string,$x as xs:string?){
      
         <tr>
            <td>{$label}</td>
            <td>{$x}</td>
          </tr>
};




declare function local:fnPatient($zip as xs:string?,
                                 $id as xs:string?,
                                  $gender as xs:string?,
                                 $gender_expanded as xs:string?,
                                 $birthdate as xs:string?,
                                 $marital_status as xs:string?,
                                 $marital_status_raw as xs:string?,
                                 $race_code as xs:string?,
                                 $updateDate as xs:string?
) as node()?{
let $birthdateDate:=local:fnGetDate($birthdate)
let $maritalStatusDisplay:=local:fnMaritalStatusDisplay($marital_status)
return 
<Patient  namespace="http://hl7.org/fhir"  >
  <id value="{$id}"/>
  {local:fnMetaData($updateDate)}
  <text>
    <status value="generated"/>
    <div xmlns="http://www.w3.org/1999/xhtml">
      <table>
        <tbody>
          <tr>
            <td>Id</td>
            <td>{$id}</td>
          </tr>
          {local:fnTxt('Zip',$zip)}
          {local:fnTxt('Gender',$gender_expanded)}
          {local:fnTxt('BirthDate',$birthdate)}
          {local:fnTxt('Marital Status',$maritalStatusDisplay)}
   
        </tbody>
      </table>
    </div>
  </text>
  
  
  <name>
    <use value="anonymous"/>
    <family value="anonymous"/>
    <given value="anonymous"/>
  </name>
  
<!--  {local:getIdentifier()}-->
 
<identifier>
    <use value="usual"/>
    <type>
      <coding>
        <system value="http://hl7.org/fhir/v2/0203"/>
        <code value="MR"/>
      </coding>
    </type>
    <system value="http://fhir.i2b2.org"/>
    <value value="{$id}"/>
    <assigner>
      <display value="i2b2"/>
    </assigner>
  </identifier>
 
  <!--   use FHIR code system for male / female   -->
  <gender value="{$gender_expanded}"/>
  <birthDate value="{$birthdateDate}">
    <extension url="http://hl7.org/fhir/StructureDefinition/patient-birthTime">
      <valueDateTime value="{$birthdate}"/>
    </extension>
  </birthDate>
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
  
  <extension url="http://hl7.org/fhir/StructureDefinition/us-core-ethnicity">
    <valueCodeableConcept>
      <coding>
        <system value="http://hl7.org/fhir/v3/Ethnicity"/>
        <code value="UNK"/>
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
      <display value="{$maritalStatusDisplay}"/>
    </coding>
   </maritalStatus>
   
   
  <active value="true"/>

</Patient>
};

let $I:=root()(:doc('/Users/kbw19/git/i2b2-fhir/i2b2-fhir/dstu21/xquery/src/main/resources/example/i2b2/AllPatients2.xml'):)

let $O:=
for $p in  $I//ns2:patient_set/patient
let $id:=$p/patient_id/text()
let $zip:=$p/param[(@column='zip_cd')]/text()
let $gender:=$p/param[(@column='sex_cd')]/text()
let $gender_expanded:=if ($gender='M') then 'male' else 'female'
let $marital_status_raw:=$p/param[(@column='marital_status_cd')]/text()
let $marital_status:=local:fnMaritalStatus(fn:lower-case($marital_status_raw))
let $race_code:=local:fnrace(fn:lower-case($p/param[(@column='race')]/text()))
let $birthdate:=local:fnI2b2TimeToFhirTime($p/param[(@column='birth_date')]/text())
let $updateDate := local:fnI2b2TimeToFhirTime($p/@update_date)

return 
<entry  xmlns="http://hl7.org/fhir">
    <resource>
{local:fnPatient($zip, $id,$gender,$gender_expanded,$birthdate,$marital_status,$marital_status_raw,$race_code,$updateDate )}
    </resource>
</entry>

return <Bundle xmlns:ns2="http://www.w3.org/1999/xhtml" xmlns="http://hl7.org/fhir">
    {$O}
    </Bundle>

