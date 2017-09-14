package com.ccorrads.kotlinfun.utils

import android.content.Context
import android.text.format.DateUtils
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    val ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ"

    /**
     * Return a date " @ " time formatted to the device's locale
     *
     * @param date DateTime
     * @return String, localized formatted date " @ " localized formatted time
     */
    fun formatDateAtTime(context: Context?, date: DateTime?): String? {
        return if (date == null) {
            null
        } else formatDate(context, date) + " @ " + formatTime(context, date)
    }

    /**
     * Return a time formatted to the device's locale
     *
     * @param date -- DateTime
     * @return String, localized formatted time
     */
    fun formatTime(context: Context?, date: DateTime?): String? {
        if (date == null) {
            return null
        }
        val localDateTime = date.toLocalDateTime()
        return DateUtils.formatDateTime(context, localDateTime.toDateTime().millis, DateUtils.FORMAT_SHOW_TIME)
    }

    /**
     * Return a date formatted to the device's locale
     *
     * @param date DateTime
     * @return String, localized formatted
     */
    fun formatDate(context: Context?, date: DateTime?): String? {
        if (date == null) {
            return null
        }
        val localDateTime = date.toLocalDateTime()
        return DateUtils.formatDateTime(context, localDateTime.toDateTime().millis, DateUtils.FORMAT_SHOW_DATE)
    }

    /**
     * Returns whether or not the two dates occur on the same day
     *
     * @param date1 -- DateTime
     * @param date2 -- DateTime
     * @return boolean, true if two dates are the same day
     */
    fun sameDate(date1: DateTime, date2: DateTime): Boolean {
        return date1.dayOfMonth == date2.dayOfMonth && date1.monthOfYear == date2.monthOfYear
                && date1.yearOfCentury == date2.yearOfCentury
    }

    /**
     * Return a date formatted to the devices local in xx/xx/xx format
     *
     * @param date DateTime
     * @return String, localized formatted numeric with slashes
     */
    fun formatNumericDate(date: DateTime?): String? {
        if (date == null) {
            return null
        }
        val localDateTime = date.toLocalDateTime()
        val format = SimpleDateFormat("M/dd/yy", Locale.getDefault())
        return format.format(localDateTime.toDateTime().millis)
    }
}