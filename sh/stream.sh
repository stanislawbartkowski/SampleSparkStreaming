export SPARK_MAJOR_VERSION=2
OPTIONS="--driver-java-options -Djava.security.auth.login.config=/etc/kafka/conf/kafka_client_jaas.conf"
spark-submit $OPTIONS --jars "/home/bench/.m2/repository/org/apache/spark/spark-streaming-kafka-0-10_2.11/2.3.0/spark-streaming-kafka-0-10_2.11-2.3.0.jar,/usr/hdp/current/kafka-broker/libs/*" --class Main --master yarn ../out/artifacts/SparkStreaming_jar/SparkStreaming.jar runstream kafka.properties
