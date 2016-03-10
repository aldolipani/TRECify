package at.ac.tuwien.ifs.trecify.mediaeval.bin

import java.io.File

import at.ac.tuwien.ifs.trecify.clefip.model._
import at.ac.tuwien.ifs.trecify.mediaeval.model.{MediaEvalTopic, MediaEvalTopics}
import at.ac.tuwien.ifs.trecify.trec.model._

import scala.xml.XML

/**
 * Created by aldo on 22/04/15.
 */
object Topics extends App{

  override def main(args: Array[String]): Unit = {
    val pathTopics = args(0)
    val pwd = pathTopics.split(File.separator).init.mkString(File.separator)

    val topics = MediaEvalTopics.fromFile(pathTopics)

    topics.list.map(topic => {
      //println(topic.number)
      val trecTopic = topic2TRECTopic(pwd, topic)
      if(trecTopic!=null) println(trecTopic)})
  }

  def topic2TRECTopic(pwd:String, topic:MediaEvalTopic): TRECTopic = {
    new TRECTopic(topic.number, topic.title)
  }
}
