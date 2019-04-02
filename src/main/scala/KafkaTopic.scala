
import java.util.Properties

import scala.collection.JavaConversions._
import kafka.admin.AdminUtils
import org.apache.kafka.clients.admin.{AdminClient, AdminClientConfig, ListTopicsResult, NewTopic}
//import kafka.utils.ZKStringSerializer
import org.I0Itec.zkclient.ZkClient
import kafka.utils.ZkUtils

object KafkaTopic {

  def ZOOKEEPER : String = "zookeeper"
  def TOPIC : String = "topic"
  def PARTITIONS : String = "partitions"
  def BOOTSTRAP : String = "bootstrap"

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

  def createTopic(prop : Properties) = {
    val topic: String = prop.getProperty(TOPIC)
    println(ZOOKEEPER + prop.getProperty(ZOOKEEPER))
    println(BOOTSTRAP + prop.getProperty(BOOTSTRAP))
    println("I'm going to create adminClient " + topic)

    val adminClient = AdminClient.create(prop)
    println("OK, got it")
    //    val numPartitions = prop.getProperty(PARTITIONS).toInt
    //    val replicationFactor :Short = 1
    //    println("I'm going to check if " + topic + " exists" )
    //    val newTopic = new NewTopic(topic, numPartitions, replicationFactor)
    val l: ListTopicsResult = adminClient.listTopics()
    println("List of topics available")
    val topics : Set[String] = l.names().get().toSet
    topics.foreach(println)

  }


}
