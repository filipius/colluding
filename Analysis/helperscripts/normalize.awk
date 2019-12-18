BEGIN {
	maximum = MAX
}


(1) {
	print (1.0 * $1 / maximum);
}
