package com.example.user.contactlistkotlin.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.user.contactlistkotlin.data.ContactRepository
import com.example.user.contactlistkotlin.data.local.AppDatabase
import com.example.user.contactlistkotlin.data.model.Contact
import com.example.user.contactlistkotlin.data.model.SingleEventLiveData
import java.util.*

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val database: AppDatabase = AppDatabase.getAppDatabase(application)

    val liveDataString: SingleEventLiveData<String>
    private val context:Context = application.applicationContext
    var contacts  = ArrayList<Contact>()

    private val repository: ContactRepository
    private val _contacts = MutableLiveData<List<Contact>>()
    val contactList: LiveData<List<Contact>>
        get() =_contacts

    private var start: Date? = null
    private lateinit var end: Date

    init {
        repository = ContactRepository(application, database)
        liveDataString = SingleEventLiveData()
    }

    private fun startLoading() {
        start = Calendar.getInstance().time
    }

    fun setup() {
        startLoading()
        val cursor = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNo =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                var photoUri =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                if (photoUri == null)
                    photoUri = ""
                val contact = Contact(name, phoneNo, photoUri)
                contacts.add(contact)
            }
            //use phone number as unique to remove duplicates
            _contacts.postValue(contacts.distinct())
            end = Calendar.getInstance().time
            val timeTaken = (end.time - start!!.time).toString() + " ms"
            Log.e("ContactViewModel", "<<<<<<<<===LoadContacts=>>>>>>>:${contacts.distinct().size} ^^ $timeTaken")
        }
        cursor?.close()
    }

    fun setLiveDataString(newString: String) {
        this.liveDataString.setValue(newString)
    }

    fun getLiveDataString(): LiveData<String> {
        return liveDataString
    }
}
