import module namespace dp="http://edu.harvard.i2b2/dynamicPath" at "modules/dynamicPath.xquery";

declare function local:traversePathAndSetAttribute($xml as node(),$pathSeq as item()*,$attrName as xs:string,$attrValue as xs:string) as node(){
  let $cT:=$pathSeq[1]
  let $nT:=$pathSeq[2]
  let $pathRemSeq:=subsequence($pathSeq,2)
  let $cN:= string(node-name($xml))
 
  return if(empty($xml)) then $xml
  else if($cT ne $cN) then $xml
  else if($cT eq $cN) then 
   (
      element {$cN} {
        if(empty($pathRemSeq)) then attribute{$attrName}{$attrValue}
        else (), 
        $xml/@*,
          $xml/*[node-name() ne xs:QName($nT)],  
         for $child in $xml/*[node-name() eq xs:QName($nT)]  
         return local:traversePathAndSetAttribute($child,$pathRemSeq ,$attrName,$attrValue)
       }
   )else ()
};

declare function dp:overwrite-path(
  $xml as node()?,
  $pathSeq as item()*
) as item()*
{

  let $pathRemSeq:=subsequence($pathSeq,2)
  let $cT:=$pathSeq[1]
  let $nT:=$pathSeq[2]
  let $cN:= string(node-name($xml))
  let $cNODE:= $xml/*[node-name() eq xs:QName($cN)]
  let $msg:= if ( empty($xml) ) then dp:make-nested-elements($pathSeq)
   else if($cT eq $cN) then 
   (
       element {$cN} {   
         $xml/@*, 
         $xml/*[node-name() ne xs:QName($nT)],
           dp:overwrite-path( $cNODE/*[node-name() eq xs:QName($nT)],
           $pathRemSeq)   
       }
   )
   else if($cT ne $cN) then 
     element {$cN} {
          $xml/@*,
          $xml/*,
           dp:make-nested-elements($pathRemSeq) 
     }
  else <s/>
  
   
 
   return $msg
};

declare function local:traversePathAndSetText($xml as node(),$pathSeq as item()*,$Value as xs:string) as node(){
  let $cT:=$pathSeq[1]
  let $nT:=$pathSeq[2]
  let $pathRemSeq:=subsequence($pathSeq,2)
  let $cN:= string(node-name($xml))
 
 
  return if(empty($xml)) then $xml
  else if($cT ne $cN) then $xml
  else if($cT eq $cN) then 
   (
      element {$cN} {
        $xml/@*,
          $xml/*[node-name() ne xs:QName($nT)],  
         for $child in $xml/*[node-name() eq xs:QName($nT)]  
         return local:traversePathAndSetText($child,$pathRemSeq ,$Value),
         if(empty($pathRemSeq)) then $Value else()
       }
   )else ()
  
  
};


declare function local:setValueOrAttrbute($xml as node(),$path as xs:string,$Value as xs:string) as node(){
  let $seq:=tokenize($path,"/")
  let $testLast:=$seq[last()]
 
  let $last:= if(contains($testLast,"@")) then $seq[last()] else "undef"
  let $acSeq:= if(contains($testLast,"@")) then subsequence($seq,1,fn:index-of($seq,$testLast)-1) else $seq
   let $xml:= dp:overwrite-path($xml,$acSeq)
  (:local:traversePathAndSetText:)
  return $xml
};

declare function local:addPathAttribute($xml as node(),$pathSeq as item()*,$attrName as xs:string,$attrValue as xs:string) as node(){
  $xml
};

let $c :=
  <Patient>
    <maritalStatus>
      <Coding>
        <Code/>
      </Coding>
    </maritalStatus>
  </Patient>


let $b:= local:traversePathAndSetAttribute($c,tokenize("a/Patient/maritalStatus/Coding/Code","/"),"attr1","val1")

let $d:= local:traversePathAndSetAttribute($b,tokenize("a/Patient/gender/Coding/Code","/"),"attr1","val1")


let $e:= local:traversePathAndSetText($c,tokenize("a/Patient/maritalStatus/Coding/Code","/"),"val1")

let $f:= local:setValueOrAttrbute($c,"a/Patient/maritalStatus/Coding/Code","val1")

return dp:overwrite-path($c,tokenize("Patient/gender/Coding/Code","/"))
(:
copy $c :=<a>
  <Patient>
    <maritalStatus>
      <Coding>
        <Code/>
      </Coding>
    </maritalStatus>
  </Patient>
</a>
modify (
  
  insert node (attribute { 'a' } { 5 }, 'text', <e/>) into $c
) 
return $c
:)