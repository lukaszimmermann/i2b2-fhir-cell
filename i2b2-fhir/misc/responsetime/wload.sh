if [[ $# < 1 ]]; then  
        echo "Usage: wload #start #end" 
        exit 
fi 
start=`date +%H:%M:%S` 
#echo $start
for ((i=$1; i<=$2; i++))
do 
 #echo $i
  #export p="http://fhir.i2b2.org/srv-dstu21-0.3/api/open/Patient/10000000$i"
  #export p="http://localhost:8080/srv-dstu21-0.3/api/open/Observation/10000000$i-1"
  export p="http://localhost:8080/srv-dstu21-0.3/api/open/Observation?patient=10000000$i"
  #echo $p
  sleep $3	
  sh getpage.sh $p $3 &
 #/usr/local/bin/wget -o /dev/null -q $p & 
done 
wait 
#end=`date +%H:%M:%S` 
#printf "%s " `hostname` $1 $start $end 
echo
