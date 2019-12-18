BEGIN {
	split(EXCLUDE, exclude_list)
	split(ALG_LIST, algs)
	arrcnt=asort(algs)
#	print EXCLUDE
#	for (i in exclude_list)
#		print exclude_list[i]
#	print ALGS
	for (i = 1; i <= arrcnt; i++) {
#		print algs[i]
		for (j in exclude_list)
			if (algs[i] == exclude_list[j])
				kill[i] = 1;
	}
	
#	print
#	for (i in kill) {
#		print i
#		print kill[i]
#	}
}

1 {
	if (NF < 4)
		print $0
	else {
		printf "%s\t%s\t", $1, $2
		for (j = 3; j <= NF; j++) {
			i = j - 2
			if (!(i in kill)) {
				printf("%s", $j)
				if (j != NF)
					printf("\t");
			}
		}
		printf "\n"
	}
}
