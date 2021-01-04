package com.tr1984.smilecompetition.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Database(entities = [Smiling::class], version = 1)
@TypeConverters(Converters::class)
abstract class BePrettyDatabase : RoomDatabase() {

    abstract fun dao(): SmilingDao

    companion object {

        @Volatile
        private var instance: BePrettyDatabase? = null

        fun getInstance(context: Context): BePrettyDatabase {
            return instance ?: Room.databaseBuilder(
                context.applicationContext,
                BePrettyDatabase::class.java, "be_pretty.db"
            ).build().also { instance = it }
        }
    }
}

@Dao
interface SmilingDao {

    @Query("SELECT * FROM smiling ORDER BY createdAt DESC")
    suspend fun getAll(): List<Smiling>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(smiling: Smiling)
}