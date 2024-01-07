package com.example.todoapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.todoapp.model.MyRoomDatabase
import com.example.todoapp.repo.database.DatabaseRepository
import com.example.todoapp.repo.database.DatabaseRepositoryImpl
import com.example.todoapp.repo.tasks.TasksRepository
import com.example.todoapp.repo.tasks.TasksRepositoryImpl
import androidx.datastore.preferences.SharedPreferencesMigration
import com.example.todoapp.utils.Constant
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MyRoomDatabase =
        Room.databaseBuilder(
            context,
            MyRoomDatabase::class.java,
            Constant.TASKS_DATABASE
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDao(db: MyRoomDatabase) = db.taskDao()

    @Singleton
    @Provides
    fun provideDatabaseRepository(dbRepo: DatabaseRepositoryImpl): DatabaseRepository = dbRepo

    @Singleton
    @Provides
    fun provideTasksRepository(tasksRepositoryImpl: TasksRepositoryImpl): TasksRepository = tasksRepositoryImpl

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        val datastore = PreferenceDataStoreFactory.create(
            migrations = listOf(
                SharedPreferencesMigration(context, Constant.DATASTORE_NAME)
            ),
            scope = CoroutineScope(Dispatchers.Default)
        ) {
            context.preferencesDataStoreFile(Constant.DATASTORE_NAME)
        }
        return datastore
    }
}