package com.example.user.contactlistkotlin.data

import android.content.Context
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.user.contactlistkotlin.data.local.AppDatabase
import com.example.user.contactlistkotlin.data.model.Contact


class ContactRepository(private val context: Context, private val database: AppDatabase) {
    private val contactList = ArrayList<Contact>()
    private val _contacts = MutableLiveData<List<Contact>>()
/*    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)*/
    val contacts: LiveData<List<Contact>>
        get() = _contacts

    fun saveContactsInDataBase() {

        val cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
        if ((cursor?.count ?: 0) > 0) {
            while (cursor!!.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                var photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                if (photoUri == null)
                    photoUri = ""
                val contact = Contact(name, phoneNo, photoUri)
                contactList.add(contact)
            }
            //use phone number as unique to remove duplicates
            _contacts.value = contactList.distinctBy { it.phoneNumber }.toList()
        }
        cursor?.close()
    }

}
