package com.example.user.contactlist.data.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "contact")
class Contact(val name: String, @field:ColumnInfo(name = "phone_number")
val phoneNumber: String, @field:ColumnInfo(name = "photo_uri")
              val photoUri: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

