package com.gaia.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gaia.db.dao.StationDao
import com.gaia.db.dao.UserDao
import com.gaia.db.entity.station.StationEntity
import com.gaia.db.entity.user.UserEntity

@Database(entities = [UserEntity::class, StationEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun createUserDao(): UserDao

    abstract fun createStationDao(): StationDao

}