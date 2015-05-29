import module namespace dp="http://edu.harvard.i2b2/dynamicPath" at "../modules/dynamicPath.xquery";
declare namespace m="http://i2b2.harvard.edu/map" ;
declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/" ;
 declare namespace functx = "http:// www.functx.com";


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

declare function local:add-attribute-Path ($ xml as element()*, $path as xs:string, $name as xs:string, $ value as xs:anyAtomicType?) 
 as item()* {
    
  let $first:=tokenize($path,"/")[1]
    return $first
   };
 

 declare function functx:add-attribute ($ element as element( ), $ name as xs:string, $ value as xs:anyAtomicType?) 
 as element( ) {
    element { node-name( $ element)} { 
      attribute {$ name} {
        $ value},
       $ element/@*, 
       $ element/ node( ) }
     };
     
  
   declare function local:overwrite-path(
  $xml as node()?,
  $paths as xs:string*
) as node()
{
   $xml
};   
 

declare function local:createNode($xml as node(),$path as xs:string,$value as xs:string) as node(){
  
  (:tokenize :)
 
  let $seq:=tokenize($path,"/")
  let $testLast:=$seq[last()]
  let $last:= if(contains($testLast,"@")) then $testLast else ""
  let $acPath:= if(contains($testLast,"@")) then fn:string-join(subsequence($seq,1,fn:index-of($seq,$testLast)-1),"/") else $path 
  
  return  local:overwrite-path($xml/Patient, tokenize($acPath, '/'))
  
};

    declare function local:overwrite-path1(
  $xml as node()?,
  $pathSeq as item()*
) as item()*
{
 
  let $pathRemSeq:=subsequence($pathSeq,2)
  let $cT:=$pathSeq[1]
  let $cN:= string(node-name($xml))
   let $cNODE:= $xml/*[node-name() eq xs:QName($cN)]
  let $msg:=
   if ( empty($xml) ) then dp:make-nested-elements($pathSeq)
   else if (  empty($cN) or empty($cT) ) then $xml
   else if($cT ne $cN) then 
   (
       element {$cN} {   
         $xml/*[node-name() ne xs:QName($cT)],   
         $cNODE/@*, 
         $cNODE/descendant::*[node-name() ne xs:QName($cT)],
           local:overwrite-path1( $xml/*[node-name() eq xs:QName($cT)],
           $pathSeq)   
       }
   )
   else element {$cT} { 
          $xml/@*, 
         $xml/descendant::*[node-name() ne xs:QName($cT)],
         dp:make-nested-elements($pathRemSeq) 
       }
  
   
 
   return $msg
};

declare function functx:add-attributes
  ( $elements as element()* ,
    $attrNames as xs:QName* ,
    $attrValues as xs:anyAtomicType* )  as element()? {

   for $element in $elements
   return element { node-name($element)}
                  { for $attrName at $seq in $attrNames
                    return if ($element/@*[node-name(.) = $attrName])
                           then ()
                           else attribute {$attrName}
                                          {$attrValues[$seq]},
                    $element/@*,
                    $element/node() }
 } ;
 


declare function dp:overwrite-path(
  $xml as node()?,
  $pathSeq as item()*
) as item()*
{
  

  let $pathRemSeq:=subsequence($pathSeq,2)
  let $cT:=$pathSeq[1]
  let $cN:= string(node-name($xml))
   let $cNODE:= $xml/*[node-name() eq xs:QName($cN)]
  let $msg:= if ( empty($xml) ) then dp:make-nested-elements($pathSeq)
   else if (  empty($cN) or empty($cT) ) then $xml
   else if($cT ne $cN) then 
   (
       element {$cN} {   
         $xml/*[node-name() ne xs:QName($cT)],   
         $cNODE/@*, 
         $cNODE/descendant::*[node-name() ne xs:QName($cT)],
           dp:overwrite-path( $xml/*[node-name() eq xs:QName($cT)],
           $pathSeq)   
       }
   )
   else element {$cT} { 
          $xml/@*, 
         $xml/child::*[node-name() ne xs:QName($cT)],
         dp:make-nested-elements($pathRemSeq) 
       }
  
   
 
   return $msg
};


let $P:=<a><Patient id="123"><gender x="3"/></Patient><Medication/><MedicationStatement/></a>
let $path:="Patient/maritalStatus/Coding/Code"
let $path2:="Patient/gender/Coding/Code/@value"
let $A:= dp:overwrite-path($P, tokenize($path,"/")) 
return dp:check-attr($A, tokenize($path2,"/")) 
(: 
local:add-attribute-Path($P,$path,"value","M")
contains("@value","@")
functx:add-attribute(*,"value","M")


let $I:=doc("../../example/i2b2/AllPatients.xml")

let $M:=doc("../../example/map/MapSystem1.xml")

let $PT:=<Patient/>

for $MS in $M//m:MapSystem
    let $toPath:=$MS/m:ToPath/m:Path/text()
    let $frPath:=$MS/m:FromPath/m:Path/text()
    let $p:="//ns2:patient_set/patient/param[(@column='marital_status_cd')]/text()"
    for $frValue in dp:dynamic($frPath,$I)
        let $toValue:=dp:dynamic(concat("/m:Map/m:ValueMap[(@from='",$frValue,"')]/@to/string()",""),$MS)
        
        let $struct:=dp:add-path(<a></a>, tokenize($toPath, '/'))
        let $structWithAttrStr:=concat('dp:add-attributes($xml/',$toPath,', xs:QName("value"), "',$toValue,'")')
       return $struct 
    
      let $newCodeNode:=local:addAttrib($struct,$structWithAttrStr)
       return local:ev($struct,concat('<Code value="',$toValue,'"></Code>'))
     
       





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




