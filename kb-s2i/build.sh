#!/usr/bin/env bash

cd /tmp/src

cp -rp -- /tmp/src/target/ds-image-analysis-*.war "$TOMCAT_APPS/ds-image-analysis.war"
cp -- /tmp/src/conf/ocp/ds-image-analysis.xml "$TOMCAT_APPS/ds-image-analysis.xml"

export WAR_FILE=$(readlink -f "$TOMCAT_APPS/ds-image-analysis.war")
