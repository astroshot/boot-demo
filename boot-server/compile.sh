#!/bin/sh

set +x
set +e

get_properties() {
    cat target/maven-archiver/pom.properties | awk 'gsub(/^ *| *$/,"")' | grep -w $1 | awk -F '=' '{print $2}' | awk 'gsub(/^ *| *$/,"")'
}

_do() {
    "$@" || {
        echo "exec failed: ""$*"
        exit 1
    }
}

_do_ex() {
    eval "$1" || {
        echo "exec failed: ""$1"
        exit 1
    }
}

prepare() {
    workspace=$(pwd)
    if [ -f $workspace/conf/env.conf ]; then
        . $workspace/conf/env.conf
    else
        echo "project conf lost"
        exit 1
    fi

    project=${workspace//*\//}
    output="$workspace/output"
}

package() {
    _do rm -rf $output
    # _do mkdir $output
    _do cp src/main/resources/* conf/
    _do mvn -U clean package -Dmaven.test.skip=true
    # _do cp -R $workspace/target/output/* $output

    finalName=$(get_properties artifactId)-$(get_properties version).jar

    mv $workspace/target/output.jar $workspace/output/$finalName

    echo "project=$(get_properties artifactId)" >$workspace/output/info.txt
    echo "version=$(get_properties version)" >>$workspace/output/info.txt
}

main(){
    _do prepare
    _do package
}

main
