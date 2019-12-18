BEGIN { 
	started = 0;
	occurrences = 0;
}

(1) {
	if (started) {
		if (minimum > $1)
			minimum = $1
		if (maximum < $1)
			maximum = $1
	} else {
		minimum = $1;
		maximum = $1;
		started = 1;
	}
	sum += $1;
	occurrences += 1;
}

END {
	average = sum / occurrences;
	printf "%lf %lf %lf\n", minimum, average, maximum;
}