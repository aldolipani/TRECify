package at.ac.tuwien.ifs.trecify.mediaeval.model

import java.io.File

import scala.io.Source
import scala.xml.XML

/**
 * Created by aldo on 23/04/15.
 */
class MediaEvalTopics(val list:List[MediaEvalTopic]) {

  def +:(topic:MediaEvalTopic):MediaEvalTopics = {
    new MediaEvalTopics(list:+topic)
  }

  def +:(num:String, title:String):MediaEvalTopics = {
    +:(new MediaEvalTopic(num, title))
  }

  override def toString():String = list.mkString("\n")

}

class MediaEvalTopic(val number:String, val title:String){

  override def toString():String = {
    (<topic>
      <number>num</number>
      <title>title</title>
    </topic>).toString()
  }

}

object MediaEvalTopics {

  def fromFile(file:File):MediaEvalTopics = {
    new MediaEvalTopics(
      (XML.loadFile(file) \\ "topic").map(t =>
        new MediaEvalTopic((t \ "number").text.trim, (t \ "title").text.trim)
      ).toList
    )
  }

  def fromFile(path:String):MediaEvalTopics = {
    new MediaEvalTopics(
      (XML.loadFile(path) \\ "topic").map(t =>
        new MediaEvalTopic((t \ "number").text.trim, (t \ "title").text.trim)
      ).toList
    )
  }

  def fromString(str:String):MediaEvalTopics = {
    new MediaEvalTopics(
      (XML.loadString(str) \\ "topic").map(t =>
        new MediaEvalTopic((t \ "number").text.trim, (t \ "title").text.trim)
      ).toList
    )
  }

}