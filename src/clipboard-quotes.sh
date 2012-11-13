#!/bin/bash

# @file   clipboard-quotes.sh
# @Author Frank (Frank@CrookedThumbSoftware.com)
# @date   November 2012
# @brief  Will read the default (or specified) quote file, select a random line, and copy it to the clipboard.
# Copyright (c) 2012, Crooked Thumb Software, All rights reserved.

file="../Quotes.txt"
#If there is a command line argument, use the specified file instead of the default
if [ $# -eq 1 ] ; then
    file=$1
fi
#find the count of lines in the chosen file, stripping out the file name from the wc report
lineCount=$(wc -l "$file" | sed 's/^[ \t]*\([0-9]*\).*/\1/' )
#Use 1-based random line number
randomLine=$[ ( $RANDOM % $lineCount )  + 1 ]
#grab the random line from the file, strip the Dos \r (if applicable), and wrap it in double-quotes
quote=$(head -n $randomLine "$file" | tail -n 1 | tr -d '\r' | sed 's/^/"/;s/$/"/')
#Toss it into the clipboard
echo "$quote" | pbcopy
