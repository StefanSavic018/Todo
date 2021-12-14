package com.stefan.todo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean) : Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
            SortOrder.BY_DATE_ASC -> getTasksSortedByDateCreatedASC(query, hideCompleted)
            SortOrder.BY_DATE_DESC -> getTasksSortedByDateCreatedDESC(query, hideCompleted)
        }

    @Query("SELECT * FROM taskTable WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC, name")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM taskTable WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY dateCreated ASC, dateCreated")
    fun getTasksSortedByDateCreatedASC(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM taskTable WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY dateCreated DESC, dateCreated")
    fun getTasksSortedByDateCreatedDESC(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM taskTable WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()

}