package at.ac.tuwien.ifs.trecify.mediaeval.model

import java.io.File

import scala.io.Source
import scala.xml.XML

/**
 * Created by aldo on 23/04/15.
 */
class MediaEvalQRels(val list:List[MediaEvalQRel]) {

  def +:(qrel:MediaEvalQRel):MediaEvalQRels = {
    new MediaEvalQRels(list:+qrel)
  }

  def +:(topic:String, list:List[MediaEvalRel]):MediaEvalQRels = {
    +:(new MediaEvalQRel(topic, list))
  }

  override def toString():String = list.mkString("\n")

}

class MediaEvalRel(val docId:String, val rel:String)

class MediaEvalQRel(val file:String, val list: List[MediaEvalRel]){
  lazy val topicId = file.split(" ").init.mkString(" ")
}

object MediaEvalQRels {

  def fromFile(file:File):MediaEvalQRels = new MediaEvalQRels(
    file.listFiles().filter(!_.getName.startsWith(".")).map(f =>{
      //println(f.getName)
      new MediaEvalQRel(f.getName,
        Source.fromFile(f).getLines()
          .map(l => {
          val a = l.split(',')
            if(a.size > 1)
              new MediaEvalRel(a(0), a(1))
            else
              null
          }).filter(_ != null).toList)}).toList)

  def fromFile(path:String):MediaEvalQRels = {
    fromFile(new File(path))
  }

}