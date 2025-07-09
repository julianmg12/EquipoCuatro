package com.example.equipocuatro.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.equipocuatro.model.Appointment
import androidx.room.Room
import android.content.Context
import com.example.equipocuatro.utils.Constants.NAME_BD

@Database(
    entities = [Appointment::class],
    version = 1
)
abstract class AppointmentDB : RoomDatabase() {
    abstract fun appointmentDao(): AppointmentDao
    companion object{
        fun getDatabase(context: Context): AppointmentDB {
            return Room.databaseBuilder(
                context.applicationContext,
                AppointmentDB::class.java,
                NAME_BD
            ).build()
        }
    }
}