package com.example.tareaalumnocurso.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tareaalumnocurso.entidades.Alumno;

import java.util.List;

@Dao
public interface AlumnoDao {

    @Query("SELECT * FROM alumno")
    LiveData<List<Alumno>>getAllAlumno();

    @Query("SELECT * FROM alumno WHERE idCurso = :cursoId ORDER BY nombre ASC")
    LiveData<List<Alumno>> getAlumnoByCurso(int cursoId);

    @Query("SELECT * FROM alumno WHERE id LIKE :uuid")
    Alumno getAlumno(int uuid);

    @Insert
    void addAlumno(Alumno alumno);

    @Delete
    void deleteAlumno(Alumno alumno);

    @Update
    void updateAlumno(Alumno alumno);
}
