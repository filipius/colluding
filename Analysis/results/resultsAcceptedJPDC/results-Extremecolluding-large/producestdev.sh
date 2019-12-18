#! /bin/bash

#false positives
cat results-Extreme-Colluding.txt | grep -A3 positive | grep 20000 > only30fp-20000.txt
cat results-Extreme-Colluding.txt | grep -A3 positive | grep 30000 > only30fp-30000.txt
cat results-Extreme-Colluding10000.txt | grep -A1 positive | grep 10000 > only30fp-10000.txt


#false negatives
cat results-Extreme-Colluding.txt | grep -A3 negative | grep 20000 > only30fn-20000.txt
cat results-Extreme-Colluding.txt | grep -A3 negative | grep 30000 > only30fn-30000.txt
cat results-Extreme-Colluding10000.txt | grep -A1 negative | grep 10000 > only30fn-10000.txt

#false negatives
cat results-Extreme-Colluding.txt | grep -A3 execution | grep 20000 > only30exec-20000.txt
cat results-Extreme-Colluding.txt | grep -A3 execution | grep 30000 > only30exec-30000.txt
cat results-Extreme-Colluding10000.txt | grep -A1 execution | grep 10000 > only30exec-10000.txt
