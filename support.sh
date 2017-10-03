#!/bin/bash

function help {
  case $1 in
    cleanup)
      echo "Usage: $0 cleanup"
      echo "Cleans up release settings and pom backups from previous execution"
      ;;
    release)
      echo "Usage: $0 release"
      echo "Creates a new release version from the current snapshot, "
      echo "tags it in Git and rolls the current snapshot version to the next day"
    ;;
    version)
      echo "Usage: $0 version [newVersion]"
    ;;
    help)
      echo "Usage: $0 help [command]"
    ;;
    *)
      echo "Usage: $0 help [command]"
      echo "where \"command\" can be: cleanup, release, version, help"
      echo "For more information use: $0 help [command]"
    ;;
  esac
}

function prepare_release {
  TODAY=`date +%Y-%m-%d`
  TOMORROW=`date --date='tomorrow' +%Y-%m-%d`
  $MVN_CMD -Dtag=adele-$TODAY -DreleaseVersion=$TODAY -DdevelopmentVersion=$TOMORROW-SNAPSHOT release:prepare -B
}

if [ $# -ne 1 ] && [ $# -ne 2 ]; then
  echo "usage: $0 command [params]"
  echo "where \"command\" can be: cleanup, release, version, help"
  echo "For more information use: $0 help [command]"
  exit
fi

MVN_CMD=""

if hash mvn 2>/dev/null; then
  MVN_CMD="mvn";
else
  MVN_CMD="./mvnw"
fi

case $1 in
  cleanup)
    $MVN_CMD release:clean
    ;;
  release)
    prepare_release
  ;;
  version)
    if [ $# -eq 2 ]; then
      $MVN_CMD versions:set -DnewVersion=$2
    else
      help version
    fi
  ;;
  help)
    help $2
  ;;
  *)
    help
  ;;
esac
