import java.util.Properties

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import scala.collection.JavaConversions._


object KafkaStream {

  def BATCHINTERVAL = "batch.interval"

  def runStream(prop : Properties, topic : String): Unit = {
    println("Creating Spark context")
    val conf = new SparkConf().setAppName("Streaming Test")
    val ssc = new StreamingContext(conf, Milliseconds(prop.getProperty(BATCHINTERVAL).toInt))
    val lines: DStream[ConsumerRecord[String, String]] = {
      val i : InputDStream[ConsumerRecord[String, String]] =
      KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String,String](Set(topic),prop.toMap))
      i
    }
    val parsedLines : DStream[(Long,String)] = lines.map{ case (c) => (c.key().toLong, c.value()) }
    parsedLines.foreachRDD(rdd => {
      rdd.foreach( p => println(p._1 + " " + p._2))
    })
    println("Start streaming, waiting for input")
    ssc.start()
    ssc.awaitTermination()
  }

}
