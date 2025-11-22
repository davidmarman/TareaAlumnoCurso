package com.example.tareaalumnocurso.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tareaalumnocurso.entidades.Curso;
import com.example.tareaalumnocurso.repositorio.CursoRepositorio;

import java.util.List;

public class CursoViewModel extends AndroidViewModel {

    CursoRepositorio cursoRepositorio;

    MutableLiveData<Curso>cursoSeleccionado = new MutableLiveData<>();
    public CursoViewModel(@NonNull Application application) {
        super(application);

        cursoRepositorio = new CursoRepositorio(application);
    }

    public LiveData<List<Curso>> obtener(){
        return cursoRepositorio.obtener();
    }

    public LiveData<Curso> obtenerById(int id){
        return cursoRepositorio.obtenerById(id);
    }

    void insertar(Curso c){
        cursoRepositorio.insertar(c);
    }

    void eliminar(Curso c){
        cursoRepositorio.eliminar(c);
    }

    void actualizar(Curso c, String nombre){
        cursoRepositorio.actualizar(c,nombre);
    }

    public void setCursoSeleccionado(Curso c){
        cursoSeleccionado.setValue(c);
    }

    public MutableLiveData<Curso>getCursoSeleccionado(){
        return cursoSeleccionado;
    }
}
