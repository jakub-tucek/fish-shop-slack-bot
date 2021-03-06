package service


/**
  * Helper function used when working with Command
  */
object CommandHelper {
  def getSeq(map: Map[String, Seq[String]], key: String): Seq[String] = map.get(key) match {
    case Some(seq) => seq
    case None => Seq.empty
  }


  //noinspection RedundantHeadOrLastOption
  def getHead(map: Map[String, Seq[String]], key: String): String =
    map.get(key) match {
      case Some(seq) => seq.headOption match {
        case Some(res) => res
        case None => ""
      }
      case None => ""
    }
}
