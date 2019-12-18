#averages must come first
BEGIN {
	filenbr = 0
}

(FNR == 1) {
	filenbr += 1
#	if (filenbr == 2)
#		for (phrase in lines) {
#			print phrase
#			for (l = 0; l < lines[phrase]; l++) {
#				for(c = 1; c <= cols[phrase]; c++) {
#					val = avg[phrase, l, c]
#					#if (int(val) == val)
#					#	printf "%d\t", val
#					#else
#					printf "%lf\t", val
#				}
#				print "\n"
#			}
#			print "\n"
#		}
#	print "new file!"
}


#data
(start == 0 && !/^$/) {
	data[phrase, lines[phrase], 1] = $1
	data[phrase, lines[phrase], 2] = $2
	for (i = 1; i <= NF; i++)
		#averages file
		if (filenbr == 1) {
			avg[phrase, lines[phrase], i] = $i
			cols[phrase] = NF
		}
		else {
			data[phrase, lines[phrase], i] += ($i - avg[phrase, lines[phrase], i])^2
		}
	lines[phrase]++
}


#the text before data
(start == 1)  {
	start = 0
	phrase = $0
	lines[phrase] = 0
	if (filenbr > 1)
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
			
			for(c = 1; c <= 2; c++)
				printf "%d\t", data[phrase, l, c]
			for(c = 3; c <= cols[phrase]; c++) {
				val = sqrt(data[phrase, l, c] / (rounds[phrase] - 1))
				#if (int(val) == val)
				#	printf "%d\t", val
				#else
				printf "%f\t", val
			}
			print
		}
		print
	}
}
