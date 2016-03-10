package at.ac.tuwien.ifs.trecify.trec.model

/**
 * Created by aldo on 22/04/15.
 */

class TRECTopics(list:List[TRECTopic] = List[TRECTopic]()) {

  def :+(topic:TRECTopic):TRECTopics = {
    new TRECTopics(list:+topic)
  }

  def :+(num:String, title:String, desc:String = null, narr:String = null):TRECTopics = {
    :+(new TRECTopic(num, title, desc, narr))
  }

  override def toString():String = list.mkString("\n")

}

class TRECTopic(val num:String, val title:String, val desc:String = null, val narr:String = null){

  override def toString():String = {
    "<top>\n\n" +
    "<num> Number: " + num + "\n" +
    "<title> " + title + "\n\n" +
      (if(desc!=null)
        "<desc> Description: \n" +
        desc + "\n\n"
      else "") +
      (if(narr!=null)
        "<narr> Narrative: \n" +
        narr + "\n\n"
      else "") +
    "</top>\n"
  }

}