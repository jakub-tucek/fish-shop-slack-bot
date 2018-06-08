package service

import domain.MealType

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
    * Formats seq of ordered meals to string.
    *
    * @param orderedItems seq of ordered meals
    * @return formatted items as text
    */
  def formatOrderItems(orderedItems: Seq[MealType]): String =
    countOccurrence(orderedItems).toList
      .sortWith((t1, t2) => t1._1.mealId < t2._1.mealId)
      .map { case (mealType, valCount) => s"${mealType.mealId}(${getEmojiRepeated(mealType.emoji, valCount)})" }
      .mkString(", ")

  private def getEmojiRepeated(emoji: String, valCount: Int): String =
    (1 to valCount).map { _ => emoji }.mkString("")
}
