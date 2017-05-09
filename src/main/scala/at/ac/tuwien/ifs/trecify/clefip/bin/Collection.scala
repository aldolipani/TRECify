package at.ac.tuwien.ifs.trecify.clefip.bin

import java.io.File

import at.ac.tuwien.ifs.trecify.clefip.model._
import at.ac.tuwien.ifs.trecify.trec.model._
import at.ac.tuwien.ifs.trecify.utility.Out

import scala.xml.XML
import scalaz.EphemeralStream

/**
 * Created by aldo on 22/04/15.
 */

//java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.clefip.bin.Collection Collection/ TRECCollection/
object Collection extends App{

  override def main(args: Array[String]): Unit = {
    val fileCLEFIPCollection = new File(args(0))
    val pathTRECCollection = args(1)

    val clefIPCollection = new CLEFIPCollection(fileCLEFIPCollection.getAbsolutePath)
    val docs = clefIPCollection.getStreamDocuments()

    //grouped(docs, 10000).map(ld => ld.par.map(clefIPDoc => { if (clefIPDoc.isEnglish()) {
    docs.map(clefIPDoc => { if (clefIPDoc.isEnglish()) {
      val docSubPath = clefIPDoc.file.getCanonicalPath.replace(fileCLEFIPCollection.getCanonicalPath, "")
      val docFile = new File(pathTRECCollection, docSubPath)
      val trecDoc = clefIP2TRECDoc(clefIPDoc)
      println("> " + docFile.getCanonicalPath.replace(".xml", ""))
      Out.writeTextFile(docFile.getCanonicalPath.replace(".xml", ""), trecDoc.toString())
    //}}).toList).toList
    }}).toList
  }

  def grouped(s: EphemeralStream[CLEFIPDocument], n:Int):EphemeralStream[List[CLEFIPDocument]] = {
    if(s.isEmpty)
      EphemeralStream.emptyEphemeralStream
    else{
      val l = s.take(n).toList
      EphemeralStream.cons(l, grouped(s.dropWhile(d => l.map(_.file.getCanonicalPath).toSet.contains(d.file.getCanonicalPath)), n))
    }
  }

  def clefIP2TRECDoc(clefIPDoc:CLEFIPDocument): TRECDocument = {
    val docno = clefIPDoc.getDocNo
    val title = clefIPDoc.getTitle
    val text =
      clefIPDoc.getDescription + "\n" +
      clefIPDoc.getClaims

    new TRECDocument(docno, title, text)
  }
}
