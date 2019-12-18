 {
 	printf "%d ", $1
 	for (i = 3; i < HOWMANY + 3; i++) {
	  	if (DIVIDE == 1)
	 		printf "%lf ", 100.0 * $i / $2
	 	else
	  		printf "%lf ", $i
 	}
 	printf "\n"
 }
