package com.example.tareaalumnocurso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tareaalumnocurso.entidades.Alumno;
import com.example.tareaalumnocurso.entidades.Curso;
import com.example.tareaalumnocurso.repositorio.AlumnoRepositorio;

import java.util.List;

public class AlumnoViewModel extends AndroidViewModel {

    AlumnoRepositorio alumnoRepositorio;

    MutableLiveData<Alumno>alumnoSeleccionado = new MutableLiveData<>();
    MutableLiveData<Integer>cursoPadreSeleccionado = new MutableLiveData<>();

    public AlumnoViewModel(@NonNull Application application) {
        super(application);
        alumnoRepositorio = new AlumnoRepositorio(application);
    }

    public LiveData<List<Alumno>> obtener(){
        return alumnoRepositorio.obtener();
    }

    public LiveData<List<Alumno>> obtenerAlumnoByCurso(int id){
        return alumnoRepositorio.obtenerAlumnoPorCurso(id);
    }

    public Alumno obtenerAlumno(int id){
        return alumnoRepositorio.obtenerById(id);
    }

    void insertar(Alumno a){
        alumnoRepositorio.insertar(a);
    }

    void eliminar(Alumno a){
        alumnoRepositorio.eliminar(a);
    }

    void actualizar(Alumno a){
        alumnoRepositorio.actualizar(a);
    }

    public void setAlumnoSeleccionado(Alumno a) {
        alumnoSeleccionado.setValue(a);
    }

    MutableLiveData<Alumno>getAlumnoSeleccionado(){
        return alumnoSeleccionado;
    }

    public void setCursoPadreSeleccionado(Curso c){
        cursoPadreSeleccionado.setValue(c.getId());
    }

    MutableLiveData<Integer>getCursoPadreSeleccionado(){
        return cursoPadreSeleccionado;
    }

}
