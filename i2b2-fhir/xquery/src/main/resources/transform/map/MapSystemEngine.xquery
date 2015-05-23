import module namespace dp="http://edu.harvard.i2b2/dynamicPath" at "../modules/dynamicPath.xquery";
declare namespace m="http://i2b2.harvard.edu/map" ;
declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/" ;


declare function local:insert($I as node(),$value as item(),$path as xs:string) as xs:string*{
let $xq:=concat('declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";declare namespace m="http://i2b2.harvard.edu/map" ;declare variable $xml external;',
"copy $c :=$xml modify (replace value of node ",$path," with '",$value,"')return $c")
    return $xq
};

declare function local:ev($I as node(),$i as xs:string)as node(){
    let $xq:=
    concat('declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";declare namespace m="http://i2b2.harvard.edu/map" ;
    declare variable $xml external;copy $c :=$xml modify (delete node $c/Patient/maritalStatus/Coding/Code 
    , insert node ',$i,' into $c/Patient/maritalStatus/Coding
    ',') return $c')

    return xquery:eval(
     $xq,
     map { 'xml': $I })
 };
 
 declare function local:addAttrib($I as node(),$q as xs:string) as node(){
 let $xq:=
    concat('declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";declare namespace m="http://i2b2.harvard.edu/map" ;import module namespace dp="http://edu.harvard.i2b2/dynamicPath" at "../modules/dynamicPath.xquery";
    declare variable $xml external;',$q)

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
    for $frValue in dp:dynamic($frPath,$I)
        let $toValue:=dp:dynamic(concat("/m:Map/m:ValueMap[(@from='",$frValue,"')]/@to/string()",""),$MS)
        
        let $struct:=dp:add-path(<a></a>, tokenize($toPath, '/'))
        let $structWithAttrStr:=concat('dp:add-attributes($xml/',$toPath,', xs:QName("value"), "',$toValue,'")')
       let $newCodeNode:=local:addAttrib($struct,$structWithAttrStr)
       return local:ev($struct,concat('<Code value="',$toValue,'"></Code>'))
     
       




(:
local:ev1($struct,$structWithAttrStr)

dp:add-attributes($struct/Patient/maritalStatus/Coding/Code, xs:QName('value'), $toValue)
local:ev($xml)
local:insert($struct,"NewVal",$toPath)
replace value of node $c/title with concat('Copy of: ', $c/title),
  insert node <author>Joey</author> into $c
       
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




