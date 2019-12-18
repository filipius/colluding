BEGIN {
	option = OPTION
	totalnodes = NODES
		#this option can be
		#1 - node classification errors
		#2 - miscalculated pools
		#3 - results discarded
		#4 - false positives
		#5 - false negatives
		#6 - overall gain of malicious
		#7 - run time of the algorithm
	#print "totalnodes =", totalnodes
}


/pools =/ {
	split($NF, a, "]")
	goodnodes = a[1]
	badnodes = totalnodes - goodnodes
	#print "good nodes = ", goodnodes
}

#/Algorithm/ {
#}

(/classification errors/ && option == 1) {
	printf "%d\t", $1
}


(/valid pools/ && option != 1 && option < 6) {
	totalpools = $1
	lostpools = $6
	falsepositives = $9
	
	if (option == 2)
		printf "%d\t", $4
	if (option == 3)
		printf "%d\t", lostpools
	if (option == 4)
		printf "%d\t", falsepositives
	if (option == 5)
		printf "%d\t", $12
}

(/gain data:/ && option == 6) {
	#goodpools = totalpools - lostpools# - falsepositives
	#poolscomputedbygoodnodes = (1.0 * totalpools / totalnodes) * goodnodes
	#poolscomputedbybadnodes = (1.0 * totalpools / totalnodes) * badnodes
	#overheadofbadnodes = poolscomputedbygoodnodes - goodpools
	#gainofbadnodes = 1.0 * overheadofbadnodes / poolscomputedbygoodnodes
	#gainperbadnode = 1.0 * gainofbadnodes / (badnodes / totalnodes)
	discardedvotes = $5
	votesbyincorrect = $9
	if (badnodes > 0)
		gainofbadnodes = 1.0 * discardedvotes / votesbyincorrect
	else
		gainofbadnodes = -1000   #some impossible value
	printf "%f\t", gainofbadnodes
}

(/runtime/ && option == 7) {
	printf "%f\t", $2
}

END {
	print
}
