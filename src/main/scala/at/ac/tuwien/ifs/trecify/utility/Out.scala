package at.ac.tuwien.ifs.trecify.utility

import java.io._

/**
 * Created by aldo on 07/04/15.
 */
object Out {

  def writeTRECResult(p:String, runName:String, results: List[(String, String, Double)], cutoff:Int = 1000){
    val text = results
      .groupBy(_._1).toList.sortBy(_._1).flatMap(r => {
        val nr = r._2.take(cutoff)
        (1 to nr.size).zip(nr).map(rr => rr._2._1 + " 0 " + rr._2._2 + " " + rr._1  + " " + rr._2._3 + " " + runName)}).mkString("\n")
    writeTextFile(p, text)
  }

  def writeCSVFile(p:String, results:List[List[AnyVal]]) = {
    writeTextFile(p, results.map(_.mkString(";").replace(".", ",")).mkString("\n"))
  }

  def writeTextFile(p: String, s: String): Unit = {
    val file = new File(p)
    if(!file.getParentFile.exists())
      file.getParentFile.mkdirs()
    val fw = new FileWriter(file.getAbsolutePath)

    try {
      fw.write(s)
    }catch{
      case e:Exception => e.printStackTrace()
    }finally{
      fw.close()
    }
  }

}
