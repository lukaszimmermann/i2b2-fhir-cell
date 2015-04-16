(:
for $id at $count in fn:distinct-values($A/observation/id)
let $refObs :=  $A/observation[id =$id and modifier_cd = "MED:FREQ"]

let $freqMod :=  $A/observation[id =$id and modifier_cd = "MED:FREQ"]
let $freq :=  $freqMod/tval_char/text()  
let $freqNode := local:fnDose($freq)

let $doseMod :=  $A/observation[id =$id and modifier_cd = "MED:DOSE"]
let $dose :=  $doseMod/tval_char/text()   (:XXX what does E mean

let $routeMod :=  $A/observation[id =$id and modifier_cd = "MED:ROUTE"]
let $route :=  $routeMod/tval_char/text()  
let $routeNode := local:fnRoute($route) (:XXX to add more routes

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

:)