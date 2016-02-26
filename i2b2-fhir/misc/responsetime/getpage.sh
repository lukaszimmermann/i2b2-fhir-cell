#start=`date +%H:%M:%S` 
	gtime -f '%E'	/usr/local/bin/wget -q $1
#end=`date +%H:%M:%S` 
#printf "%s " `hostname` $start $end 
