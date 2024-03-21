  #!/bin/bash

  time=(00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23)

  for((i=0;i<24;i++)); do
    echo "Hour ${time[i]}:"
    grep "2024-02-01 ${time[i]}.*" SMART_EIRID_1_20240201* | cut -d ',' -f15 | sort | uniq -c | awk '{print $1","$2}'

  done