package com.example.todoapp.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constant {
    const val TASKS_DATABASE="tasksDatabase"
    const val TASKS_TABLE="task"
    const val DATASTORE_NAME = "filter status"

    const val APP_NAME = "Todo App"

    const val UPCOMING_TITLE = "Upcoming"
    const val PAST_DUE_TITLE = "Past Due"
    const val ALL_TASKS_TITLE = "All Tasks"

    val UPCOMING_KEY = booleanPreferencesKey(UPCOMING_TITLE)
    val PAST_DUE_KEY = booleanPreferencesKey(PAST_DUE_TITLE)
    val ALL_TASKS_KEY = booleanPreferencesKey(ALL_TASKS_TITLE)
}