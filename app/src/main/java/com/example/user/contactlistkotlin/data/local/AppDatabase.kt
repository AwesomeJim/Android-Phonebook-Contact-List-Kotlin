package com.example.user.contactlistkotlin.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.user.contactlistkotlin.data.model.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDao(): ContactDao

    companion object {

        private var instance: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder<AppDatabase>(context.applicationContext,
                        AppDatabase::class.java!!,
                        "app-db")
                        .allowMainThreadQueries()
                        .build()
            }
            return instance as AppDatabase
        }
    }
}
