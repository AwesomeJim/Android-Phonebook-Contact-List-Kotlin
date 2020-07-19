package com.example.user.contactlist.data

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.user.contactlist.data.local.AppDatabase
import com.example.user.contactlist.data.model.Contact


class ContactRepository(private val context: Context, private val database: AppDatabase) {
    private val contactList = ArrayList<Contact>()
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>>
        get() = _contacts
        //get() = database.contactDao().allContacts

    fun saveContactsInDataBase() {
       // val contacts: List<Contact> = ArrayList()
        val cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if (cursor?.count ?: 0 > 0) {
            while (cursor!!.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                var photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                if(photoUri == null)
                    photoUri = ""
                val contact = Contact(name, phoneNo, photoUri)
                contactList.add(contact)
            }
            _contacts.value=contactList
        }
        cursor?.close()
    }
}
