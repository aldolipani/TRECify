package at.ac.tuwien.ifs.trecify.ehealth.model;

import java.io.File
import at.ac.tuwien.ifs.trecify.model.{Document, Collection}
import org.jsoup.Jsoup

import scala.io.Source
import scala.xml.{Node, XML}
import scalaz.EphemeralStream

/**
 * Created by aldo on 23/04/15.
 */
class EHealthCollection(filePath:String) extends Collection[EHealthDocument](filePath){

  override def createDocuments(file: File): List[EHealthDocument] = {
    val sFile = scala.io.Source.fromFile(file)
    val ls = scala.collection.mutable.MutableList[EHealthDocument]()
    for(l <- sFile.getLines().withFilter(_.nonEmpty)) {
      if (l.startsWith("#UID")) {
        if (ls.nonEmpty)
          println(ls.last.html.length)
        print("Discovered docid: " + l.drop(5) + " ")
        ls += new EHealthDocument(file, l.drop(5).trim())
      } else if (l.startsWith("#")) {
        ls
      } else {
        ls.last.addLine(l)
      }
    }
    ls.toList
  }

}

class EHealthDocument(val file:File, val id:String, val html:StringBuilder = new StringBuilder("")) extends Document{

  lazy val text = {
    val body = Jsoup.parse(html.toString(), "UTF-8").body()
      if (body != null)
        body.text()
      else ""
  }

  def addLine(line:String) = html.append("\n" + line);

}