BEGIN {
	start = 1
}


#data
(start == 0 && !/^$/) {
	for (i = 1; i <= NF; i++)
		data[phrase, lines[phrase], i] += $i
	lines[phrase]++
	if (cols[phrase] == 0)
		cols[phrase] = NF
}


#the text before data
(start == 1)  {
	start = 0
	phrase = $0
	lines[phrase] = 0
	cols[phrase] = 0
	rounds[phrase]++
}


#blank lines
/^$/ {
	start = 1
}


END {
	for (phrase in lines) {
		print phrase
		for (l = 0; l < lines[phrase]; l++) {
			for(c = 1; c <= cols[phrase]; c++) {
				val = data[phrase, l, c] / rounds[phrase]
				if (int(val) == val) 
					printf "%d\t", val
				else 
					printf "%f\t", val
			}
			print
		}
		print
	}
}
