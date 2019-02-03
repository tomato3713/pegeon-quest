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
    jar cfm PegeonQuest.jar manifest.txt *.class img/* sound/* media.html
    # move dir
    cp PegeonQuest.jar ../PegeonQuest

    echo "Success: Will you execute PegeonQuest.jar file? [Y/n]"
    read ANSWER

    case $ANSWER in
        "" | "Y" | "y" | "yes" | "Yes" | "YES" )
            echo "YES!! Execute: java -jar PegeonQuest.jar"
            cd ../PegeonQuest
            java -jar PegeonQuest.jar
            ;;
        * )
            ;;
    esac
fi
