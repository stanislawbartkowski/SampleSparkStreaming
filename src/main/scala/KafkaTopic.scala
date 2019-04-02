
import java.util.Properties

import scala.collection.JavaConversions._
import org.apache.kafka.clients.admin.{AdminClient, ListTopicsResult, NewTopic}

object KafkaTopic {

  def PARTITIONS : String = "partitions"
  def BOOTSTRAP : String = "bootstrap.servers"


  private def setoftopics(adminClient : AdminClient):  Set[String] = {
    val l: ListTopicsResult = adminClient.listTopics()
    l.names().get().toSet
  }

  def createTopic(prop : Properties, topic : String) = {
    println(BOOTSTRAP + prop.getProperty(BOOTSTRAP))
    println("I'm going to create adminClient " + topic)

    val adminClient = AdminClient.create(prop)
    println("OK, got it")
    println("List of topics available:")
    println("============")
    val topics : Set[String] = setoftopics(adminClient)
    topics.foreach(println)
    println("============")
    if (topics contains topic) println(topic + " topic already created, do nothing")
    else {
      println("I'm going to create topic " + topic)
      val numPartitions = prop.getProperty(PARTITIONS).toInt
      val replicationFactor :Short = 1
      val newTopic = new NewTopic(topic, numPartitions, replicationFactor)
      adminClient.createTopics(List(newTopic))
      println("Command executed, now wait unless emerges")
      var i : Int = 0
      while (( ! (setoftopics(adminClient) contains topics)) && (i < 10)) {
        // sleep 1 second
        Thread.sleep(1000)
        println(i)
        i = i+1
      }
    }

  }


}
