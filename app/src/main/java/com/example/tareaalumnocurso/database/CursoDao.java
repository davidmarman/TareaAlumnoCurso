package com.example.tareaalumnocurso.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tareaalumnocurso.entidades.Curso;

import java.util.List;

@Dao
public interface CursoDao {

    @Query("SELECT * FROM curso")
    LiveData<List<Curso>>getAllCursos();

    @Query("SELECT * FROM curso WHERE id = :uuid")
    LiveData<Curso> getCurso(int uuid);

    @Insert
    void addCategoria(Curso curso);

    @Delete
    void deleteCurso(Curso curso);

    @Update
    void updateCurso(Curso c);

}
