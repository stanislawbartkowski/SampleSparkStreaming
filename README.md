# SampleSparkStreaming

This sample demonstrates how to run Spark Kafka Streaming in Kerberos enabled HDP cluster.<br>
It is a simple Scala/Intellij project, the main issue is how to connect, prepare and read Spark Stream in a distribute environment protected by Kerberos.<br>

The test was conducted on HDP 2.6.5. <br>

# Test description

The Kafka stream should be created by another sample project (https://github.com/stanislawbartkowski/KafkaSample). Then *SampleSparkStreaming* application is digesting the stream created by *KafkaSample* in batches and producing output. The output is another Kafka topic, every message contains the number of messages in the latest batch. The application is very simple, comprises of several 

# Test 

Make sure that the *KafkaSample* application is running and producing stream of messages. <br>
