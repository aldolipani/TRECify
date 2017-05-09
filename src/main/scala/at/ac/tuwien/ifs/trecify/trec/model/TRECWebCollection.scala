package at.ac.tuwien.ifs.trecify.trec.model

import java.io.File

import at.ac.tuwien.ifs.trecify.model.{Collection, Document}
import org.jsoup.Jsoup

/**
 * Created by aldo on 23/04/15.
 */
class TRECWebCollection(filePath:String) extends Collection[TRECWebDocument](filePath){

  override def createDocuments(file: File): List[TRECWebDocument] = {
    println(file.getAbsolutePath)
    val sFile = scala.io.Source.fromFile(file, "ISO8859_1")
    val ls = scala.collection.mutable.MutableList[TRECWebDocument]()
    for(l <- sFile.getLines()) {
      if (l.startsWith("<DOC>")) {
        if (ls.nonEmpty) {
          print("Discovered docid: " + ls.last.id + " ")
          println(ls.last.html.length)
        }
        ls += new TRECWebDocument(file)
      }
      ls.last.addLine(l)
    }
    ls.toList
  }

}

class TRECWebDocument(val file:File, val html:StringBuilder = new StringBuilder("")) extends Document{

  lazy val parsedText = Jsoup.parse(html.toString(), "UTF-8")

  lazy val id = parsedText.select("docno").first().text()

  lazy val text = {
    val body = parsedText
      .select("html > body").first()
      if (body != null)
        body.text()
      else ""
  }

  def addLine(line:String) = html.append("\n" + line);

}