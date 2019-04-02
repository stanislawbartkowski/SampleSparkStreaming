
import java.io.FileInputStream

import KafkaTopic._
import java.util.Properties
import KafkaStream._

object Main extends  App {

   def TOPIC = "topic"

   def printHelp = {
      println(" /what/ /prop/")
      println("   what: createtopic, pro : property file path")
      println("   what: runstream, pro : property file path")
      println("")
      println("Example:")
      println("Main createtopic ./kafka.properties")
      System.exit(4)
   }

   if (args.length != 2) printHelp

   val prop = new Properties()
   val proppath = args(1)
   prop.load(new FileInputStream(proppath))

   println("Hello")
   if (args.length != 2) {
      printHelp
   }
   val what : String = args(0)
   val topic = prop.getProperty(TOPIC)
   println(what)
   what match  {
      case "createtopic" =>  createTopic(prop,topic)
      case "runstream" => runStream(prop,topic)
      case _  => printHelp
   }
}
