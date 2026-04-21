package com.example.clase10;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Weather.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    //private static String database = "database-name";
    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, AppDatabase.class, "clima-db")
                    .setJournalMode(JournalMode.TRUNCATE)
                    .build();
                //*AppDatabase.class, database).build();


        }
        return instance;
    }
    public abstract WeatherDao weatherDao();
    public abstract UserDao userDao();

}
