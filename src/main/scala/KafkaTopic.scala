
import java.util.Properties

import scala.collection.JavaConversions._
import kafka.admin.AdminUtils
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, ListTopicsResult, NewTopic}
//import kafka.utils.ZKStringSerializer
//import org.I0Itec.zkclient.ZkClient
//import kafka.utils.ZkUtils

object KafkaTopic {

  def ZOOKEEPER : String = "zookeeper"
  def TOPIC : String = "topic"
  def PARTITIONS : String = "partitions"
  def BOOTSTRAP : String = "bootstrap.servers"

/*
  def old_createTopic(zkUri : String,boostrap : String, topic : String, partitions : Int) = {
    println("Zookeeper " + zkUri )
    println("I'm going to check " + topic)
    val sessionTimeoutMs = 10000
    val connectionTimeoutMs = 10000
    val (zkClient, zkConnection) = ZkUtils.createZkClientAndConnection(zkUri, sessionTimeoutMs, connectionTimeoutMs)
    println("Connecting to Zookeeper, secure")
    val zkUtils = new ZkUtils(zkClient, zkConnection, true)
    println("Got it")
    val exist = AdminUtils.topicExists(zkUtils,topic)
    println(if (exist) "Exist" else "Sorry, does not exist")
    if (! exist) {
      println("If does not exist, let's create")
      AdminUtils.createTopic(zkUtils,topic,partitions,1)
    }
  }
*/

  private def setoftopics(adminClient : AdminClient):  Set[String] = {
    val l: ListTopicsResult = adminClient.listTopics()
    l.names().get().toSet
  }

  def createTopic(prop : Properties) = {
    val topic: String = prop.getProperty(TOPIC)
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
        println(i)
        i = i+1
      }
    }

  }


}
