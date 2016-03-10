package at.ac.tuwien.ifs.trecify.ehealth.model;

import java.io.File
import at.ac.tuwien.ifs.trecify.model.{Document, Collection}

import scala.io.Source
import scala.xml.XML
import scalaz.EphemeralStream

/**
 * Created by aldo on 23/04/15.
 */
class EHealthCollection(filePath:String) extends Collection[EHealthDocument](filePath){

  override def createDocuments(file: File): List[EHealthDocument] = List(new EHealthDocument(file))

}

class EHealthDocument(val file:File) extends Document{
  lazy val html = {
    val o = scala.io.Source.fromFile(file)
    val lines = o.getLines().drop(5).mkString("\n")
    o.close()
    lines
  }
}