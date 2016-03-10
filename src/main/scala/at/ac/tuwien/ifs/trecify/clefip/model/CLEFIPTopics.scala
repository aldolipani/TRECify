package at.ac.tuwien.ifs.trecify.clefip.model

import java.io.File

import scala.io.Source
import scala.xml.XML

/**
 * Created by aldo on 23/04/15.
 */
class CLEFIPTopics(val list:List[CLEFIPTopic]) {

  def +:(topic:CLEFIPTopic):CLEFIPTopics = {
    new CLEFIPTopics(list:+topic)
  }

  def +:(num:String, narr:String, file:String):CLEFIPTopics = {
    +:(new CLEFIPTopic(num, narr, file))
  }

  override def toString():String = list.mkString("\n")

}

class CLEFIPTopic(val num:String, val narr:String, val file:String){

  override def toString():String = {
    (<topic>
      <num>num</num>
      <narr>narr</narr>
      <file>file</file>
    </topic>).toString()
  }

}

object CLEFIPTopics {

  def fromFile(file:File):CLEFIPTopics = {
    val str = Source.fromFile(file).getLines().mkString("\n")
    fromString(str)
  }

  def fromFile(path:String):CLEFIPTopics = {
    val str = Source.fromFile(path).getLines().mkString("\n")
    fromString(str)
  }

  def fromString(str:String):CLEFIPTopics = {
    val xmlText = "<topics>\n" + str + "\n</topics>"
    new CLEFIPTopics(
      (XML.loadString(xmlText) \\ "topic").map(t =>
        new CLEFIPTopic((t \ "num").text, (t \ "narr").text, (t \ "file").text)
      ).toList
    )
  }

}