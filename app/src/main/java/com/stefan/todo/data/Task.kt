package com.stefan.todo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "taskTable")
@Parcelize
public data class Task(
    val name: String,
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false,
    val dateCreated: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateInstance().format(dateCreated)

}