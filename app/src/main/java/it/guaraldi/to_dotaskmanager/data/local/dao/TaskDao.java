package it.guaraldi.to_dotaskmanager.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.guaraldi.to_dotaskmanager.data.local.entities.Task;

/**
 * Created by sugfdo on 10/06/19.
 */

@Dao
public interface TaskDao {


    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM task")
    List<Task> getTasks();

    /**
     * * Select lastTaskId
     */
    @Query("SELECT id FROM task ORDER BY LENGTH(id) DESC, id DESC LIMIT 1")
    int getLastTaskId();

    @Query("SELECT COUNT(id) FROM task")
    int getSizeTableTasks();

    @Query("SELECT * FROM task WHERE CAST(start AS LONG) BETWEEN :startRange AND :endRange ORDER BY start ASC")
    List<Task> getUpcomingTasks(long startRange, long endRange);

    @Query("SELECT * FROM task ORDER BY start ASC")
    List<Task> getTasksByMonth();

    @Query("DELETE FROM task WHERE id = :taskId")
    void deleteTask(int taskId);

    @Query("UPDATE task SET status = :newStatus WHERE id = :taskId")
    void changeTaskStatus(String newStatus,int taskId);

    @Query("DELETE FROM task")
    void deleteAllTasks();

    @Query("SELECT * FROM task WHERE id=:taskId")
    Task getTaskById(int taskId);

    @Query("SELECT * FROM task WHERE category LIKE :categor ORDER BY status ASC")
    List<Task> getAllTaskByCategory(String categor);
    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param task the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

//
//    /**
//     * Select a task by id.
//     *
//     * @param taskId the task id.
//     * @return the task with taskId.
//     */
//    @Query("SELECT * FROM Tasks WHERE mId = :taskId")
//    Task getTaskById(String taskId);
//

//
//    /**
//     * Update a task.
//     *
//     * @param task task to be updated
//     * @return the number of tasks updated. This should always be 1.
//     */
//    @Update
//    int updateTask(Task task);
//
//    /**
//     * Update the complete status of a task
//     *
//     * @param taskId    id of the task
//     * @param completed status to be updated
//     */
////    @Query("UPDATE tasks SET completed = :completed WHERE mId = :taskId")
////    void updateCompleted(String taskId, boolean completed);
//
//    /**
//     * Delete a task by id.
//     *
//     * @return the number of tasks deleted. This should always be 1.
//     */
//    @Query("DELETE FROM Tasks WHERE mId = :taskId")
//    int deleteTaskById(String taskId);
//
//    /**
//     * Delete all tasks.
//     */
//    @Query("DELETE FROM Tasks")
//    void deleteTasks();
//
//    /**
//     * Delete all completed tasks from the table.
//     *
//     * @return the number of tasks deleted.
//     */
//    @Query("DELETE FROM Tasks WHERE completed = 1")
//    int deleteCompletedTasks();
}
