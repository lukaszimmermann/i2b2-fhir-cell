xquery version "1.0";
declare namespace functx = "http://www.functx.com";
 
 
declare function local:fnI2b2TimeToFhirTime($r as xs:string?) as xs:string{ 
let $x :=fn:replace($r,'.000Z$','') 
return $x
};
 
declare function local:fnDoseFhir($dose as xs:string?,$unit as xs:string?) as node()?
{ 
<doseQuantity  xmlns="http://hl7.org/fhir">
      <value value="{$dose}"/>
      <units value="{$unit}"/>
      <system value="http://unitsofmeasure.org"/>
      <code value="{$unit}"/>
    </doseQuantity>
}; 
declare function local:fnTimingSchedule($freq as xs:string?) as node()?
{ 
let $freq:= fn:lower-case($freq)
let $o:=
if($freq="qds") then "4"
else if($freq="tid") then "3"
else if($freq="bds") then "2"
else if($freq="qd") then "1"
else if($freq="qhs") then "1" (: every night at bed time:)
else ""

let $c:= 
<timingSchedule  xmlns="http://hl7.org/fhir">
     <repeat>
       <frequency value="{$o}" />
       <duration value="1" />
       <units value="d" />
     </repeat>
</timingSchedule>
return $c
};

declare function local:fnRoute($r as xs:string?) as node()?
{ 
let $r:= fn:lower-case($r)
(:http://hl7.org/fhir/2015May/valueset-route-codes.html:)
let $c:=
if($r="PO") then "26643006"
else if($r="ID") then "372464004"
else if($r="IM") then "78421000"
else if($r="IO") then "417255000"
else if($r="IP") then "38239002" 
else if($r="IV") then "47625008"
else "UNK"

let $d:=
if($r="PO") then "Oral route"
else if($r="ID") then "Intradermal route"
else if($r="IM") then "Intramuscular"
else if($r="IO") then "Intraosseous"
else if($r="IP") then "Intraperitoneal" 
else if($r="IV") then "Intravenous"
else "UNK"

return
   
<route  xmlns="http://hl7.org/fhir">
   <coding>
      <system value="http://snomed.info/sct"/>
      <code value="{$c}"/>
      <display value="{$d}"/>
      <primary value="true"/>
   </coding>  
 </route>
   
};


declare function local:fnFhirMedication($count as xs:integer,$cn as xs:string, $cid as xs:string, $pid as xs:string) as node(){           
  <Medication xmlns="http://hl7.org/fhir"  xmlns:ns2="http://www.w3.org/1999/xhtml">
 <id value="{$pid}-{$count}"/>
   <text>
        <status value="generated"/>
        <ns2:div>{$cn}</ns2:div>
    </text>
  <code>
    <coding>
      <system value="http://../NDC"/>
      <code value="{$cid}"/>
      <display value="{$cn}"/>
      <primary value="true"/>
    </coding>
  </code>

  </Medication>
  
};


declare function local:fnFhirObservation( $sd as xs:string?, $ed as xs:string?,$count as xs:integer,$cn as xs:string, $cid as xs:string, $pid as xs:string, $valueFhir as node()?) as node(){     
  let $endDateString:=
    if($ed != "") then
    <end value="{$ed}"/>
  else ()

return
  <Observation xmlns="http://hl7.org/fhir"  xmlns:ns2="http://www.w3.org/1999/xhtml">
 <id value="{$pid}-{$count}"/>
    <text>
        <status value="generated"/>
        <ns2:div>{$cn}</ns2:div>
    </text>
  
    <code>  
    <coding>
      <system value="http://loinc.org"/>
      <code value="{$cid}"/>
      <display value="{$cn}"/>
      <primary value="true"/>
    </coding>
</code>
  
  <effectivePeriod>
    <start value="{$sd}"/>
    {$endDateString}
  </effectivePeriod>
  
    {$valueFhir}
  <!--   the mandatory quality flags:   -->
  <status value="final"/>
  <reliability value="ok"/>
  
   <subject>
     <reference value="Patient/{$pid}"/>
  </subject>

  </Observation>
  
};

declare function local:fnFhirValueQuantity($val as xs:string?,$unit as xs:string?) as node(){    
let $unitStr:=
       if($unit="") then ""
        else   <units value="{$unit}"/> 
 

let $codeStr:=
       if($unit="") then ""
        else    <code value="{$unit}"/>  
 return    

<valueQuantity>
    <value value="{$val}"/>    
   
    <system value="http://unitsofmeasure.org"/>
    
    {$unitStr}
    {$codeStr}
  </valueQuantity>
};

declare function local:fnFhirValueCodeableConcept($val as xs:string?) as node(){
<valueCodeableConcept>
    <coding>
      <system value="http://i2b2.org"/>
      <code value="{$val}"/>
      <display value="{$val}"/>
    </coding>
  </valueCodeableConcept>
};


declare function local:fnFhirDiagCondition($sd as xs:string?, $ed as xs:string?,$count as xs:integer, $cid as xs:string?, $pid as xs:string) as node(){           
  let $endDateString:=
    if($ed != "") then
    <end value="{$ed}"/>
  else ()
 
 return

<Condition xmlns="http://hl7.org/fhir"  xmlns:ns2="http://www.w3.org/1999/xhtml">
 <id value="{$pid}-{$count}"/>
   <status value="generated"/>
  <text>   
  </text>
  <patient>
     <reference value="Patient/{$pid}"/>
  </patient>
  
  <onsetPeriod>
    <start value="{$sd}"/>
    {$endDateString}
  </onsetPeriod>
  
  <code>
    <coding>
      <system value="http://hl7.org/fhir/sid/icd-9"/>
      <code value="{$cid}"/>
    </coding>/
  </code>
  <category>
    <coding>
      <system value="http://hl7.org/fhir/condition-category"/>
      <code value="diagnosis"/>
      <display value="Diagnosis"/>
    </coding>
  </category>
  <clinicalStatus value="active"/>
  
  </Condition>
};


declare function local:fnFhirDiagReport($sd as xs:string?, $ed as xs:string?,$count as xs:integer, $cid as xs:string?, $pid as xs:string,$textContent as xs:string) as node(){  

<DiagnosticReport xmlns="http://hl7.org/fhir">
  <id value="{$pid}-{$count}"/>
  <text><status value="generated"/><div xmlns="http://www.w3.org/1999/xhtml">{$textContent}</div> </text>
   
  <status value="final"/>
  <code>
    <coding>
      <system value="http://loinc.org"/>
      <code value="{$cid}"/>
    </coding>
  </code>
  <subject>
    <reference value="Patient/{$pid}"/>
   </subject>
  <effectiveDateTime value="{$sd}"/>
  <issued value="{$ed}"/>
  <performer>
    <reference value="Organization/TODO"/>
  </performer> 
</DiagnosticReport>
};

declare function local:fnMetaData($class as xs:string,$pid as xs:string?,$count as xs:string?,$last_updated as xs:string? ) as node(){
<ns3:MetaData xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
    <ns3:id>{concat($class,'/',$pid,"-",$count)}</ns3:id>
    <ns3:lastUpdated>{$last_updated}</ns3:lastUpdated>
</ns3:MetaData>
};

declare function local:fnFhirMedicationStatement($count as xs:integer?, $timingScheduleFhir as node()?, $routeFhir as node()?,$doseQuantityFhir as node()?, $medication_id as xs:string?,
        $sd as xs:string, $ed as xs:string, $pid as xs:string?,$instr as xs:string?) as node(){
 
  <MedicationStatement xmlns="http://hl7.org/fhir"  xmlns:ns2="http://www.w3.org/1999/xhtml">
 <id value="{$pid}-{$count}"/>
 
  
<!--<identifier value="MedicationStatement/{$pid}-{$count}"/>-->
   <dateWritten value="{$sd}"/>
   
   <text>
    <status value="generated"/>
    <div xmlns="http://www.w3.org/1999/xhtml">
      <p>{$instr}  </p>
     </div>
  </text>
  
  <!--<status value="active"/>-->
  
  <patient>
     <reference value="Patient/{$pid}"/>
  </patient>
 
 <!-- <prescriber>
    <reference value="Practitioner/example"/>
  </prescriber>
    <start value="{$sd}"/>
    <end value="{$ed}"/>
-->
  <medicationReference>
    <reference value="Medication/{$medication_id}"/> 
  </medicationReference>
    


  <dosageInstruction>
  {$timingScheduleFhir}
  {$routeFhir}
  {$doseQuantityFhir}
    </dosageInstruction>

  
</MedicationStatement>
 
};



declare function functx:is-node-in-sequence
  ( $node as node()? ,
    $seq as node()* )  as xs:boolean {

   some $nodeInSeq in $seq satisfies $nodeInSeq is $node
 } ;
 
declare function functx:distinct-nodes
  ( $nodes as node()* )  as node()* {

    for $seq in (1 to count($nodes))
    return $nodes[$seq][not(functx:is-node-in-sequence(
                                .,$nodes[position() < $seq]))]
 } ;
 
 (:to link modifier_cds that are for same concept_cd instance:)

declare function local:distinctObservations($I as node()?) as node ()?{

let $distobs := 
 (:for $t in $doc//observation:)
 
 for $t in $I//observation
 let $panelName:=$t/../@panel_name/string()
 let $eid := $t/event_id/text()
 let $pid := $t/patient_id/text()
 let $cid := $t/concept_cd/text()
 let $cn := $t/concept_cd/@name/string()
 let $sourceSystem := $t/@sourcesystem_cd
 let $importDate := local:fnI2b2TimeToFhirTime($t/@import_date)
 let $downloadDate := local:fnI2b2TimeToFhirTime($t/@download_date)
 let $updateDate := local:fnI2b2TimeToFhirTime($t/@update_date)
 let $oid := $t/observer_cd/text()
 let $sd := local:fnI2b2TimeToFhirTime($t/start_date/text())
 let $ed := local:fnI2b2TimeToFhirTime($t/end_date/text())
 let $m := $t/modifier_cd/text()
 let $val_cd := $t/valuetype_cd/text()
 let $tval_char := $t/tval_char/text()
  let $nval_num := $t/nval_num/text()
  let $units_cd:=$t/units_cd/text()
  let $instNum := $t/instance_num/text()
  let $loc := $t/location_cd/text()
  let $obs_blob:=$t/observation_blob/text()
 
  let $id := concat($pid,"-",$eid,"-",$cid,"-",$sd,"-",$oid,"-",$instNum) (:all in primary key except modifier_cd, but includes instNum:)

 return 
  
             <observation sourcesystem_cd="{$sourceSystem}" import_date="{$importDate}" download_date="{$downloadDate}" update_date="{$updateDate}">
                        <id>{$id}</id>
                        <panel_name>{$panelName}</panel_name>
                        <event_id source="HIVE">{$eid}</event_id>
                        <patient_id source="HIVE">{$pid}</patient_id>
                        <concept_cd name="{$cn}">{$cid}</concept_cd>
                        <observer>{$oid}</observer>
                        <start_date>{$sd}</start_date>
                        <modifier_cd>{$m}</modifier_cd>
                        <valuetype_cd>{$val_cd}</valuetype_cd>
                        <tval_char>{$tval_char}</tval_char>
                        <nval_num>{$nval_num}</nval_num>
                        <units_cd>{$units_cd}</units_cd>
                        <valueflag_cd />
                        <instance_num>{$instNum}</instance_num>
                        <end_date>{$ed}</end_date>
                        <location_cd>{$loc}</location_cd>
                        <observation_blob>{$obs_blob}</observation_blob>
                    </observation>
    return  <set>{functx:distinct-nodes($distobs)}</set>
 };
 
 

 
 
 
declare function local:processMedObs
  ( $A as node()* )  as node()*
{

let $O:=
for $id at $count in fn:distinct-values($A/observation/id)
let $refObs :=  $A/observation[id =$id and modifier_cd = "MED:FREQ"]

let $freqMod :=  $refObs
let $freq :=  $freqMod/tval_char/text()  

let $doseMod :=  $A/observation[id =$id and modifier_cd = "MED:DOSE"]
let $dose :=  $doseMod/nval_num/text()   (:XXX what does E mean:)
let $doseUnit :=  $doseMod/units_cd/text() 

let $routeMod :=  $A/observation[id =$id and modifier_cd = "MED:ROUTE"]
let $route :=  $routeMod/tval_char/text()  

let $instrMod :=  $A/observation[id =$id and modifier_cd = "MED:INST"]
let $instr :=  $instrMod/tval_char/text()   

let $sourceSystem := $refObs/@sourcesystem_cd/string()
let $importDate := $refObs/@import_date/string()
let $downloadDate := $refObs/@download_date/string()
let $updateDate := $refObs/@update_date/string()

let $pid := $refObs/patient_id/text()
let $cid := fn:replace($refObs/concept_cd/text(),"NDC:","")
let $cn := $refObs/concept_cd/@name/string()
let $oid := $refObs/observer_cd
let $sd := local:fnI2b2TimeToFhirTime($refObs/start_date/text())
let $ed := local:fnI2b2TimeToFhirTime($refObs/end_date/text())

let $fhirMedication:=local:fnFhirMedication($count,$cn, $cid,$pid)
let $medication_id:=concat("",$pid,"-",$count)
let $timingScheduleFhir:=local:fnTimingSchedule($freq)
let $routeFhir:=local:fnRoute($route)
let $doseQuantityFhir:=local:fnDoseFhir($dose,$doseUnit)


let $fhirMedicationStatement:=local:fnFhirMedicationStatement($count,$timingScheduleFhir,$routeFhir,$doseQuantityFhir,$medication_id,$sd,$ed,$pid,$instr)



return
 <set>
 <entry xmlns="http://hl7.org/fhir">
<resource xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
{$fhirMedication}
</resource>
</entry>
<entry xmlns="http://hl7.org/fhir">
<resource xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
{$fhirMedicationStatement}
</resource>
</entry>
</set>


return <Bundle xmlns="http://hl7.org/fhir" >
    {$O/entry}
</Bundle>

};


declare function local:processLabObs
  ( $A as node()* )  as node()*
{

let $O:=
for $id at $count in fn:distinct-values($A/observation/id)
let $refObs :=  $A/observation[id =$id]

let $pid := $refObs/patient_id/text()
let $cid := fn:replace($refObs/concept_cd/text(),"NDC:","")
let $cn := $refObs/concept_cd/@name/string()
let $oid := $refObs/observer_cd
let $sd := local:fnI2b2TimeToFhirTime($refObs/start_date/text())
let $ed := local:fnI2b2TimeToFhirTime($refObs/end_date/text())
let $sourceSystem := $refObs/@sourcesystem_cd/string()
let $importDate := local:fnI2b2TimeToFhirTime($refObs/@import_date/string())
let $downloadDate := local:fnI2b2TimeToFhirTime($refObs/@download_date/string())
let $updateDate := local:fnI2b2TimeToFhirTime($refObs/@update_date/string())


let $valType:= $refObs/valuetype_cd/text()
let $nval:=$refObs/nval_num/text()
let $tval:= $refObs/tval_char/text()

let $val:=
 if($valType="N") then $nval
 else $tval
 

let $unit:=$refObs/units_cd/text()

let $unit:=
if($unit="@") then ""
else $unit



let $fhirValue:= 
if ($valType="N") then local:fnFhirValueQuantity($nval,$unit)
else local:fnFhirValueCodeableConcept($val)

let $fhirObservation:=local:fnFhirObservation($sd,$ed,$count,$cn, $cid,$pid,$fhirValue)


return 
 <set>
 <entry xmlns="http://hl7.org/fhir">
<resource xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
{$fhirObservation}
</resource>
</entry>
</set>


return <Bundle xmlns="http://hl7.org/fhir" >
    {$O/entry}
</Bundle>


};


declare function local:processDiagObs
  ( $A as node()* )  as node()*
{

let $O:=
for $id at $count in fn:distinct-values($A/observation/id)
let $refObs :=  $A/observation[id =$id][1] (:why does some diagnosis in i2b2 have more than one modified cd?:)

let $pid := $refObs/patient_id/text()
let $cid := fn:replace($refObs/concept_cd/text(),"ICD9:","")



let $oid := $refObs/observer_cd
let $sd := local:fnI2b2TimeToFhirTime($refObs/start_date/text())
let $ed := local:fnI2b2TimeToFhirTime($refObs/end_date/text())
let $sourceSystem := $refObs/@sourcesystem_cd/string()
let $importDate := local:fnI2b2TimeToFhirTime($refObs/@import_date/string())
let $downloadDate := local:fnI2b2TimeToFhirTime($refObs/@download_date/string())
let $updateDate := local:fnI2b2TimeToFhirTime($refObs/@update_date/string())

let $modifier_cd:=$A/observation[id =$id ]/modifier_cd/text()


let $fhirDiagCondition:=local:fnFhirDiagCondition($sd , $ed ,$count , $cid, $pid )

return 
 <set>
 <entry xmlns="http://hl7.org/fhir">
<resource xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
{$fhirDiagCondition}
</resource>
</entry>
</set>


return <Bundle xmlns="http://hl7.org/fhir" >
    {$O/entry}
</Bundle>

};



declare function local:processReportObs
  ( $A as node()* )  as node()*
{

let $O:=
for $id at $count in fn:distinct-values($A/observation/id)
let $refObs :=  $A/observation[id =$id][1] 

let $pid := $refObs/patient_id/text()
let $raw_cid := $refObs/concept_cd/text()
let $textContent :=  $refObs//observation_blob/text()

let $oid := $refObs/observer_cd
let $sd := local:fnI2b2TimeToFhirTime($refObs/start_date/text())
let $ed := local:fnI2b2TimeToFhirTime($refObs/end_date/text())
let $sourceSystem := $refObs/@sourcesystem_cd/string()
let $importDate := local:fnI2b2TimeToFhirTime($refObs/@import_date/string())
let $downloadDate := local:fnI2b2TimeToFhirTime($refObs/@download_date/string())
let $updateDate := local:fnI2b2TimeToFhirTime($refObs/@update_date/string())

let $modifier_cd:=$A/observation[id =$id ]/modifier_cd/text()


let $cid:=
if ($raw_cid="LCS-I2B2:MRI_RPT") then "72230-6"
else if($raw_cid="LCS-I2B2:MRI_RPT_DID") then "72230-6"
else "UNK" 

let $fhirDiagReport:=local:fnFhirDiagReport($sd ,$ed ,$count , $cid, $pid, $textContent )

return 
 <set>
 <entry xmlns="http://hl7.org/fhir">
<resource xmlns:ns3="http://i2b2.harvard.edu/fhir/core">
{$fhirDiagReport}
</resource>
</entry>
</set>


return <Bundle xmlns="http://hl7.org/fhir" >
    {$O/entry}
</Bundle>

};



let $I:=root()(:doc('/Users/kbw19/tmp/new_git/res/i2b2-fhir/dstu2/xquery-2/src/main/resources/example/i2b2/reportsForAPatient.xml'):)
(:doc('/Users/kbw19/tmp/new_git/res/i2b2-fhir/dstu1/xquery-1/src/main/resources/example/i2b2/diagnosisForAPatient.xml'):)
(:root()doc('/Users/kbw19/tmp/new_git/res/i2b2-fhir/dstu1/xquery-1/src/main/resources/example/i2b2/labsForAPatientSimple.xml'):)
(:doc('/Users/kbw19/tmp/new_git/res/i2b2-fhir/dstu1/xquery-1/src/main/resources/example/i2b2/labsAndMedicationsForAPatient.xml')
root():)
  
let $distObs:=local:distinctObservations($I)

let $labObs:= $distObs//observation[panel_name="labs"]
let $medObs:= $distObs//observation[ panel_name="medications"]
let $diagObs:= $distObs//observation[panel_name="diagnoses"]
let $reportObs:= $distObs//observation[panel_name="reports"]

return <Bundle xmlns="http://hl7.org/fhir" xmlns:ns3="http://i2b2.harvard.edu/fhir/core">



(:INSERT_RESOURCE_FUNCTION_HERE:)
</Bundle>


(:
valueQuantity

 
 
/Users/kbw19/tmp/new_git/res/i2b2-fhir/dstu1/xquery-1/src/main/resources/example/i2b2/labsAndMedicationsForAPatient.xml
:)