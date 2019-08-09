package common.utils

import java.sql.Date

import org.joda.time.DateTime

object DateUtils {
  def sqlDate(date: DateTime): Date = new Date(date.getMillis)
}
