package service

/**
  * Helper function for usage in templates.
  *
  * @author Jakub Tucek
  */
object FishShopUtils {


  /**
    * Counts occurrences of items in sequence
    *
    * @param items items to count
    * @tparam T type of item
    * @return map where key is item from given sequence and value is it's occurrences
    */
  def countOccurrence[T](items: Seq[T]): Map[T, Int] = items.groupBy(identity).mapValues(_.size)


  /**
    * Formats seq of ordered items to string.
    *
    * @param orderedItems seq of ordered meals
    * @return formatted items as text
    */
  def formatOrderItems(orderedItems: Seq[Int]): String =
    countOccurrence(orderedItems).toList
      .sortWith((t1, t2) => t1._1 < t2._1)
      .map { case (keyItem, valCount) => s"$keyItem (${getEmojiRepeated(keyItem, valCount)})" }
      .mkString(", ")

  private def getEmojiRepeated(keyItem: Int, valCount: Int): String =
    (1 to valCount).map { _ => getEmoji(keyItem) }.mkString("")

  private def getEmoji(value: Int): String = value match {
    case 0 => ":ramen:"
    case 1 | 2 => ":fish:"
    case 3 | 4 => ":curry:"
    case 5 => ":green_salad:"
    case _ => ""
  }
}
