package at.ac.tuwien.ifs.trecify.ehealth.bin

import java.io.File

import at.ac.tuwien.ifs.trecify.ehealth.model.{EHealthTopic, EHealthTopics}
import at.ac.tuwien.ifs.trecify.mediaeval.model.{MediaEvalTopic, MediaEvalTopics}
import at.ac.tuwien.ifs.trecify.trec.model._

/**
 * Created by aldo on 22/04/15.
 */
object Topics extends App{

  override def main(args: Array[String]): Unit = {
    val pathTopics = args(0)

    val topics = EHealthTopics.fromFile(pathTopics)

    topics.list.map(topic => {
      //println(topic.number)
      val trecTopic = topic2TRECTopic(topic)
      if(trecTopic!=null) println(trecTopic)})
  }

  def topic2TRECTopic(topic:EHealthTopic): TRECTopic = {
    new TRECTopic(topic.id, topic.title)
  }
}
