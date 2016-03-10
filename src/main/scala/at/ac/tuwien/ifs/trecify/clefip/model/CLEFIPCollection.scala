package at.ac.tuwien.ifs.trecify.clefip.model

import java.io.File
import at.ac.tuwien.ifs.trecify.model.{Document, Collection}

import scala.xml.XML
import scalaz.EphemeralStream

/**
 * Created by aldo on 23/04/15.
 */
class CLEFIPCollection(filePath:String) extends Collection[CLEFIPDocument](filePath){

  override def createDocuments(file: File): List[CLEFIPDocument] = List(new CLEFIPDocument(file))

}

class CLEFIPDocument(val file:File) extends Document {
  lazy val content = XML.loadFile(file)

  def getDescription = (content \ "description").filter(c => (c \ "@lang").text == "EN").text

  def getClaims = (content \ "claims").filter(c => (c \ "@lang").text == "EN").map(_.text).mkString("\n")

  def getTitle = (content \\ "invention-title").filter(c => (c \ "@lang").text == "EN").text

  def getDocNo = (content \ "@ucid").head.text.trim

  def isEnglish():Boolean = {
    (content \\ "@lang").exists(e => (e.text == "EN"))
  }
}