package common.utils

case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)

  def hasNext(take: Int)= (offset + take > total)

  def rows(take: Int):String = items match {
    case Nil => """{"rows": [], "next": false, "total": 0}"""
    case _ => s"""{"rows": ${items.head}, "next": ${hasNext(take)}, "total": $total} """
  }
}

case class PageDbJson(items: String, total: Long, hasNext: Boolean){
  def toJson(): String =
    s"""{"rows": ${if (items == null) "[]" else items}, "next": $hasNext, "total": $total}"""
}
