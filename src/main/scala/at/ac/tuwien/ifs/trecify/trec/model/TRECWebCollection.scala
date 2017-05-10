package at.ac.tuwien.ifs.trecify.trec.model

import java.io.File

import at.ac.tuwien.ifs.trecify.model.{Collection, Document}
import org.jsoup.Jsoup
import org.jsoup.nodes.Node
import org.jsoup.parser.Parser

/**
 * Created by aldo on 23/04/15.
 */
class TRECWebCollection(filePath:String) extends Collection[TRECWebDocument](filePath){

  override def createDocuments(file: File): List[TRECWebDocument] = {
    println(file.getAbsolutePath)
    val sFile = scala.io.Source.fromFile(file, "ISO8859_1")
    val ls = scala.collection.mutable.MutableList[TRECWebDocument]()
    for(l <- sFile.getLines().withFilter(_.nonEmpty)) {
      if (l.startsWith("<DOC>")) {
        if (ls.nonEmpty) {
          print("Discovered docid: " + ls.last.id + " ")
          println(ls.last.html.length + " " + ls.last.parsedBody.nonEmpty)
        }
        ls += new TRECWebDocument(file)
        //}else if(l.trim.take(6).matches("^<[hH][tT][mM][lL][ >]")){
      }else if(ls.last.doc.takeRight(9).toString().matches("</DOCHDR>")){
        ls.last.addLineToHtml(l)
      }
      if (ls.last.html.isEmpty || l.startsWith("</DOC>"))
        ls.last.addLineToDoc(l)
      else
        ls.last.addLineToHtml(l)
    }
    ls.toList
  }

}

class TRECWebDocument(val file:File, val doc:StringBuilder = new StringBuilder(""), val html:StringBuilder = new StringBuilder("")) extends Document{

  lazy val parsedDoc = Jsoup.parse(doc.toString(), "ISO8859_1")

  lazy val parsedBody = {
    val stringHtml = html.toString()
    val doc = if("<[hH][tT][mM][lL][ >]".r.findFirstIn(stringHtml).isDefined) {
      print("html ")
      Jsoup.parse(stringHtml, "ISO8859_1")
    } else {
      print("something else ")
      Jsoup.parseBodyFragment(stringHtml, "ISO8859_1")
    }
    val body = doc.body()
    if(body != null) {
      val text = body.text()
      print(text.size + " ")
      text
    }else {
      print("empty ")
      ""
    }
  }

  lazy val id = parsedDoc.select("docno").first().text()

  lazy val text = parsedBody

  def addLineToDoc(line:String) = doc.append(" " + line.trim())

  def addLineToHtml(line:String) = html.append(" " + line.trim())

}