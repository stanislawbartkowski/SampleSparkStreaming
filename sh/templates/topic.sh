KERBEROS="-Djava.security.auth.login.config=/home/bench/jaas/kafka_user_jaas.conf -Dsun.security.krb5.debug=true"
KERBEROS="-Djava.security.auth.login.config=/home/bench/jaas/kafka_user_jaas.conf -Dsun.security.krb5.debug=true"
java $KERBEROS -cp /usr/hdp/current/kafka-broker/libs/*:/usr/hdp/current/spark2-client/jars/*:../out/artifacts/SparkStreaming_jar/SparkStreaming.jar Main createtopic kafka.properties
