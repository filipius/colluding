BEGIN { start = 0; }

start == 1 && /------------------/ {
	start = 0;
}


(start == 1) {
	for (i = 2; i <= NF; i++) {
		set[indx] = $i;
		indx++;
	}
}


/Independent set/ {
	start = 1;
	indx = 0;
	for (i = 4; i <= NF; i++) {
		set[indx] = $i;
		indx++;
	}
}


END {
	for (j = 0; j < indx; j++)
		printf "%i ", set[j];
	printf "\n";
}
