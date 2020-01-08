time=$(date "+%Y%m%d-%H:%M")

for i in {1..3600}
do
  top -o -mem | head -n 25 >> toplog_${time}.txt
  echo $i
  sleep 1
done
