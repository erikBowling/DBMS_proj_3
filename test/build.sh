if [ -f test.class ]; then
    rm manager.class
fi

java -cp /usr/share/java/mysql-connector-j-8.0.32.jar Manager.java