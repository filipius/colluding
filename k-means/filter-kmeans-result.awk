BEGIN {
	started = 0
}

(started == 1 && NF > 0) {
	printf "%d ", $1
}

/assignment/ {
	started = 1
}


END {
	printf "\n"
}
