(:declare variable $doc as document-node(element(*, xs:untyped)) external;
 :)
 (: Distinct observations for one patient:)
xquery version "1.0";
declare namespace functx = "http://www.functx.com";
 
declare function local:fnI2b2TimeToFhirTime($r as xs:string?) as xs:string{ 
fn:replace($r,'.000Z$','') 
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
 
 
declare function local:distinctObservations($I as node()?) as node ()?{


let $distobs :=
 (:for $t in $doc//observation:)
 for $t in $I//observation
 let $eid := $t/event_id/text()
 let $pid := $t/patient_id/text()
 let $cid := $t/concept_cd/text()
 let $cn := $t/concept_cd/@name
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
 
  let $id := concat($pid,"-",$eid,"-",$cid,"-",$sd)

 return
  
             <observation sourcesystem_cd="{$sourceSystem}" import_date="{$importDate}" download_date="{$downloadDate}" update_date="{$updateDate}">
                        <id>{$id}</id>
                        <event_id source="HIVE">{$eid}</event_id>
                        <patient_id source="HIVE">{$pid}</patient_id>
                        <concept_cd name="{$cn}">{$cid}</concept_cd>
                        {$oid}
                        <start_date>{$sd}</start_date>
                        <modifier_cd>{$m}</modifier_cd>
                        <valuetype_cd>{$val_cd}</valuetype_cd>
                        <tval_char>{$tval_char}</tval_char>
                        <nval_num />
                        <valueflag_cd />
                        <end_date>{$ed}</end_date>
                        <location_cd name="">@</location_cd>
                    </observation>
    return  <set>{functx:distinct-nodes($distobs)}</set>
 };
 
 let $I:=
if(empty(root())) 
then doc('/Users/***REMOVED***/Documents/workspace-sts/xqueryProject1/resources/example/i2b2/medicationsForAPatient.xml')
else root()
let $distObs:=local:distinctObservations($I)
 
return $distObs
