package com.example.user.contactlist.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.user.contactlist.data.model.Contact

@Dao
interface ContactDao {

    @get:Query("SELECT * FROM contact")
    val allContacts: LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE phone_number = :phoneNo")
    fun contains(phoneNo: String): Contact

    @Insert(onConflict = REPLACE)
    fun insert(contact: Contact)

    @Delete
    fun delete(contact: Contact)

    @Update
    fun update(contact: Contact)
}
