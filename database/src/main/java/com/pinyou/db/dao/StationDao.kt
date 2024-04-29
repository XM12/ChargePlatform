package com.pinyou.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.pinyou.db.entity.station.StationEntity

@Dao
interface StationDao {

    //设置插入策略，如果存在就替换或更新
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: StationEntity): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<StationEntity>): List<Long>

    @Delete
    fun delete(entity: StationEntity): Int

    @Query("delete from charge_station where ID = :id")
    fun deleteById(id: String)

    @Query("delete from charge_station where ID in (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Update
    suspend fun update(entity: StationEntity): Int

    @Query("select * from charge_station where ID = :id")
    suspend fun queryById(id: String): List<StationEntity>

    @Query("select * from charge_station")
    suspend fun queryAll(): List<StationEntity>

    @RawQuery
    suspend fun queryBySql(sqLiteQuery: SimpleSQLiteQuery): List<StationEntity>

}