package at.ac.tuwien.ifs.trecify.ehealth.model

import java.io.File

import scala.xml.XML

/**
 * Created by aldo on 23/04/15.
 */
class EHealthTopics(val list:List[EHealthTopic]) {

  def +:(topic:EHealthTopic):EHealthTopics = {
    new EHealthTopics(list:+topic)
  }

  def +:(id:String, discarge_summary:String, title:String, desc:String, narr:String, profile:String):EHealthTopics = {
    +:(new EHealthTopic(id, discarge_summary, title, desc, narr, profile))
  }

  override def toString():String = "<topics>\n"+list.mkString("\n")+"\n</topics>"

}

class EHealthTopic(val id:String, val discarge_summary:String, val title:String, desc:String, narr:String, profile:String){

  override def toString():String = {
    (<topic>
      <id>id</id>
      <discarge_summary>discarge_summary</discarge_summary>
      <title>title</title>
      <desc>desc</desc>
      <narr>narr</narr>
      <profile>profile</profile>
    </topic>).toString()
  }

}

object EHealthTopics {

  def fromFile(file:File):EHealthTopics = {
    fromString(scala.io.Source.fromFile(file).getLines().drop(1).mkString("\n"))
  }

  def fromFile(path:String):EHealthTopics =
    new EHealthTopics(
      (XML.loadFile(path) \\ "topic").map(t =>
        new EHealthTopic(
          (t \ "id").text.trim,
          (t \ "discarge_summary").text.trim,
          (t \ "title").text.trim,
          (t \ "desc").text.trim,
          (t \ "narr").text.trim,
          (t \ "profile").text.trim)
      ).toList
    )

  def fromString(str:String):EHealthTopics = {
    new EHealthTopics(
      (XML.loadString(str) \\ "topic").map(t =>
        new EHealthTopic(
          (t \ "id").text.trim,
          (t \ "discarge_summary").text.trim,
          (t \ "title").text.trim,
          (t \ "desc").text.trim,
          (t \ "narr").text.trim,
          (t \ "profile").text.trim)
      ).toList
    )
  }

}