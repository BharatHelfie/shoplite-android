package com.shoplite.app.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shoplite.app.network.model.Review

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String?): List<String>? =
        gson.fromJson(value, object : TypeToken<List<String>>() {}.type)

    @TypeConverter
    fun fromReviews(value: List<Review>?): String? = gson.toJson(value)

    @TypeConverter
    fun toReviews(value: String?): List<Review>? =
        gson.fromJson(value, object : TypeToken<List<Review>>() {}.type)
}
