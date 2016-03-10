package at.ac.tuwien.ifs.trecify.mediaeval.bin

import java.io.File

import at.ac.tuwien.ifs.trecify.mediaeval.model.{MediaEvalQRel, MediaEvalQRels, MediaEvalTopic, MediaEvalTopics}
import at.ac.tuwien.ifs.trecify.trec.model._

/**
 * Created by aldo on 22/04/15.
 */
object QRels extends App{

  override def main(args: Array[String]): Unit = {
    val pathQRels = args(0)
    val pathTopics = args(1)

    val qrels = MediaEvalQRels.fromFile(pathQRels)
    val topics = MediaEvalTopics.fromFile(pathTopics)

    val topicIds = topics.list.map(e => (e.title.trim() -> e.number)).toMap

    println(
      qrels.list.map(qrel => {
        val trecQRel = qrel2TRECQrel(qrel, topicIds)
        trecQRel}
      ).groupBy(_.head.num.toInt).toList.sortBy(_._1).flatMap(_._2).flatten.mkString("\n"))
  }

  def qrel2TRECQrel(qrel:MediaEvalQRel, topicsIds:Map[String, String]): List[TRECQRel] = {
    qrel.list.map(q => {
      //println(qrel.topicId)
      //try {
        new TRECQRel(topicsIds.get(qrel.topicId.trim()).get, q.docId, q.rel)
      //}catch{
      //  case e:Exception =>
      //    new File("/Users/aldo/Projects/Git/ADmIRE-7oZ7jntENqfRHZk/workspace/IRScoreAnalysis/input/MediaEval/MediaEval-2013/QRels/" + qrel.topicId+" rGT.txt").delete(); null
      //}
    })
  }
}
