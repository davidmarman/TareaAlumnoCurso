package com.example.tareaalumnocurso.entidades;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "alumno",
foreignKeys = @ForeignKey(entity = Curso.class,
parentColumns = "id",
childColumns = "idCurso",
onDelete = ForeignKey.CASCADE,
onUpdate = ForeignKey.CASCADE))
public class Alumno {

    public void setId(int id) {
        this.id = id;
    }
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int id;

    @NonNull
    int idCurso;

    @ColumnInfo(name = "nombre")
    String nombre;

    float nota;

    public int getId() {
        return id;
    }

    public Alumno(String nombre, float nota, int idCurso)
    {
        this.nombre = nombre;
        this.nota = nota;
        this.idCurso = idCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

}
