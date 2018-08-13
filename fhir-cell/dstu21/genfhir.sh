cd ~/tmp/fhir-build/build/publish/; 
xjc fhir-all.xsd -d orghl7java
rm -rf ~/git/res/i2b2-fhir/dstu2/core-2/src/main/java/org
mv orghl7java/org ~/git/res/i2b2-fhir/dstu2/core-2/src/main/java
cd ~/git/res/i2b2-fhir/dstu2/core-2/src/main/java/org/hl7/fhir/
for x in $(ls *.java); do y=$(echo $x|sed 's/.java//') ;cat $x|sed -e $"s/public class/@javax.xml.bind.annotation.XmlRootElement(name=\"$y\") NNNpublic class/" |sed -e $'s/NNN/\\\n/'>/tmp/f; mv /tmp/f $x ;done; 
