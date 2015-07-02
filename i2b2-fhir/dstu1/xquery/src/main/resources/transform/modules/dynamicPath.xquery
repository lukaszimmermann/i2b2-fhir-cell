module namespace dp = "http://edu.harvard.i2b2/dynamicPath";

declare function dp:make-nested-elements(
  $element-names as xs:string*
) as element()*
{
  if (empty($element-names)) then ()
  else element { $element-names[1] } {
    dp:make-nested-elements(subsequence($element-names, 2))
  }
};

declare function dp:add-path(
  $xml as node()?,
  $paths as xs:string*
) as node()
{
  let $first := $paths[1]
  let $rest := subsequence($paths, 2)
  return
    if (empty($first)) then $xml
    else if ($xml instance of text()) then $xml
    else if (node-name($xml) = xs:QName($first))
    then element { $first } {
        $xml/@*,
        for $n in $xml/node()
        return dp:add-path($n, $rest)
      }
    else element { node-name($xml) } {
        $xml/@*, $xml/node(),
        dp:make-nested-elements($paths)
      }
};

declare function dp:dynamic($path as xs:string,$I as node()*) as item()*{
    let $xq:=concat('declare namespace ns2="http://www.i2b2.org/xsd/hive/pdo/1.1/";declare namespace m="http://i2b2.harvard.edu/map" ;declare variable $xml external;',
    ' $xml/',$path,'')
    return xquery:eval(
  $xq,
  map { 'xml': $I })

};

declare function dp:update-attributes
  ( $elements as element()* ,
    $attrNames as xs:QName* ,
    $attrValues as xs:anyAtomicType* )  as element()? {

   for $element in $elements
   return element { node-name($element)}
                  { for $attrName at $seq in $attrNames
                    return if ($element/@*[node-name(.) = $attrName])
                           then attribute {$attrName}
                                     {$attrValues[$seq]}
                           else (),
                    $element/@*[not(node-name(.) = $attrNames)],
                    $element/node() }
 } ;
 
 declare function dp:add-attributes
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
 


