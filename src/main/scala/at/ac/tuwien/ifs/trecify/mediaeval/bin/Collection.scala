package at.ac.tuwien.ifs.trecify.mediaeval.bin

import java.io.File

import at.ac.tuwien.ifs.trecify.clefip.model._
import at.ac.tuwien.ifs.trecify.mediaeval.model.{MediaEvalDocument, MediaEvalCollection}
import at.ac.tuwien.ifs.trecify.trec.model._
import at.ac.tuwien.ifs.trecify.utility.Out

import scala.xml.XML
import scalaz.EphemeralStream

/**
 * Created by aldo on 22/04/15.
 */
/*mkdir TRECified/Collection/text_html/part2
java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection Collection/text_html/part2/ TRECified/Collection/text_html/part2
mkdir TRECified/Collection/text_html/part3
java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection Collection/text_html/part3/ TRECified/Collection/text_html/part3 &
mkdir TRECified/Collection/text_html/part4
java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection Collection/text_html/part4/ TRECified/Collection/text_html/part4 &
mkdir TRECified/Collection/text_html/part5
java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection Collection/text_html/part5/ TRECified/Collection/text_html/part5 &
mkdir TRECified/Collection/text_html/part6
java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection Collection/text_html/part6/ TRECified/Collection/text_html/part6 &
mkdir TRECified/Collection/text_html/part7
java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection Collection/text_html/part7/ TRECified/Collection/text_html/part7 &
mkdir TRECified/Collection/text_html/part8
java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.ehealth.bin.Collection Collection/text_html/part8/ TRECified/Collection/text_html/part8 &
*/

//java -cp /data/lipani/BM25Norm/bin/trecify.jar at.ac.tuwien.ifs.trecify.clefip.bin.Collection Collection/ TRECCollection/
object Collection extends App{

  override def main(args: Array[String]): Unit = {
    val pathCollection = new File(args(0))
    val pathTRECCollection = args(1)

    val collection = new MediaEvalCollection(pathCollection.getAbsolutePath)
    val docs = collection.getStreamDocuments()

    var docIds:Set[String] = Set[String]()

    docs.map(doc => {
      val docSubPath = doc.file.getCanonicalPath.replace(pathCollection.getCanonicalPath, "")
      val docFile = new File(pathTRECCollection, docSubPath)
      val trecDoc = doc2TRECDoc(doc)
      println("> " + docFile.getCanonicalPath.replace(".xml", ""))
      val nTrecDoc = trecDoc.filter(d => !docIds.contains(d.docno))
      docIds = docIds ++ nTrecDoc.map(_.docno).toSet
      Out.writeTextFile(docFile.getCanonicalPath.replace(".xml", ""), nTrecDoc.mkString("\n").toString())
    }).toList
  }

  def doc2TRECDoc(doc:MediaEvalDocument): List[TRECDocument] = {
    doc.content.map(u =>{
      val ndescription = try {
         XML.loadString("<description>" + u.description + "</description>").text
      }catch {
        case e:Exception => u.description
      }
      new TRECDocument(
        u.id,
        u.title,
        List(ndescription, u.tags).mkString("\n"))})
  }
}
