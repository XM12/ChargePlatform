package com.gaia.db.entity.user

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserInfoEntity(
    val accessToken: String,
    val expireSeconds: Int,
    val grantType: String,
    val refreshToken: String,
    val user: UserEntity
)

@Entity(tableName = "app_user")
data class UserEntity(
    val accessAppId: String,
    @PrimaryKey
    val appUserId: String,
    val carNo: String,
    val carType: String,
    val loginDeviceCode: String,
    val mobile: String,
    val nickName: String,
    val realName: String,
    val sign: String,
    val theme: String,
    val userId: String,
    val userImg: String,
    val userType: String,
    val vin: String,
    val password: String
)