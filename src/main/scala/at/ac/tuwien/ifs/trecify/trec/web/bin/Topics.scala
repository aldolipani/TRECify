package at.ac.tuwien.ifs.trecify.trec.web.bin

import java.io.File
import at.ac.tuwien.ifs.trecify.trec.model.TRECTopic
import at.ac.tuwien.ifs.trecify.trec.web.model.{WebTopic, WebTopics}

import scala.xml.XML

/**
 * Created by aldo on 22/04/15.
 */
object Topics extends App{

  override def main(args: Array[String]): Unit = {
    val pathTopics = args(0)
    val pwd = pathTopics.split(File.separator).init.mkString(File.separator)

    val topics = WebTopics.fromFile(pathTopics)

    topics.list.map(topic => {
      //println(clefIPTopic.file)
      val trecTopic = clefIP2TRECTopic(pwd, topic)
      if(trecTopic!=null) println(trecTopic)})
  }

  def clefIP2TRECTopic(pwd:String, topic:WebTopic): TRECTopic = {
    new TRECTopic(topic.num, topic.query)
  }
}
