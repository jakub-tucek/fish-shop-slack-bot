package service


object CommandHelper {

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
