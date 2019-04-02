#KERBEROS="-Djava.security.auth.login.config=/home/bench/jaas/kafka_user_jaas.conf -Dsun.security.krb5.debug=true"
#KERBEROS="-Djava.security.auth.login.config=/home/bench/jaas/kafka_user_jaas.conf"
KERBEROS=-Djava.security.auth.login.config=/etc/kafka/conf/kafka_client_jaas.conf
java $KERBEROS -cp /usr/hdp/current/kafka-broker/libs/*:/usr/hdp/current/spark2-client/jars/*:../out/artifacts/SparkStreaming_jar/SparkStreaming.jar Main createtopic kafka.properties
