package com.ccorrads.kotlinfun.serialization

import android.text.TextUtils
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.ccorrads.kotlinfun.utils.DateUtil
import org.joda.time.DateTime
import java.io.IOException

class DateTimeTypeAdapter : TypeAdapter<DateTime>() {

    @Throws(IOException::class)
    override fun read(reader: JsonReader): DateTime? {
        val dateStr = reader.nextString()
        return if (TextUtils.isEmpty(dateStr)) null else DateTime.parse(dateStr)
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, date: DateTime) {
        writer.value(date.toString(DateUtil.ISO_DATE_FORMAT))
    }

}
