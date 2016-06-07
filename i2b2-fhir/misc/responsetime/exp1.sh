for x in {1..10}; do
  cmd="/bin/sh wload.sh 11 20 $x"
  echo $cmd
 $($cmd)
  wget "http://52.3.50.52/srv-dstu21-0.3/api/open/reset_cache"
  sleep 3
done;
