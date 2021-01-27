package com.tr1984.smilecompetition.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Singleton

@Database(entities = [Smiling::class], version = 1)
@TypeConverters(Converters::class)
abstract class BePrettyDatabase : RoomDatabase() {

    abstract fun dao(): SmilingDao
}

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): BePrettyDatabase {
        return Room.databaseBuilder(
            appContext,
            BePrettyDatabase::class.java,
            "be_pretty.db"
        ).build()
    }

    @Provides
    fun provideDao(database: BePrettyDatabase): SmilingDao {
        return database.dao()
    }
}

@Dao
interface SmilingDao {

    @Query("SELECT * FROM smiling ORDER BY createdAt DESC")
    suspend fun getAll(): List<Smiling>

    @Query("SELECT * FROM smiling WHERE createdAt = :date LIMIT 1")
    suspend fun get(date: Date): Smiling?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(smiling: Smiling)
}