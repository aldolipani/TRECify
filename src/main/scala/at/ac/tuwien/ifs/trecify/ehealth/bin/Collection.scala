package at.ac.tuwien.ifs.trecify.ehealth.bin

import java.io.File

import at.ac.tuwien.ifs.trecify.clefip.model.CLEFIPCollection
import at.ac.tuwien.ifs.trecify.ehealth.model.{EHealthCollection, EHealthDocument}
import at.ac.tuwien.ifs.trecify.mediaeval.model.{MediaEvalCollection, MediaEvalDocument}
import at.ac.tuwien.ifs.trecify.trec.model._
import at.ac.tuwien.ifs.trecify.utility.Out

import scala.xml.XML

/**
 * Created by aldo on 22/04/15.
 */

//java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection Collection/ TRECCollection/
object Collection extends App {

  override def main(args: Array[String]): Unit = {
    val pathCollection = new File(args(0))
    val pathTRECCollection = args(1)

    val collection = new EHealthCollection(pathCollection.getAbsolutePath)
    val docs = collection.getStreamDocuments()

    var docIds: Set[String] = Set[String]()

    docs.map(doc => {
      val docSubPath = doc.file.getCanonicalPath.replace(pathCollection.getCanonicalPath, "")
      val docFile = new File(pathTRECCollection, docSubPath)
      val trecDoc = doc2TRECDoc(doc)
      println("> " + docFile.getCanonicalPath)
      Out.writeTextFile(docFile.getCanonicalPath, trecDoc.toString())
    }).toList
  }

  def doc2TRECDoc(doc: EHealthDocument): TRECWebDocument = {
    new TRECWebDocument(
      doc.file.getName,
      null,
      doc.html)
  }

}
