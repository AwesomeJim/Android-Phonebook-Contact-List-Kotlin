package com.example.user.contactlistkotlin.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.user.contactlistkotlin.data.ContactRepository
import com.example.user.contactlistkotlin.data.local.AppDatabase
import com.example.user.contactlistkotlin.data.model.Contact
import com.example.user.contactlistkotlin.data.model.SingleEventLiveData

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val database: AppDatabase = AppDatabase.getAppDatabase(application)
    val liveDataString: SingleEventLiveData<String>
    var contacts: LiveData<List<Contact>>? = null
        private set
    private val repository: ContactRepository

    init {
        repository = ContactRepository(application, database)
        liveDataString = SingleEventLiveData()
        repository.saveContactsInDataBase()
    }

    fun setup() {
        contacts = repository.contacts
    }

    fun setLiveDataString(newString: String) {
        this.liveDataString.setValue(newString)
    }

    fun getLiveDataString(): LiveData<String> {
        return liveDataString
    }
}
