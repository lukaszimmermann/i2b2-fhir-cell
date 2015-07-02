 
 (: Distinct observations for one patient:)
 let $PID := 1000000001

let $distobs :=
 for $t in //observation
 let $eid := $t/event_id/text()
 let $pid := $t/patient_id/text()
 let $cid := $t/concept_cd/text()
 let $cn := $t/concept_cd/@name
 let $oid := $t/observer_cd
 let $sd := $t/start_date/text()
 let $ed := $t/end_date/text()
 let $m := $t/modifier_cd/text()
 let $val_cd := $t/valuetype_cd/text()
 let $tval_char := $t/tval_char/text()
 
  let $id := concat($pid,"-",$eid,"-",$cid,"-",$sd)

 return
       if ($t is (//observation)[. = $t and patient_id/text() = $PID ][1]
        
       ) then
            <observation>
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
let $freq :=  $refObs/valuetype_cd/text()  
let $tval_char:=  $refObs/tval_char/text()
let $cid := $refObs/concept_cd/text()
let $cn := $refObs/concept_cd/@name/string()
 let $oid := $refObs/observer_cd
 let $sd := $refObs/start_date/text()
 let $ed := $refObs/end_date/text()



return 

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
