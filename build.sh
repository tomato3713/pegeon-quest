#! /bin/sh

javac *.java
jar cfm PegeonQuest.jar manifest.txt *.class

if [ $?  -eq 0  ]; then
    echo "Success to make jar file! : PegeonQuest.jar"

    echo "Will you execute this jar file? [Y/n]"
    read ANSWER

    case $ANSWER in
        "" | "Y" | "y" | "yes" | "Yes" | "YES" )
            echo "YES!! Execute: java -jar PegeonQuest.jar"
            java -jar PegeonQuest.jar
            ;;
        * )
            echo "NO!!"
            ;;
    esac

else
    echo "Failure to Made jar file!\njar cfm PegeonQuest.jar manifest.txt *.class *.java"
fi
