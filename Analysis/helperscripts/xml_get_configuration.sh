#! /bin/bash

if [ $# -lt 3 -o $# -gt 5 ]; then
	echo "Wrong arguments. It should be"
	echo "$0 xmlfile idnumber parameter [attribute]"
	exit 1
fi

#xmlstarlet sel -t -v "//$2"  $1 | tr -d "\n" | tr -s "\t" " "

#xmlstarlet sel -t -m //configuration[@id=$2] -v $3 -n $1 | tr -d "\n" | tr -s "\t" " "

if [ $# -eq 4 ]; then
	attr="$4"
else
	attr="."
fi

#echo "anteapre = $*"
#echo "apre = xmlstarlet sel -t -m //configuration[@id=$2]/$3 -v $attr -o \" \" $1"
xmlstarlet sel -t -m //configuration[@id=$2]/$3 -v $attr -o " " $1
