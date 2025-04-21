package com.example.mobile_a1.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Utility class for converting between Date and Long types for Room database.
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    companion object {
        fun dateToUserString(date: Date): String {
            // Format the date to a user-friendly string
            val today = Calendar.getInstance()
            val calendar = Calendar.getInstance()
            calendar.time = date

            // Helper function to check if two dates are on the same day
            fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
                return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                        cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                        cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
            }

            // Check if the date is today
            if (isSameDay(today, calendar)) {
                return "Hoy"
            }

            // Check if the date is tomorrow
            val tomorrow = Calendar.getInstance()
            tomorrow.add(Calendar.DAY_OF_MONTH, 1)
            if (isSameDay(tomorrow, calendar)) {
                return "Mañana"
            }

            // Otherwise, format the date to dd/MM/yyyy
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return formatter.format(date)
        }
    }
}
