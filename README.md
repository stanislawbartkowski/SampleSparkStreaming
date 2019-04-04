# SampleSparkStreaming

This sample demonstrates how to run Spark Kafka Streaming in Kerberos enabled HDP cluster.<br>
It is a simple Scala/Intellij project, the main issue is how to connect, prepare and read Spark Stream in a distribute environment protected by Kerberos. Problems like tunning, optimization, algorithm is not the concern.<br>
The application is very simple, the source code is available here (https://github.com/stanislawbartkowski/SampleSparkStreaming/tree/master/src/main/scala).

The test was conducted on HDP 2.6.5. <br>

# Test description

The Kafka stream should be created by another sample project (https://github.com/stanislawbartkowski/KafkaSample). The *SampleSparkStreaming* application is digesting the stream created by *KafkaSample* in batches and producing output. The output is another Kafka topic, every message contains the number of messages in the latest batch. 

# Configure SampleSparkStreaming for Kerberos

In HDP and Kerberos enabled, the Kafka ACL feature is also activated. Make sure that the user running the test is authorized to create, read and write Kafka topics. It can be done in a friendly way with the help of Kafka Ranger plugin.<br>








Make sure that the *KafkaSample* application is running and producing stream of messages 
