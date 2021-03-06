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

//java -Xmx20G -Xms10G -cp ~/bin/TRECify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection ./eHealth14/files TRECCollection/
object Collection extends App {

  override def main(args: Array[String]): Unit = {
    val pathCollection = new File(args(0))
    val pathTRECCollection = args(1)

    val collection = new EHealthCollection(pathCollection.getAbsolutePath)
    val docs = collection.getStreamDocuments()

    docs.map(doc => {
      val docSubPath = doc.file.getParent.replace(pathCollection.getCanonicalPath, "")
      val trecDoc = doc2TRECDoc(doc)
      val docFile = new File(pathTRECCollection, docSubPath + File.separator + trecDoc.docno)
      val text = trecDoc.toString()
      println("> " + docFile.getCanonicalPath + ": " + text.length)
      Out.writeTextFile(docFile.getCanonicalPath, text)
    }).toList
  }

  def doc2TRECDoc(doc: EHealthDocument): TRECDocument = {
    new TRECDocument(
      doc.id,
      null,
      doc.text)
  }

}
