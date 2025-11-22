package com.example.tareaalumnocurso.repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tareaalumnocurso.database.AlumnoCursoDB;
import com.example.tareaalumnocurso.database.CursoDao;
import com.example.tareaalumnocurso.entidades.Curso;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CursoRepositorio {
    CursoDao cursoDao;
    Executor executor = Executors.newSingleThreadExecutor();

    public CursoRepositorio(Application application){
        cursoDao = AlumnoCursoDB.obtenerInstancia(application).getCursoDao();
    }

    public LiveData<List<Curso>> obtener() {
        return cursoDao.getAllCursos();

    }

    public LiveData<Curso> obtenerById(int id) {
        return cursoDao.getCurso(id);

    }


    public void insertar(Curso c){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cursoDao.addCategoria(c);
            }
        });
    }

    public void eliminar(Curso curso) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cursoDao.deleteCurso(curso);
            }
        });
    }

    public void actualizar(Curso c, String nombre) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                c.setNombre(nombre);

                cursoDao.updateCurso(c);
            }
        });
    }

}
