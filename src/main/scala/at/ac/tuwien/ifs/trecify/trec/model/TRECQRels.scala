package at.ac.tuwien.ifs.trecify.trec.model

/**
 * Created by aldo on 22/04/15.
 */

class TRECQRels(list:List[TRECQRel] = List[TRECQRel]()) {

  def :+(qrel:TRECQRel):TRECQRels = {
    new TRECQRels(list:+qrel)
  }

  def :+(num:String, docId:String, rel:String = null):TRECQRels = {
    :+(new TRECQRel(num, docId, rel))
  }

  override def toString():String = list.mkString("\n")

}

class TRECQRel(val num:String, val docId:String, val rel:String){

  override def toString():String = num + " 0 " + docId + " " + rel

}