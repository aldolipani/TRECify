package at.ac.tuwien.ifs.trecify.trec.model

import java.io.{FileInputStream, BufferedInputStream, File}
import java.nio.charset.CodingErrorAction
import java.util.zip.GZIPInputStream
import org.jsoup.Jsoup
import at.ac.tuwien.ifs.trecify.model.{Document, Collection}
import com.sun.xml.internal.ws.streaming.TidyXMLStreamReader

import scala.io.{Codec, Source}

/**
 * Created by aldo on 23/04/15.
 */
class TRECCollection(filePath:String) extends Collection[TRECDocument](filePath) {

  override def createDocuments(file: File): List[TRECDocument] =
    try {
      TRECDocument(file)
    } catch {
      case e:Exception => println("Warning: " + file.getAbsolutePath + "\n" + e.toString); Nil
    }

}

class TRECDocument(val path:String, val docno:String, val h3:String, val text:String) extends Document{

  override def toString():String = {
    "<DOC>\n\n" +
      "<DOCNO> " + docno + " </DOCNO>\n\n" +
      (if(h3!=null && h3.nonEmpty)
        "<H3> " + h3 + " </H3>\n\n"
      else "") +
      (if(text!=null && text.trim().nonEmpty)
        "<TEXT>\n" +
          text + "\n\n" +
        "</TEXT>\n\n"
      else "") +
      "</DOC>\n"
  }

}

object TRECDocument{

  def apply(file:File):List[TRECDocument] = {
    println(file.getName)
    val codec = Codec("UTF-8")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

    val (filePath, src) = (if(file.getName.endsWith(".gz")) {
      (file.getAbsolutePath.dropRight(3),
        Source.fromInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(file))))(codec))
      }else{
      (file.getAbsolutePath,
        Source.fromFile(file)(codec))
      })

    val xmlStr = src
      .getLines().mkString("\n")
      .replaceAll("[A-Z]+=[A-Za-z0-9\\-]+", "")
      .replaceAll("&([a-zA-Z];)?", "")

    val doc = Jsoup.parse(xmlStr)
    doc.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml)
    doc.outputSettings().escapeMode(org.jsoup.nodes.Entities.EscapeMode.xhtml)

    val node = scala.xml.XML.loadString(doc.toString)
    (node \\ "doc").map(d => {
      new TRECDocument(
        filePath,
      (d \ "docno").text.trim,
      (d \ "h3").text,
      (d \ "text").text)
    }).toList
  }

}

class TRECWebDocument(val docno:String, val dochdr:String = null, val html:String = null) {

  override def toString():String = {
      "<DOC>\n\n" +
        "<DOCNO> " + docno + " </DOCNO>\n\n" +
        (if(dochdr!=null && dochdr.nonEmpty)
          "<DOCHDR> " + dochdr + " </DOCHDR>\n\n"
        else
          "<DOCHDR>\n\n</DOCHDR>\n\n") +
        (if(html!=null && html.trim().nonEmpty)
          html + "\n\n"
        else "") +
      "</DOC>\n"
  }

}