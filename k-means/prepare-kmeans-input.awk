BEGIN {
	printf "matrix = ["
	firsttime = 1
}

/Client/ && /score/ {
	if (!firsttime)
		printf ";"
	else
		firsttime = 0
	printf "%d,%d", $5, $8
}

END {
	print "]"
	print "[centroid, pointsInCluster, assignment]=myKmeans(matrix, 2)"
}
