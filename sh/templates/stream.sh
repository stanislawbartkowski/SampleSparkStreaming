export SPARK_MAJOR_VERSION=2
#DEBUG=-Dsun.security.krb5.debug=true
KAFKA_STREAM=lib/spark-streaming-kafka-0-10_2.11-2.3.0.jar
JAASFILE=jaas/kafka_jaas.conf
spark-submit  \
    --conf "spark.driver.extraJavaOptions=-Djava.security.auth.login.config=$JAASFILE $DEBUG" \
    --conf "spark.executor.extraJavaOptions=-Djava.security.auth.login.config=$JAASFILE $DEBUG" \
    --files jaas/kafka_jaas.conf#jaas/kafka_jaas.conf,jaas/bench.keytab#jaas/bench.keytab \
    --driver-java-options "-Djava.security.auth.login.config=$JAASFILE $DEBUG" \
    --jars "$KAFKA_STREAM,/usr/hdp/current/kafka-broker/libs/*" \
    --class Main --master yarn ../out/artifacts/SparkStreaming_jar/SparkStreaming.jar runstream kafka.properties
