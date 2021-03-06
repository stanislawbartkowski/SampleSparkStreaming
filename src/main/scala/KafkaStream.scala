import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import scala.collection.JavaConversions._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.LongDeserializer
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer


object KafkaStream {

  def BATCHINTERVAL = "batch.interval"
  def LOCAL="local"
  def APPNAME="Streaming test"

  def runStream(prop : Properties, topic : String): Unit = {
    println("Creating Spark context")
    prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[LongDeserializer].getName)
    prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    val conf = if (prop.containsKey(LOCAL)) new SparkConf().setMaster("local").setAppName(APPNAME)
    else new SparkConf().setAppName(APPNAME)
    val ssc = new StreamingContext(conf, Milliseconds(prop.getProperty(BATCHINTERVAL).toInt))
    println("Opening Direct Stream")
    val lines: DStream[ConsumerRecord[Long, String]] = {
      val i : InputDStream[ConsumerRecord[Long, String]] =
      KafkaUtils.createDirectStream[Long, String](ssc, PreferConsistent, Subscribe[Long,String](Set(topic),prop.toMap))
      i
    }
    println("Opened")
    val parsedLines : DStream[(Long,String)] = lines.map{ case (c) => (c.key(), c.value()) }
    parsedLines.foreachRDD(rdd => {
      println("================================= NEXT STREAM ==================")
      rdd.foreach( p => println(p._1 + " " + p._2))
      println("=================================")
      KafkaOutput.produce(" Number of messages : " + rdd.count())
    })
    println("Start streaming, waiting for input")
    ssc.start()
    ssc.awaitTermination()
  }

}
