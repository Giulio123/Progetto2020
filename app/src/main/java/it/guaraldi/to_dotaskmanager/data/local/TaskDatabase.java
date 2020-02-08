package it.guaraldi.to_dotaskmanager.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.local.dao.TaskDao;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;

import javax.inject.Singleton;


@Singleton
@Database(entities = Task.class, version = 1)
public abstract class TaskDatabase extends RoomDatabase{
    public abstract TaskDao getTaskDao();
}

