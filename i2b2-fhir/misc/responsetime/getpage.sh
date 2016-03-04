#start=`date +%H:%M:%S` 
echo "sending call $1"
msg=`gtime -f '%E'	/usr/local/bin/wget -q $1`
#end=`date +%H:%M:%S` 
#printf "%s " `hostname` $start $end 
printf "%s "  $2	$msg  
