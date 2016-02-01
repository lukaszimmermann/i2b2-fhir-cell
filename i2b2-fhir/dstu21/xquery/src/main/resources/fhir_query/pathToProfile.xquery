declare default element namespace "http://hl7.org/fhir";
let $I:=doc('/Users/kbw19/Documents/workspace-sts-dstu2/fhir-build-svn/build/publish/guidanceresponse.profile.xml')

for $t in $I//element
return <A>{$t}</A>