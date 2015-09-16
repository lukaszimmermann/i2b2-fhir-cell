
export F=$1
export T=/tmp/tmpfile
export L="copyright_header.txt"

if grep "Copyright" $F >/dev/null ;
then 
	echo "Y"; 

else 
	echo "N" 
	cat "$L" >$T  
	cat $F >> $T
	mv $T $F
fi
