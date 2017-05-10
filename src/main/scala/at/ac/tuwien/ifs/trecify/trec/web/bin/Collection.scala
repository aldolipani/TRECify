package at.ac.tuwien.ifs.trecify.trec.web.bin

import java.io.File

import at.ac.tuwien.ifs.trecify.clefip.model._
import at.ac.tuwien.ifs.trecify.ehealth.model.{EHealthDocument, EHealthCollection}
import at.ac.tuwien.ifs.trecify.trec.model._
import at.ac.tuwien.ifs.trecify.utility.Out

import scalaz.EphemeralStream

/**
 * Created by aldo on 22/04/15.
 */

//java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.trec.web.bin.Collection Collection/ TRECCollection/
object Collection extends App {

  override def main(args: Array[String]): Unit = {
    val pathCollection = new File(args(0))
    val pathTRECCollection = args(1)

    val collection = new TRECWebCollection(pathCollection.getAbsolutePath)
    val docs = collection.getStreamDocuments()

    docs.map(doc => {
      val docSubPath = doc.file.getParent.replace(pathCollection.getAbsolutePath, "")
      val trecDoc = doc2TRECDoc(doc)
      val docFile = new File(pathTRECCollection, docSubPath + File.separator + trecDoc.docno)
      val text = trecDoc.toString()
      println("> " + docFile.getCanonicalPath + ": " + text.length)
      Out.writeTextFile(docFile.getCanonicalPath, text)
    }).toList
  }

  def doc2TRECDoc(doc: TRECWebDocument): TRECDocument = {
    new TRECDocument(
      doc.id,
      null,
      doc.text)
  }

}
