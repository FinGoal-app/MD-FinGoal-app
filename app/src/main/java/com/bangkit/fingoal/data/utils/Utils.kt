package com.bangkit.fingoal.data.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

private const val TIME_FORMAT = "HH:mm"
private const val TIMESTAMP_FORMAT = "dd MMM yyyy"
private const val DATE_FORMAT = "dd/MM/yyyy"
private const val DATE_PICKER_FORMAT = "yyyy-MM-dd"
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private const val CURRENCY_FORMAT = "#,###"
private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun getImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri!!
}

@SuppressLint("NewApi")
fun formatDate(date: String): String {
    val dateFormatter = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT, Locale("id", "ID"))
        .withZone(ZoneId.of("Asia/Jakarta"))
    return dateFormatter.format(Instant.parse(date))
}

@SuppressLint("NewApi")
fun formatDateForUpload(date: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale("id", "ID"))
    val outputFormatter = DateTimeFormatter.ofPattern(DATE_PICKER_FORMAT, Locale("id", "ID"))
    val localDate = LocalDate.parse(date, inputFormatter)
    return outputFormatter.format(localDate)
}

@SuppressLint("NewApi")
fun formatDatePicker(selectedDate: String): String {
    val timestamp = selectedDate.toLong()
    val instant = Instant.ofEpochMilli(timestamp)

    val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        .withZone(ZoneId.of("Asia/Jakarta"))

    return formatter.format(instant)
}

@SuppressLint("NewApi")
fun formatTime(date: String): String {
    val timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT, Locale("id", "ID"))
        .withZone(ZoneId.of("Asia/Jakarta"))
    return timeFormatter.format(Instant.parse(date))
}

@SuppressLint("NewApi")
fun calculateMonthDifference(startDate: String, targetDate: String): String {
    val startLocalDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
    val targetLocalDate = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

    val monthsDifference = ChronoUnit.MONTHS.between(startLocalDate, targetLocalDate)

    return "in $monthsDifference month${if (monthsDifference > 1) "s" else ""}"
}

fun formatRupiah(amount: Double): String {
    val formatter = DecimalFormat(CURRENCY_FORMAT, DecimalFormatSymbols(Locale("id", "ID")))
    return "Rp" + formatter.format(amount)
}