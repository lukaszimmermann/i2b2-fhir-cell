#!/bin/sh

cp -a /opt/data/* /tmp/volume
sync
rm -rf /opt/data
sync
