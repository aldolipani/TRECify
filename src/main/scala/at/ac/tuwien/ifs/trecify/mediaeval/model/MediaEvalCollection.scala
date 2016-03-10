package at.ac.tuwien.ifs.trecify.mediaeval.model

import java.io.File
import scala.xml.{Node, Elem, XML}
import scalaz.EphemeralStream

/**
 * Created by aldo on 23/04/15.
 */
class MediaEvalCollection(filePath:String) {

  lazy val file = new File(filePath)

  def getStreamDocuments(n:Int = -1):EphemeralStream[MediaEvalDocument] = {

    def next(files: EphemeralStream[File], n: Int): EphemeralStream[MediaEvalDocument] = {
      if (files.isEmpty || n == 0) {
        EphemeralStream.emptyEphemeralStream
      } else {
        EphemeralStream.cons(new MediaEvalDocument(files.headOption.get), next(files.tailOption.get, if (n != -1) n - 1 else -1))
      }
    }

    val files = getStreamFiles(file)//Filter(!_.getName.startsWith("."))
    next(files, n)
  }



  def getStreamFiles(file:File):EphemeralStream[File] = {
    def next(files:List[File]):EphemeralStream[File] ={
      if(files.isEmpty)
        EphemeralStream.emptyEphemeralStream
      else{
        if(files.head.isFile()){
          println("- " + files.head.getAbsolutePath)
          EphemeralStream.cons(files.head, next(files.tail))
        }else{
          println("d " + files.head.getAbsolutePath)
          next(files.head.listFiles().toList ::: files.tail)
        }
      }
    }

    next(file.listFiles().toList)
  }
}

class MediaEvalDocument(val file:File){
  lazy val content = getUnits(file)

  private def getUnits(file: File):List[MediaEvalUnit] = {
    (XML.loadFile(file) \ "photo").map(e => getUnit(e)).toList
  }

  private def getUnit(node: Node):MediaEvalUnit = new MediaEvalUnit(node)
}

class MediaEvalUnit(content:Node) {
  lazy val id = (content \ "@id").text.trim
  lazy val title = (content \ "@title").text.trim
  lazy val description = (content \ "@description").text.trim
  lazy val tags = (content \ "@tags").text.trim
}