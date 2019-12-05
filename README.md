# SampleSparkStreaming

This sample demonstrates how to run Spark Kafka Streaming in Kerberos enabled HDP cluster.<br>
It is a simple Scala/Intellij project, the main issue is how to connect, prepare and read Spark Stream in a distribute environment protected by Kerberos. Problems like tunning, optimization, algorithm is not the concern.<br>
The application is very simple, the source code is available here (https://github.com/stanislawbartkowski/SampleSparkStreaming/tree/master/src/main/scala).

The test was conducted on HDP 2.6.5. <br>

# Test description

The Kafka stream should be created by another sample project (https://github.com/stanislawbartkowski/KafkaSample). The *SampleSparkStreaming* application is digesting the stream created by *KafkaSample* in batches and producing output. The output is another Kafka topic, every message contains the number of messages in the latest batch. 

# Configure SampleSparkStreaming for Kerberos

## Kafka ACL
In HDP and Kerberos enabled, the Kafka ACL feature is also activated. Make sure that the user running the test is authorized to create, read and write Kafka topics. It can be done in a friendly way with the help of Kafka Ranger plugin.<br>
<br>
## Kerberos keytab
Keytab containing user credentials should be at hand. It can be produced by Windows *ktpass* command or, in case of MIT Kerberos, *ktadd* request.
```
kadmin:  ktadd -k jaas/bench.keytab bench
Entry for principal bench with kvno 3, encryption type aes256-cts-hmac-sha1-96 added to keytab WRFILE:bench.keytab.
Entry for principal bench with kvno 3, encryption type aes128-cts-hmac-sha1-96 added to keytab WRFILE:bench.keytab.
kadmin:  
```
## JAAS file
Next step is to prepare *jaas* file and *keytab* (not ticket) authorization. <br>
Example:
```
KafkaClient {
   com.sun.security.auth.module.Krb5LoginModule required
   useKeyTab=true
   keyTab="jaas/bench.keytab"
   storeKey=true
   useTicketCache=false
   serviceName="kafka"
   principal="bench@FYRE.NET";
};
```
Make sure that proper *principal* is specified:<br>
> klist -kt jaas/bench.keytab 
```
Keytab name: FILE:jaas/bench.keytab
KVNO Timestamp           Principal
---- ------------------- ------------------------------------------------------
   3 28.04.2019 10:56:00 bench@FYRE.NET
   3 28.04.2019 10:56:00 bench@FYRE.NET
```
*keyTab* parameter should point to proper *keytab* file according to *spark-submit* command and *principal* is user principal name.<br>
## kafka.properties
Prepare *kafka.properties* file. Use *template/kafka.properties* as an example.
```
topic=test_input
partitions=20
bootstrap.servers=a1.fyre.ibm.com:6667,aa1.fyre.ibm.com:6667,hurds1.fyre.ibm.com:6667
batch.interval=10000
group.id=test-kafka
#local=X
topicout=test_output

security.protocol=SASL_PLAINTEXT
sasl.kerberos.service.name=kafka
```
The application is reading from *topic* (test_input) and writing to *topicout* (test_output). The parameter *topic* should have to same value as parameter in *KafkaSample* application.
## stream.sh
Prepare *stream.sh* command launching Sample Spark Streaming application. Use *template/stream.sh*.
```bash
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
```
For clarity, *jaas* file and *keytab* are in *jaas* subdirectory. 
# Run the test.
## Start KafkaSample producer
Make sure that parameter *topic* in *kafka.properties* is equal to *test_input*.<br>
>cd KafkaSample/sh<br>
> ./produce.sh
```
 ./produce.sh 
I'm producing lines to topic test_input unless stopped by CTRL/C ...
Key:0
Key:100
Key:200
Key:300
Key:400
Key:500
```
## Start Sample Spark Streaming consumer
>cd SampleSparkStreaming/sh<br>
>./stream.sh<br>
```
SPARK_MAJOR_VERSION is set to 2, using Spark2
Hello
runstream
I'm opening kafka topic test_output
19/04/04 20:29:30 INFO ProducerConfig: ProducerConfig values: 
	acks = 1
	batch.size = 16384
	bootstrap.servers = [a1.fyre.ibm.com:6667, aa1.fyre.ibm.com:6667, hurds1.fyre.ibm.com:6667]
	buffer.memory = 33554432
	client.id = 
	compression.type = none
	connections.max.idle.ms = 540000
	enable.idempotence = false
....
```
## Verify that Spark Streaming is running
>  /usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh --security-protocol PLAINTEXTSASL --bootstrap-server a1.fyre.ibm.com:6667,aa1.fyre.ibm.com:6667,hurds1.fyre.ibm.com:6667 --topic test_output
<br>

>  /usr/hdp/3.1.0.0-78/kafka/bin/kafka-console-consumer.sh --consumer-property security.protocol=SASL_PLAINTEXT  --topic output_topic -bootstrap-server mdp1:6667
<br>

```
0 :  Number of messages : 2535
1 :  Number of messages : 418
2 :  Number of messages : 439
3 :  Number of messages : 417

```
