package com.android.myplacesandroid.di

import android.content.Context
import androidx.room.Room
import com.android.myplacesandroid.data.local.AppDatabase
import com.android.myplacesandroid.data.local.MyPlaceRepositoryImpl
import com.android.myplacesandroid.domain.repository.MyPlaceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()

    @Provides
    fun provideMyPlaceRepository(db: AppDatabase): MyPlaceRepository {
        return MyPlaceRepositoryImpl(db.myPlaceDao)
    }


}