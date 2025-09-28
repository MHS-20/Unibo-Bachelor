ls -R | egrep '\..+$' | rev | cut -f1 -d. | rev | sort | uniq -c | sort -nr | head -n5

cat The_Adventures_Of_Sherlock_Holmes.txt | tr -cd 'a' | wc
cat The_Adventures_Of_Sherlock_Holmes.txt | grep -oi "sherlock" | wc -l
cat The_Adventures_Of_Sherlock_Holmes.txt | tr ' ' '\n' | sort | uniq -c | sort -nr | head -n5