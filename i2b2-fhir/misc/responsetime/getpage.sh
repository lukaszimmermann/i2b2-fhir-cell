#start=`date +%H:%M:%S` 
echo "sending call $1"
msg=`time -f '%E' 2>&1	/usr/bin/wget -q $1`
#end=`date +%H:%M:%S` 
#printf "%s " `hostname` $start $end 
printf "%s "  $2 $msg >>result 
echo "" >> result
printf "%s "  $2 $msg  
