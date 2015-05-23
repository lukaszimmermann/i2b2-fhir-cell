module namespace dp = "http://edu.harvard.i2b2/dynamicPath";
declare function dp:dynamic-path
  ( $parent as node() ,
    $path as xs:string )  as item()* {

  let $nextStep := dp:substring-before-if-contains($path,'/')
  let $restOfSteps := substring-after($path,'/')
  for $child in
    ($parent/*[dp:name-test(name(),$nextStep)],
     $parent/@*[dp:name-test(name(),
                              substring-after($nextStep,'@'))])
  return if ($restOfSteps)
         then dp:dynamic-path($child, $restOfSteps)
         else $child
 } ;

declare function dp:name-test
  ( $testname as xs:string? ,
    $names as xs:string* )  as xs:boolean {

$testname = $names
or
$names = '*'
or
dp:substring-after-if-contains($testname,':') =
   (for $name in $names
   return substring-after($name,'*:'))
or
substring-before($testname,':') =
   (for $name in $names[contains(.,':*')]
   return substring-before($name,':*'))
 } ;
 
 
 declare function dp:substring-after-if-contains
  ( $arg as xs:string? ,
    $delim as xs:string )  as xs:string? {

   if (contains($arg,$delim))
   then substring-after($arg,$delim)
   else $arg
 } ;
 
declare function dp:substring-before-if-contains
  ( $arg as xs:string? ,
    $delim as xs:string )  as xs:string? {

   if (contains($arg,$delim))
   then substring-before($arg,$delim)
   else $arg
 } ;





