package com.example.badmintonai.model

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "logs")
@Parcelize
data class Log(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val logTitle: String,
    val logDesc: String,
    val videoPath: String
): Parcelable