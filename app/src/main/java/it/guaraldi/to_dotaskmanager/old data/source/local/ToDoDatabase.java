package it.guaraldi.to_dotaskmanager.data.source.local;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;


import it.guaraldi.to_dotaskmanager.data.Task;

/**
 * Created by sugfdo on 11/06/19.
 */

@Database(entities = {Task.class},version = 2)
public abstract class ToDoDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "TaskDatabase.db";

    private static ToDoDatabase INSTANCE;

    public abstract TaskDao taskDao();

    private static final Object slock = new Object();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
            database.execSQL("CREATE TABLE IF NOT EXISTS `task` (`id` TEXT NOT NULL," +
                    "`title` TEXT NOT NULL,`email` TEXT NOT NULL,`all_day` BOOLEAN,`group_id` TEXT NOT NULL," +
                    "`priority` INTEGER NOT NULL,`category` TEXT NOT NULL,`status` TEXT NOT NULL," +
                    "`start` TEXT,`end` TEXT,`longitude` TEXT,`latitude` TEXT,`description` TEXT," +
                    "`color` TEXT NOT NULL,PRIMARY KEY (`id`,`email`))");
            database.execSQL("DROP TABLE `tasks`");
        }
    };

    public static ToDoDatabase getInstance(Context context){
        synchronized (slock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoDatabase.class, DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        return INSTANCE;
    }

}