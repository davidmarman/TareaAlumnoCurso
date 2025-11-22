package com.example.tareaalumnocurso.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tareaalumnocurso.entidades.Alumno;
import com.example.tareaalumnocurso.entidades.Curso;

@Database(entities = {Curso.class, Alumno.class}, version = 1)
public abstract class AlumnoCursoDB extends RoomDatabase{
    private static volatile AlumnoCursoDB INSTANCIA;

    public abstract AlumnoDao getAlumnoDao();

    public abstract CursoDao getCursoDao();

    public static AlumnoCursoDB obtenerInstancia(final Context context) {
        if (INSTANCIA == null) {
            synchronized (AlumnoCursoDB.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(context,
                                    AlumnoCursoDB.class, "categorias_tareas.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCIA;
    }

}
