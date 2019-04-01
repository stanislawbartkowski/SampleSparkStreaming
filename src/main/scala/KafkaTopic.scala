
import kafka.admin.AdminUtils
//import kafka.utils.ZKStringSerializer
import org.I0Itec.zkclient.ZkClient
import kafka.utils.ZkUtils

object KafkaTopic {

  def createTopic(zkUri : String,topic : String) = {
     println("Create topic: " + topic)
    val sessionTimeoutMs = 10000
    val connectionTimeoutMs = 10000
    val (zkClient, zkConnection) = ZkUtils.createZkClientAndConnection(zkUri, sessionTimeoutMs, connectionTimeoutMs)
    val zkUtils = new ZkUtils(zkClient, zkConnection, false)
    println("Got it")
    val exist = AdminUtils.topicExists(zkUtils,topic)
    println(exist)
  }

}
