
import java.io.FileInputStream

import KafkaTopic._
import java.util.Properties

object Main extends  App {

   def printHelp = {
      println(" /what/ /prop/")
      println("   what: createtopic, pro : property file path")
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
   println(what)
   what match  {
      case "createtopic" =>  createTopic(prop)
      case _  => printHelp
   }
}
