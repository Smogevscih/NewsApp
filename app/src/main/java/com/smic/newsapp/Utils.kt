package com.smic.newsapp

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Smogevscih Yuri
01.09.2022
 **/
fun getDateAgo(daysAgo: Int = 0): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return dateFormat.format(calendar.time)
}

/**
 * Only for the following date format: yyyy-MM-dd'T'HH:mm:ss'Z'
 * Example 2022-09-01T02:33:00Z
 */
val String.getDateOfPublished
    get() = this.substringBefore("T")
