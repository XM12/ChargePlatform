package com.gaia.basic.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

const val PUBLIC_PILE_STORE_NAME = "public_pile_store"
const val FORUM_STORE_NAME = "forum_store"
const val PERSONAL_PILE_STORE_NAME = "personal_pile_store"
const val USER_STORE_NAME = "user_store"

val Context.publicStore: DataStore<Preferences> by preferencesDataStore(name = PUBLIC_PILE_STORE_NAME)
val Context.forumStore: DataStore<Preferences> by preferencesDataStore(name = FORUM_STORE_NAME)
val Context.personalStore: DataStore<Preferences> by preferencesDataStore(name = PERSONAL_PILE_STORE_NAME)
val Context.userStore: DataStore<Preferences> by preferencesDataStore(name = USER_STORE_NAME)

suspend fun <T : Any> put(dataStore: DataStore<Preferences>, key: String, value: T) {
    dataStore.edit {
        when (value) {
            is Int -> it[intPreferencesKey(key)] = value
            is Long -> it[longPreferencesKey(key)] = value
            is Double -> it[doublePreferencesKey(key)] = value
            is Float -> it[floatPreferencesKey(key)] = value
            is Boolean -> it[booleanPreferencesKey(key)] = value
            is String -> it[stringPreferencesKey(key)] = value
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }
}

suspend inline fun <reified T : Any> get(dataStore: DataStore<Preferences>, key: String): T {
    return when (T::class) {
        Int::class -> {
            dataStore.data.map {
                it[intPreferencesKey(key)] ?: 0
            }.first() as T
        }

        Long::class -> {
            dataStore.data.map {
                it[longPreferencesKey(key)] ?: 0L
            }.first() as T
        }

        Double::class -> {
            dataStore.data.map {
                it[doublePreferencesKey(key)] ?: 0.0
            }.first() as T
        }

        Float::class -> {
            dataStore.data.map {
                it[floatPreferencesKey(key)] ?: 0f
            }.first() as T
        }

        Boolean::class -> {
            dataStore.data.map {
                it[booleanPreferencesKey(key)] ?: false
            }.first() as T
        }

        String::class -> {
            dataStore.data.map {
                it[stringPreferencesKey(key)] ?: ""
            }.first() as T
        }

        else -> {
            throw IllegalArgumentException("This type can be get into DataStore")
        }
    }
}

/**
 * 清空数据
 */
fun DataStore<Preferences>.clear() = runBlocking { edit { it.clear() } }
