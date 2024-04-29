package com.pinyou.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pinyou.db.dao.StationDao
import com.pinyou.db.dao.UserDao
import com.pinyou.db.entity.station.StationEntity
import com.pinyou.db.entity.user.UserEntity

@Database(entities = [UserEntity::class, StationEntity::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun createUserDao(): UserDao

    abstract fun createStationDao(): StationDao

}