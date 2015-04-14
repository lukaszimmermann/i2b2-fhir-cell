(:declare variable $doc as document-node(element(*, xs:untyped)) external;
 :)
 (: Distinct observations for one patient:)
declare function local:fnDose($r as xs:string?) as node()?
{ 
let $r:= fn:lower-case($r)
let $f:=
if($r="qds") then "4"
else if($r="tid") then "3"
else if($r="bds") then "2"
else if($r="qd") then "1"
else if($r="qhs") then "1" (: every night at bed time:)
else "UNK"

let $c:= 
<timing>
   <schedule>
     <repeat>
       <frequency value="{$f}" />
       <duration value="1" />
       <units value="d" />
     </repeat>
  </schedule>
</timing>
return $c
};

declare function local:fnRoute($r as xs:string?) as node()?
{ 
let $r:= fn:lower-case($r)

let $c:=
if($r="PO") then ""
else if($r="tid") then "394899003"
else "UNK"

let $d:=
if($r="PO") then ""
else if($r="tid") then "oral administration of treatment"
else "UNK"

return
   <dosage>
<route>
   <coding>
      <system value="http://snomed.info/sct"/>
      <code value="{$c}"/>
      <display value="{$d}"/>
   </coding>  
 </route>
    </dosage>

};

declare function local:fnFhirDosage($d as node()?, $q as node()?) as node()?
{ 
   <dosage>
   {$d}
   {$q}
    </dosage>

};

declare function local:fnFhirMedication($count as xs:integer?,$cn as xs:string?, $cid as xs:string? ) as node(){
<Medication id="Medication/{$count}"
  xmlns:ns2="http://www.w3.org/1999/xhtml" 
    xmlns="http://hl7.org/fhir">
    
	<text>
        <status value="generated"/>
        <ns2:div>{$cn}</ns2:div>
    </text>
  <name value="{$cn}"/>
  <code>
    <coding>
      <system value="http://../NDC"/>
      <code value="{$cid}"/>
      <display value="{$cn}"/>
    </coding>
  </code>

  </Medication>
  
};

declare function local:fnFhirResourceMetaData($id as xs:string?, $last_updated as xs:string? ) as node(){
<FhirResourceMetaData>
    <id>{$id}</id>
    <lastUpdated>$last_updated</lastUpdated>
</FhirResourceMetaData>
};

declare function local:fnFhirMedicationStatement($count as xs:integer?, $route as xs:string? ,$medicationNode as node()?,
        $sd as xs:string, $ed as xs:string) as node(){
<MedicationStatement xmlns="http://hl7.org/fhir"
id="MedicationStatement/{$count}">
  <text>
    <status value="generated"/>
    <div xmlns="http://www.w3.org/1999/xhtml">
      <p>Penicillin VK 10ml suspension administered by oral route at 14:30 on 1 June 2012</p>
      <p>to patient ref: a23</p>
    </div>
  </text> 
  <!--      -->
  <patient>
    <reference value="Patient/example"/>
  </patient>
  <whenGiven>
    <start value="{$sd}"/>
    <end value="{$ed}"/>
  </whenGiven>
  <medication>
    <reference value="{$medicationNode/id}"/> 
  </medication>
    {local:fnRoute($route)}
</MedicationStatement>
};

let $PID := 1000000001

let $distobs :=
 (:for $t in $doc//observation:)
 for $t in //observation
 let $eid := $t/event_id/text()
 let $pid := $t/patient_id/text()
 let $cid := $t/concept_cd/text()
 let $cn := $t/concept_cd/@name
 let $sourceSystem := $t/@sourcesystem_cd
 let $importDate := $t/@import_date
 let $downloadDate := $t/@download_date
 let $updateDate := $t/@update_date
 let $oid := $t/observer_cd/text()
 let $sd := $t/start_date/text()
 let $ed := $t/end_date/text()
 let $m := $t/modifier_cd/text()
 let $val_cd := $t/valuetype_cd/text()
 let $tval_char := $t/tval_char/text()
 
  let $id := concat($pid,"-",$eid,"-",$cid,"-",$sd)

 return
       if ($t is (//observation)[. = $t and patient_id/text() = $PID ][1]
        
       ) then
            <observation sourcesystem_cd="{$sourceSystem}" import_date="{$importDate}" download_date="{$downloadDate}" update_date="{$updateDate}">
                        <id>{$id}</id>
                        <event_id source="HIVE">{$eid}</event_id>
                        <patient_id source="HIVE">{$pid}</patient_id>
                        <concept_cd name="{$cn}">{$cid}</concept_cd>
                        {$oid}
                        <start_date>${sd}</start_date>
                        <modifier_cd>{$m}</modifier_cd>
                        <valuetype_cd>{$val_cd}</valuetype_cd>
                        <tval_char>{$tval_char}</tval_char>
                        <nval_num />
                        <valueflag_cd />
                        <end_date>{$ed}</end_date>
                        <location_cd name="">@</location_cd>
                    </observation>
     else ()

 


let $A := <set>{$distobs}</set>
for $id at $count in fn:distinct-values($A/observation/id)
let $refObs :=  $A/observation[id =$id and modifier_cd = "MED:FREQ"]

let $freqMod :=  $A/observation[id =$id and modifier_cd = "MED:FREQ"]
let $freq :=  $freqMod/tval_char/text()  
let $freqNode := local:fnDose($freq)

let $doseMod :=  $A/observation[id =$id and modifier_cd = "MED:DOSE"]
let $dose :=  $doseMod/tval_char/text()   (:XXX what does E mean:)

let $routeMod :=  $A/observation[id =$id and modifier_cd = "MED:ROUTE"]
let $route :=  $routeMod/tval_char/text()  
let $routeNode := local:fnRoute($route) (:XXX to add more routes:)

let $instrMod :=  $A/observation[id =$id and modifier_cd = "MED:INST"]
let $instr :=  $instrMod/tval_char/text()   

let $sourceSystem := $refObs/@sourcesystem_cd/string()
let $importDate := $refObs/@import_date/string()
let $downloadDate := $refObs/@download_date/string()
let $updateDate := $refObs/@update_date/string()

 
let $cid := $refObs/concept_cd/text() 
let $cn := $refObs/concept_cd/@name/string()
let $oid := $refObs/observer_cd
let $sd := $refObs/start_date/text()
let $ed := $refObs/end_date/text()

let $outputDosage:=local:fnFhirDosage($routeNode,$routeNode)

let $fhirMedication:=local:fnFhirMedication($count,$cn, $cid)
let $fhirMedicationStatement:=local:fnFhirMedicationStatement($count,"route",$fhirMedication,$sd,$ed)

return <p>{$fhirMedication}
{local:fnFhirResourceMetaData(xs:string($count),$updateDate)}
{$fhirMedicationStatement}</p>

(:

return <p>{$freq}{$freqNode}</p>

MED:FREQ MED:DOSE MED:ROUTE MED:INST MED:SIG
 <p>{$freq}{$freqNode}</p>
 
 <MedicationStatement xmlns="http://hl7.org/fhir">
  <text>
    <status value="generated"/>
    <div xmlns="http://www.w3.org/1999/xhtml">
      <p>Penicillin VK 10ml suspension administered by oral route at 14:30 on 1 June 2012</p>
      <p>to patient ref: a23</p>
    </div>
  </text> 
  <!--      -->
  <patient>
    <reference value="Patient/example"/>
  </patient>
  <whenGiven>
    <start value="2012-06-01T14:30:00"/>
    <end value="2012-06-01T14:30:00"/>
  </whenGiven>
  <medication>
    <reference value="Medication/example"/> 
  </medication>
  <dosage>
    <route>
      <coding>
        <system value="http://snomed.info/sct"/>
        <code value="394899003"/>
        <display value="oral administration of treatment"/>
      </coding>  
    </route>
    <quantity>
      <value value="10"/>
      <units value="ml"/>
      <system value="http://unitsofmeasure.org"/>
      <code value="ml"/>
    </quantity>
  </dosage>
</MedicationStatement>
 <MedicationStatement xmlns="http://hl7.org/fhir"> doco
 <!-- from Resource: extension, modifierExtension, language, text, and contained -->
 <identifier><!-- 0..* Identifier External Identifier --></identifier>
 <patient><!-- 0..1 Resource(Patient) Who was/is taking medication --></patient>
 <wasNotGiven value="[boolean]"/><!-- 0..1 True if medication is/was not being taken -->
 <reasonNotGiven><!-- ?? 0..* CodeableConcept True if asserting medication was not given --></reasonNotGiven>
 <whenGiven><!-- 0..1 Period Over what period was medication consumed? --></whenGiven>
 <medication><!-- 0..1 Resource(Medication) What medication was taken? --></medication>
 <device><!-- 0..* Resource(Device) E.g. infusion pump --></device>
 <dosage>  <!-- 0..* Details of how medication was taken -->
  <timing><!-- 0..1 Schedule When/how often was medication taken? --></timing>
  <asNeeded[x]><!-- 0..1 boolean|CodeableConcept Take "as needed" f(or x) --></asNeeded[x]>
  <site><!-- 0..1 CodeableConcept Where on body was medication administered? --></site>
  <route><!-- 0..1 CodeableConcept How did the medication enter the body? --></route>
  <method><!-- 0..1 CodeableConcept Technique used to administer medication --></method>
  <quantity><!-- 0..1 Quantity Amount administered in one dose --></quantity>
  <rate><!-- 0..1 Ratio Dose quantity per unit of time --></rate>
  <maxDosePerPeriod><!-- 0..1 Ratio 
      Maximum dose that was consumed per unit of time --></maxDosePerPeriod>
 </dosage>
</MedicationStatement>
 
<Medication id="Medication/{$count}"
  xmlns:ns2="http://www.w3.org/1999/xhtml" 
    xmlns="http://hl7.org/fhir">
    
	<text>
        <status value="generated"/>
        <ns2:div>{$cn}</ns2:div>
    </text>
  <name value="{$cn}"/>
  <code>
    <coding>
      <system value="http://../NDC"/>
      <code value="{$cid}"/>
      <display value="{$cn}"/>
    </coding>
  </code>
  
  </Medication>
  
 :)
