#!/bin/bash


str=$(docker stats --no-stream `docker ps --format={{.Names}}` | grep -v CONTAINER)

# str: weavescope,6.18%,104.6,MiB,984.8,MiB,10.63%,0,B,0,B,71.1,MB,0,B,28
echo $str | sed "s/ \/ /,/g" | sed "s/ /,/g" \
    | awk -F ',' '{
            if ($4=="KiB") print $1","$2","$3/1024","$5","$6","$7
            if ($4=="MiB") print $1","$2","$3","$5","$6","$7
            if ($4=="GiB") print $1","$2","$3*1024","$5","$6","$7
        }' \
    | awk -F ',' '{
            if ($5=="KiB") print $1","$2","$3","$4/1024","$6
            if ($5=="MiB") print $1","$2","$3","$4","$6
            if ($5=="GiB") print $1","$2","$3","$4*1024","$6
        }' \
    >> dockerMonitor.csv