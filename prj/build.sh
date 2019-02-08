#! /bin/sh
#
# example:
# cd prj && ./build.sh
#
# このスクリプトはカレントディレクトリのprjディレクトリの下にあるjavaファイルをもとにjarファイルを生
# 成します。その後、/img/と/sound/, media.htmlを/bin/ディレクトリ下に移動させま
# す。また、その後のプロンプトでプログラムの実行を行うか選択します。

# build.sh が保存されているディレクトリに移動
cd `dirname $0`
pwd

# コンパイル
javac *.java

# javac コマンドが失敗していれば実行を停止する
if [ $? -ne 0 ]; then
    exit 1
fi

# jar ファイルの生成
jar cfm PegeonQuest.jar \
    manifest.txt \
    *.class \
    img/*.png img/*.jpg \
    sound/* \
    media.html

# move dir
cp PegeonQuest.jar ../

echo "Success: Will you execute PegeonQuest.jar file? [Y/n]"
read ANSWER

# プロンプトを表示し、生成した jar ファイルを実行するか尋ねる
case $ANSWER in
    "" | "Y" | "y" | "yes" | "Yes" | "YES" )
        echo "YES!! Execute: java -jar PegeonQuest.jar"
        java -jar ../PegeonQuest.jar
        ;;
    * )
        ;;
esac
