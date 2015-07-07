declare default element namespace "http://hl7.org/fhir";
declare namespace i="http://i2b2.harvard.edu/fhir/core";
declare namespace functx = "http://www.functx.com";
declare function functx:dynamic-path
  ( $parent as node() ,
    $path as xs:string )  as item()* {

  let $nextStep := functx:substring-before-if-contains($path,'/')
  let $restOfSteps := substring-after($path,'/')
  for $child in
    ($parent/*[functx:name-test(name(),$nextStep)],
     $parent/@*[functx:name-test(name(),
                              substring-after($nextStep,'@'))])
  return if ($restOfSteps)
         then functx:dynamic-path($child, $restOfSteps)
         else $child
 } ;

declare function functx:name-test
  ( $testname as xs:string? ,
    $names as xs:string* )  as xs:boolean {

$testname = $names
or
$names = '*'
or
functx:substring-after-if-contains($testname,':') =
   (for $name in $names
   return substring-after($name,'*:'))
or
substring-before($testname,':') =
   (for $name in $names[contains(.,':*')]
   return substring-before($name,':*'))
 } ;
 
 
 declare function functx:substring-after-if-contains
  ( $arg as xs:string? ,
    $delim as xs:string )  as xs:string? {

   if (contains($arg,$delim))
   then substring-after($arg,$delim)
   else $arg
 } ;
 
declare function functx:substring-before-if-contains
  ( $arg as xs:string? ,
    $delim as xs:string )  as xs:string? {

   if (contains($arg,$delim))
   then substring-before($arg,$delim)
   else $arg
 } ;


declare function local:getResource($id as xs:string?,$I as node())
as node()
{
    $I/(Patient|i:Resource)[@id=$id]
};

declare function local:matchDate($path as xs:string,$eValue as xs:date,$operator as xs:string, $U as node())
as xs:boolean
{
    let $I:=local:getResource("Patient/example",$U)
    let $v:=xs:date(string(functx:dynamic-path($I,$path)))
    let $r:= 
    if($operator eq "=" or $operator eq ">=" or $operator eq "<=") then
        if ($v eq $eValue) then boolean('true') 
        else if($operator eq "<" or $operator eq "<=") then
        if ($v lt $eValue) then boolean('true') else boolean(())
        else if($operator eq ">" or $operator eq ">=") then
        if ($v gt $eValue) then boolean('true') else boolean(())
        else boolean(())
    else boolean(())
    return  $r
};

let $path:="birthDate/@value"
return local:matchDate($path,xs:date("1944-11-17"),"<",/root())




(:
true return local:matchDate($path,xs:date("1944-11-17"),"=",/root())
false return local:matchDate($path,xs:date("1943-11-17"),"=",/root())
:)


