package com.example.tareaalumnocurso.entidades;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "curso")
public class Curso {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;

    @ColumnInfo(name = "nombre")
    String nombre;

    public Curso(String nombre){
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
