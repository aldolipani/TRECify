package at.ac.tuwien.ifs.trecify.model

import java.io.File

import scalaz.EphemeralStream

/**
 * Created by aldo on 09/06/15.
 */
abstract class Collection[A <: Document](val filePath: String) {

  lazy val file = new File(filePath)

  def getStreamDocuments(n: Int = -1): EphemeralStream[A] = {
    def next(files: EphemeralStream[File], docs: List[A], n: Int): EphemeralStream[A] = {
      if(!docs.isEmpty){
        EphemeralStream.cons(docs.head, next(files, docs.tail, if (n != -1) n - 1 else -1))
      } else if (files.isEmpty || n == 0) {
        EphemeralStream.emptyEphemeralStream
      } else {
        try {
          val ds = createDocuments(files.headOption.get)
          if(!ds.isEmpty)
            EphemeralStream.cons(ds.head, next(files.tailOption.get, ds.tail, if (n != -1) n - 1 else -1))
          else
            next(files.tailOption.get, docs, n)
        }catch{
          case e:Exception => {
            println("Warning: " + e.getStackTrace)
            next(files.tailOption.get, docs, n)
          }
        }
      }
    }

    val files = getStreamFiles(file)
    next(files, Nil, n)
  }

  def createDocuments(file: File): List[A]

  def getStreamFiles(file: File): EphemeralStream[File] = {
    def next(files: List[File]): EphemeralStream[File] = {
      if (files.isEmpty)
        EphemeralStream.emptyEphemeralStream
      else {
        if (files.head.isFile()) {
          EphemeralStream.cons(files.head, next(files.tail))
        } else {
          next(files.head.listFiles().toList.filter(!_.getName.startsWith(".")) ::: files.tail)
        }
      }
    }

    next(file.listFiles().toList.filter(!_.getName.startsWith(".")))
  }

}

class Document
