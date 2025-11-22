package com.example.tareaalumnocurso.repositorio;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.tareaalumnocurso.database.AlumnoCursoDB;
import com.example.tareaalumnocurso.database.AlumnoDao;
import com.example.tareaalumnocurso.entidades.Alumno;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AlumnoRepositorio {
    AlumnoDao alumnoDao;
    Executor executor = Executors.newSingleThreadExecutor();

    public AlumnoRepositorio(Application application){
        alumnoDao = AlumnoCursoDB.obtenerInstancia(application).getAlumnoDao();
    }

    public LiveData<List<Alumno>> obtener(){
        return alumnoDao.getAllAlumno();
    }

    public LiveData<List<Alumno>> obtenerAlumnoPorCurso(int id){
        return alumnoDao.getAlumnoByCurso(id);
    }

    public Alumno obtenerById(int id){
        return alumnoDao.getAlumno(id);
    }

    public void insertar(Alumno a){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alumnoDao.addAlumno(a);
            }
        });
    }

    public void eliminar(Alumno a){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alumnoDao.deleteAlumno(a);
            }
        });
    }

    public void actualizar(Alumno a){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alumnoDao.updateAlumno(a);
            }
        });
    }

}
