package com.android.myplacesandroid.di

import android.app.Application
import androidx.room.Room
import com.android.myplacesandroid.data.local.AppDatabase
import com.android.myplacesandroid.data.local.MyPlaceRepositoryImpl
import com.android.myplacesandroid.domain.repository.MyPlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "database.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMyPlaceRepository(db: AppDatabase): MyPlaceRepository {
        return MyPlaceRepositoryImpl(db.myPlaceDao)
    }
}