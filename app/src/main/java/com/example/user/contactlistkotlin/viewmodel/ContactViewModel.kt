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


    protected var phones: HashMap<Long, MutableList<String?>> = HashMap()

    init {
        repository = ContactRepository(application, database)
        liveDataString = SingleEventLiveData()
    }

    private fun startLoading() {
        start = Calendar.getInstance().time
    }

    fun setup() {
        startLoading()
        var cursor = context.applicationContext.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        Constants.PROJECTION_NUMBERS, null, null, null
        )
        if (cursor != null) {
            while (!cursor.isClosed && cursor.moveToNext()) {
                val contactId = cursor.getLong(0)
                val phone = cursor.getString(1)
                var list: MutableList<String?>?
                if (phones.containsKey(contactId)) {
                    list = phones[contactId]
                } else {
                    list = ArrayList()
                    phones[contactId] = list
                }
                list!!.add(phone)
            }
            cursor.close()
            cursor = context.applicationContext.contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                Constants.PROJECTION_DETAILS,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )
            if (cursor != null) {
                while (!cursor.isClosed && cursor.moveToNext()) {
                    val contactId = cursor.getLong(0)
                    val name = cursor.getString(1)
                    val contactPhones = phones[contactId]
                    contactPhones?.let {
                        for (phone in it) {
                            contacts.add(Contact(name, phone ?: "", ""))
                        }
                    }
                }
                cursor.close()
            }
            //use phone number as unique to remove duplicates
            _contacts.postValue(contacts)
            end = Calendar.getInstance().time
            val timeTaken = (end.time - start!!.time).toString() + " ms"
            Log.e("ContactViewModel", "<<<<<<<<===LoadContacts=>>>>>>>:${contacts.size} ^^ $timeTaken")
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
