import java.util.Properties

import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.clients.producer.ProducerRecord


object KafkaOutput {

  private var key : Long = 0
  var produce : KafkaProducer[Long,String] = null
  var topic : String = null

  def openoutput(prop : Properties, topic : String) : Unit = {

    this.topic = topic
    prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName)
    prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    println("I'm opening kafka topic " + topic)
    produce = new KafkaProducer[Long,String](prop)
  }

  def produce(value : String): Unit = {
    println(value)
    // key - the number is not consecutive in distributed environment
    val record = new ProducerRecord[Long, String](topic, key, key + " : " + value)
    key += 1
    produce.send(record)
  }

}
