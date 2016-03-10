package at.ac.tuwien.ifs.trecify.clefip.bin

import java.io.File

import at.ac.tuwien.ifs.trecify.clefip.model._
import at.ac.tuwien.ifs.trecify.trec.model._

import scala.xml.XML

/**
 * Created by aldo on 22/04/15.
 */
object Topics extends App{

  override def main(args: Array[String]): Unit = {
    val pathCLEFIPTopics = args(0)
    val pwd = pathCLEFIPTopics.split(File.separator).init.mkString(File.separator)

    val clefIPTopics = CLEFIPTopics.fromFile(pathCLEFIPTopics)

    clefIPTopics.list.map(clefIPTopic => {
      //println(clefIPTopic.file)
      val trecTopic = clefIP2TRECTopic(pwd, clefIPTopic)
      if(trecTopic!=null) println(trecTopic)})
  }

  def clefIP2TRECTopic(pwd:String, clefIPTopic:CLEFIPTopic): TRECTopic = {
    val patent = XML.loadFile(new File(pwd, clefIPTopic.file))
    val title = (patent \\
      "invention-title").head.text
    val xmlAbss = (patent \\
      "abstract" filter { _ \ "@lang" exists (_.text == "EN")})

    if(xmlAbss.isEmpty) null else
    new TRECTopic(clefIPTopic.num, title, xmlAbss.head.text)
  }
}
