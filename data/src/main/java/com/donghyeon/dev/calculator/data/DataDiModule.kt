package com.donghyeon.dev.calculator.data

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataDiModule {
    @Provides
    fun providesDispatcher(): Dispatcher = AppDispatcher()

    private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore("Calculator")

    @Provides
    fun provideDataStore(application: Application): DataStore<Preferences> = application.applicationContext.dataStore
}
