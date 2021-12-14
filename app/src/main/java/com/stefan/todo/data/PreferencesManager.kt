package com.stefan.todo.data
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.IllegalArgumentException

private const val TAG = "PreferencesManager"

enum class SortOrder { BY_NAME, BY_DATE_ASC, BY_DATE_DESC }

data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

@Singleton
val Context.dataStore by preferencesDataStore("user_preferences")

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.dataStore

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                Log.e(TAG, "Error reading preferences: ",exception )
                emit(emptyPreferences())
            }
            else if (exception is IllegalArgumentException) {
                Log.e(TAG, "Error reading preferences: ", exception)
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(
                preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE_DESC.toString()
            )
            val hideCompleted = preferences[PreferencesKeys.HIDE_COMPLETED] ?: false
            FilterPreferences(sortOrder,hideCompleted)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }
    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sortOrder")
        val HIDE_COMPLETED = booleanPreferencesKey("hideCompleted")
    }
}