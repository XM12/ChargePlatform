package com.gaia.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gaia.db.entity.user.UserEntity

@Dao
interface UserDao {

    //设置插入策略，如果存在就替换或更新
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entity: UserEntity): List<Long>

    @Delete
    suspend fun delete(entity: UserEntity): Int

    @Query("delete from app_user where appUserId = :appUserId")
    suspend fun deleteById(appUserId: String)

    @Update
    suspend fun update(entity: UserEntity): Int

    @Query("update app_user set nickName = :userName where appUserId = :appUserId")
    suspend fun updateUserName(appUserId: String, userName: String)

    @Query("update app_user set password = :password where appUserId = :appUserId")
    suspend fun updatePassword(appUserId: String, password: String)

    @Query("select * from app_user where appUserId = :appUserId")
    suspend fun queryById(appUserId: String): List<UserEntity>

    @Query("select * from app_user")
    suspend fun queryAll(): List<UserEntity>

}