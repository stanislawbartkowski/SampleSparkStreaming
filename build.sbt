name := "SparkStreaming"


version := "1.0"

scalaVersion := "2.11.6"

// https://mvnrepository.com/artifact/org.slf4j/slf4j-api
//libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.26"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.3.0"

// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.3.0"

// https://mvnrepository.com/artifact/org.apache.kafka/kafka
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.0.0"


// https://mvnrepository.com/artifact/org.apache.spark/spark-streaming-kafka-0-10
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.3.0"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"

















