#! /bin/sh
#
# example:
# cd prj && ./build.sh
#
# このスクリプトはカレントディレクトリのprjディレクトリの下にあるjavaファイルをもとにjarファイルを生
# 成します。その後、/img/と/sound/, media.htmlを/bin/ディレクトリ下に移動させま
# す。また、その後のプロンプトでプログラムの実行を行うか選択します。

javac *.java

if [ $? -eq 0 ]; then
    jar cfm PegeonQuest.jar manifest.txt *.class

    # move dir
    cp PegeonQuest.jar ../bin
    cp -r img ../bin
    cp -r sound ../bin
    cp media.html ../bin

    echo "Success: Will you execute PegeonQuest.jar file? [Y/n]"
    read ANSWER

    case $ANSWER in
        "" | "Y" | "y" | "yes" | "Yes" | "YES" )
            echo "YES!! Execute: java -jar PegeonQuest.jar"
            cd ../bin
            java -jar PegeonQuest.jar
            ;;
        * )
            ;;
    esac
fi
