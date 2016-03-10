package at.ac.tuwien.ifs.trecify.trec.web.model


import java.io.File

import scala.io.Source
import scala.xml.XML

/**
 * Created by aldo on 23/04/15.
 */
class WebTopics(val list:List[WebTopic]) {

  def +:(topic:WebTopic):WebTopics = {
    new WebTopics(list:+topic)
  }

  def +:(num:String, query:String):WebTopics = {
    +:(new WebTopic(num, query))
  }

  override def toString():String = list.mkString("\n")

}

class WebTopic(val num:String, val query:String){

  override def toString():String = {
    ""
  }

}

/*
<topic number="50" type="ambiguous">
  <query>dog heat</query>
  <description>What is the effect of excessive heat on dogs?
  </description>
  <subtopic number="1" type="inf">
    What is the effect of excessive heat on dogs?
  </subtopic>
  <subtopic number="2" type="inf">
    What are symptoms of heat stroke and other heat-related illnesses
    in dogs?
  </subtopic>
  <subtopic number="3" type="inf">
    Find information on dogs' reproductive cycle.  What does it mean
    when a dog is "in heat"?
  </subtopic>
</topic>
 */

object WebTopics {

  def fromFile(file:File):WebTopics = {
    val str = Source.fromFile(file).getLines().mkString("\n")
    fromString(str)
  }

  def fromFile(path:String):WebTopics = {
    val str = Source.fromFile(path).getLines().mkString("\n")
    fromString(str)
  }

  def fromString(str:String):WebTopics = {
    val xmlText = str
    new WebTopics(
      (XML.loadString(xmlText) \\ "topic").map(t =>
        new WebTopic((t \ "@number").text, (t \ "query").text)
      ).toList
    )
  }

}