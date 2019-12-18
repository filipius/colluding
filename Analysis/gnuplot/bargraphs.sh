#! /bin/bash 

if [ $# -ne 2 ]; then
	echo "Wrong arguments. It should be"
	echo "$0 xml_configfile config_id"
	exit 1
fi


xmltool="../helperscripts/xml_get_configuration.sh"
xmltool2="../helperscripts/xml_get_run.sh"
basefile=`$xmltool2 $1 $2 nodeconfigfile`
baseconfignbr=`$xmltool2 $1 $2 configuration`
averageruns=`$xmltool2 $1 $2 averageruns`
averagesfile=`$xmltool2 $1 $2 averagesfile`
stdevsfile=`$xmltool2 $1 $2 stdevsfile`
headerfilename=`$xmltool $1 $baseconfignbr plotsheaderfile`
awkfile=`$xmltool $1 $baseconfignbr plotsawkfile`
deletealgsfile="deletealgs.awk"
#list_clients=( `$xmltool $1 $2 nodes/number` )
#list_pools=( `$xmltool $1 $2 pools/factor` )
list_clients=`$xmltool $1 $baseconfignbr nodes/number`
list_pools=`$xmltool $1 $baseconfignbr pools/factor`
clients_array=( $list_clients )
pools_array=( $list_pools )
nbrclients=${#clients_array[@]}
nbrpools=${#pools_array[@]}
overalltests=`expr $nbrclients \* $nbrpools`

#graphpatterns=${graphpatterns//\ /\;}
#gp2=`echo $graphpatterns | tr ' ' ';'`
#graphpatterns=`$xmltool $1 $2 algorithms/algorithm | sed s/" \$"//| tr ' ' ';'`
graphpatterns=`$xmltool $1 $baseconfignbr algorithms/algorithm[@show='1']`
algorithms_array=( $graphpatterns )
#algorithms to delete
list_del_algs=`$xmltool $1 $baseconfignbr algorithms/algorithm[@show='0'] @algid`
del_algs_array=( $list_del_algs )
nbr_del_algs=${#del_algs_array[@]}
nbralgorithms=${#algorithms_array[@]}
graphcolors=`$xmltool $1 $baseconfignbr algorithms/algorithm[@show='1'] @color | sed s/" \$"//| tr ' ' ','`
allgraphids=`$xmltool $1 $baseconfignbr algorithms/algorithm @algid`

#echo $nbrclients $nbrpools $overalltests
#echo $list_clients
#echo $list_pools
#echo $graphpatterns
#echo $allgraphids
#echo $graphcolors
#echo $list_del_algs
#exit 0

dirname="$(dirname $0)"
dirname2="$(dirname $averagesfile)"

#headerfilename="$dirname/header.txt"
#headerfilename2="$dirname/header2.txt"

resultfilename="$dirname2/bargraph"
#awkfile="$dirname/bargraph.awk"


#echo $dirname $dirname2 

#exit

format=("%.1f%%" "%.1f%%" "%.1f%%" "%.1f%%" "%.1f%%" "%g" "%g")
array=("Nodes classification errors" "Miscalculated pools" "Discarded pools" \
 	"False positive pools" "False negative pools" "Gain of malicious" "Execution time (s)")
pattern=("classification" "miscalculated" "discarded" \
 	"positive" "negative" "gain" "time")
names=("nce" "mp" "dp" "fpp" "fnp" "gm" "et")



for param in 1 2 3 4 5 6
#for param in 1
do
	shortname=${names[$param]}
	preparationname=$resultfilename-$shortname.txt
	finalname=$resultfilename-$shortname.eps

	if [ $shortname = "fpp" -o $shortname = "fnp" ]; then
		divide="DIVIDE=1"
		#headerfilename=$headerfilename2
	else
		#if [ $shortname = "gm" ]; then
		#headerfilename=$(basename -s .txt $2)-gm.txt
		#fi
		divide=""
		#headerfilename=$headerfilename1
	fi

	cat $headerfilename | sed s/"ylabel=replace-me"/"ylabel=${array[$param]}"/ | sed s/"yformat=replace-me"/"yformat=${format[$param]}"/ | sed s/"=replace-patters"/"=cluster $graphpatterns"/ | sed s/"replace-colors"/"$graphcolors"/ > $preparationname
	#echo "cat $averagesfile | gawk -f $deletealgsfile -v EXCLUDE=\"$list_del_algs\" -v ALG_LIST=\"$allgraphids\" | grep -A${overalltests} ${pattern[$param]} | tail -n ${nbrpools} | awk -f $awkfile -v HOWMANY=$nbralgorithms $divide | tr ',' '.'"
	cat $averagesfile | gawk -f $deletealgsfile -v EXCLUDE="$list_del_algs" -v ALG_LIST="$allgraphids" | grep -A${overalltests} ${pattern[$param]} | tail -n ${nbrpools} | awk -f $awkfile -v HOWMANY=$nbralgorithms $divide | tr ',' '.' >> $preparationname
	echo "=yerrorbars" >> $preparationname
	cat $stdevsfile | gawk -f $deletealgsfile -v EXCLUDE="$list_del_algs" -v ALG_LIST="$allgraphids" | grep -A${overalltests} ${pattern[$param]} | tail -n ${nbrpools} | awk -f $awkfile -v HOWMANY=$nbralgorithms $divide | tr ',' '.' >> $preparationname
	./bargraph.pl $preparationname  > $finalname
done


#rm $tmpfile
