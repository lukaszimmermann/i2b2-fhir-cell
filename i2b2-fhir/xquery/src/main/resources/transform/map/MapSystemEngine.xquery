(:import module namespace dp="http://edu.harvard.i2b2/dynamicPath" at "../modules/dynamicPath.xquery";:)
declare namespace dp="http://edu.harvard.i2b2/dynamicPath";
declare namespace m="http://i2b2.harvard.edu/map" ;
declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/" ;

declare function local:dynamic($path as xs:string,$I as node()*) as item()*{
    let $xq:=concat('declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";declare namespace m="http://i2b2.harvard.edu/map" ;declare variable $xml external;',
    ' $xml/',$path,'')
    return xquery:eval(
  $xq,
  map { 'xml': $I })

};

let $I:=doc("../../example/i2b2/AllPatients.xml")

let $M:=doc("../../example/map/MapSystem1.xml")


for $MS in $M//m:MapSystem
    let $toPath:=$MS/m:ToPath/m:Path/text()
    let $frPath:=$MS/m:FromPath/m:Path/text()
    let $p:="//ns2:patient_set/patient/param[(@column='marital_status_cd')]/text()"
    for $frValue in local:dynamic($frPath,$I)
        let $toValue:=local:dynamic(concat("/m:Map/m:ValueMap[(@from='",$frValue,"')]/@to/string()",""),$MS)
        return $toValue
(:     
concat($frValue,'->',$toValue)
for $iMaritalStatus in $I//ns2:patient_set/patient/param[(@column='marital_status_cd')]/text()
return $iMaritalStatus

return local:dynamic("//patient",$I)

return xquery:eval(
  'declare variable $xml external;$xml//patient',
  map { 'xml': $I })

declare variable $xml external;
  declare variable $p external;
   for $x in $xml//patient return $x"
   


:)