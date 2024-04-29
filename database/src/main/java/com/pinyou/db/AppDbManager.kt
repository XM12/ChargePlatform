package com.pinyou.db

import android.app.Application
import androidx.room.Room
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class AppDbManager private constructor() {
    private var stationDb: AppDataBase? = null
    private var appDb: AppDataBase? = null

    companion object {
        private const val CHARGE_BAR_DB = "charge_bar.db"
        private const val CHARGE_BAR_STATION_DB = "charge_bar_station.db"

        val instance: AppDbManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            AppDbManager()
        }
    }

    /**
     * 初始化电站数据库和充电吧数据库
     * */
    fun init(context: Application) {
        appDb = Room.databaseBuilder(context, AppDataBase::class.java, CHARGE_BAR_DB)
            .setQueryExecutor(dbExecutor())
            .build()

        val builder = Room.databaseBuilder(context, AppDataBase::class.java, CHARGE_BAR_STATION_DB)
            .fallbackToDestructiveMigration()
        val stationDbFile = context.getDatabasePath(CHARGE_BAR_STATION_DB)
        if (!stationDbFile.exists()) {
            builder.createFromAsset("station.db")
        }
        builder.setQueryExecutor(dbExecutor())
        stationDb = builder.build()
    }

    /**
     * 设置查询线程池
     * 策略：采用一个核心线程和长度只有一个的等待队列，核心线程被占用时，任务添加并更新到等待队列，并清除等待队列未执行的任务
     * */
    private fun dbExecutor(): Executor {
        val queue = ArrayBlockingQueue<Runnable>(1)
        val policy = ThreadPoolExecutor.DiscardOldestPolicy()
        return ThreadPoolExecutor(
            1,
            1,
            0,
            TimeUnit.SECONDS,
            queue,
            policy
        )
    }

    fun getStationDb(): AppDataBase {
        stationDb?.let {
            return it
        } ?: let {
            throw RuntimeException("charge_bar_station database not init")
        }
    }

    fun getAppDb(): AppDataBase {
        appDb?.let {
            return it
        } ?: let {
            throw RuntimeException("charge_bar database not init")
        }
    }

}