#! /bin/sh

javac *.java

if [ $? -eq 0 ]; then
    jar cfm PegeonQuest.jar manifest.txt *.class

    echo "Success: Will you execute PegeonQuest.jar file? [Y/n]"
    read ANSWER

    case $ANSWER in
        "" | "Y" | "y" | "yes" | "Yes" | "YES" )
            echo "YES!! Execute: java -jar PegeonQuest.jar"
            java -jar PegeonQuest.jar
            ;;
        * )
            ;;
    esac
fi
