declare default element namespace "http://hl7.org/fhir";
declare namespace i="http://i2b2.harvard.edu/fhir/core";

declare function f:getResource($id as xs:string?)
as xs:Node()?
{
let $disc := ($p * $d) div 100
return //i:Resource[@id='{$id}']
};

f:getResource("Patient/example")