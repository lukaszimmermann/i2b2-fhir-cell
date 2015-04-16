
let $I:=
if(empty(root())) 
then doc('/Users/kbw19/Documents/workspace-sts/xqueryProject1/resources/example/i2b2/medicationsForAPatient.xml')
else root()

return <p>{$I}</p>