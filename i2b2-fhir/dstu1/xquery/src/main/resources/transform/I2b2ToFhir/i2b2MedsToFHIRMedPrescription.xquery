xquery version "1.0";
declare namespace functx = "http://www.functx.com";
 
 
declare function local:fnI2b2TimeToFhirTime($r as xs:string?) as xs:string{ 
fn:replace($r,'.000Z$','') 
};
 
 
declare function local:fnDoseFhir($dose as xs:string?,$unit as xs:string?) as node()?
{ 
<doseQuantity>
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
<timingSchedule>
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

let $c:=
if($r="PO") then ""
else if($r="tid") then "394899003"
else "UNK"

let $d:=
if($r="PO") then ""
else if($r="tid") then "oral administration of treatment"
else "UNK"

return
   
<route>
   <coding>
      <system value="http://snomed.info/sct"/>
      <code value="{$c}"/>
      <display value="{$d}"/>
      <primary value="true"/>
   </coding>  
 </route>
   
};


declare function local:fnFhirMedication($count as xs:integer,$cn as xs:string, $cid as xs:string, $pid as xs:string) as node(){
<Resource namespace="http://hl7.org/fhir" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:type="ns3:Medication" xmlns:ns2="http://www.w3.org/1999/xhtml"
           
 >
 <id value="Medication/{$pid}-{$count}"/>
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
      <primary value="true"/>
    </coding>
  </code>

  </Resource>
  
};

declare function local:fnMetaData($id as xs:string*, $last_updated as xs:string* ) as node(){
<MetaData>
    <id>{$id}</id>
    <lastUpdated>{$last_updated}</lastUpdated>
</MetaData>
};

declare function local:fnFhirMedicationPrescription($count as xs:integer?, $timingScheduleFhir as node()?, $routeFhir as node()?,$doseQuantityFhir as node()?, $medication_id as xs:string?,
        $sd as xs:string, $ed as xs:string, $pid as xs:string?,$instr as xs:string?) as node(){
 
 <Resource xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" namespace="http://hl7.org/fhir" xsi:type="ns3:MedicationPrescription" >
  <id value="MedicationPrescription/{$pid}-{$count}"/>
   <text>
    <status value="generated"/>
    <div xmlns="http://www.w3.org/1999/xhtml">
      <p>{$instr}  </p>
     </div>
  </text>
  <status value="active"/>
  <patient>
     <reference value="Patient/{$pid}"/>
  </patient>
 
 <!-- <prescriber>
    <reference value="Practitioner/example"/>
  </prescriber>
    <start value="{$sd}"/>
    <end value="{$ed}"/>
-->
  <medication>
    <reference value="{$medication_id}"/> 
  </medication>
    


  <dosageInstruction>
  {$timingScheduleFhir}
  {$routeFhir}
  {$doseQuantityFhir}
    </dosageInstruction>

  
</Resource>
 
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
 
  let $id := concat($pid,"-",$eid,"-",$cid,"-",$sd,"-",$oid,"-",$instNum) (:all in primary key except modifier_cd, but includes instNum:)

 return 
  
             <observation sourcesystem_cd="{$sourceSystem}" import_date="{$importDate}" download_date="{$downloadDate}" update_date="{$updateDate}">
                        <id>{$id}</id>
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
                    </observation>
    return  <set>{functx:distinct-nodes($distobs)}</set>
 };
 
 let $I:= root()
 (:doc('/Users/kbw19/tmp/new_git/res/i2b2-fhir/dstu1/xquery/src/main/resources/example/i2b2/MedicationsForAPatient3.xml')
 
:)
let $distObs:=local:distinctObservations($I)
 

let $A:=$distObs

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
let $sd := $refObs/start_date/text()
let $ed := $refObs/end_date/text()

let $fhirMedication:=local:fnFhirMedication($count,$cn, $cid,$pid)
let $medication_id:=concat("Medication/",$pid,"-",$count)
let $timingScheduleFhir:=local:fnTimingSchedule($freq)
let $routeFhir:=local:fnRoute($route)
let $doseQuantityFhir:=local:fnDoseFhir($dose,$doseUnit)


let $fhirMedicationPrescription:=local:fnFhirMedicationPrescription($count,$timingScheduleFhir,$routeFhir,$doseQuantityFhir,$medication_id,$sd,$ed,$pid,$instr)


return <set>
<MetaResource>
{$fhirMedication}
{local:fnMetaData(concat("Medication/",$pid,"-",xs:string($count)),$updateDate)}
</MetaResource>

<MetaResource>
{$fhirMedicationPrescription}
{local:fnMetaData(concat("MedicationPrescription/",$pid,"-",xs:string($count)),$updateDate)}
</MetaResource>

</set>


return <ns4:metaResourceSet xmlns:ns2="http://www.w3.org/1999/xhtml" xmlns:ns3="http://hl7.org/fhir"
    xmlns:ns4="http://i2b2.harvard.edu/fhir/core">
    {$O/MetaResource}
    </ns4:metaResourceSet>

