package com.example.tareaalumnocurso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.tareaalumnocurso.databinding.FragmentModificarAlumnoBinding;
import com.example.tareaalumnocurso.entidades.Alumno;
import com.example.tareaalumnocurso.entidades.Curso;
import com.example.tareaalumnocurso.viewmodel.AlumnoViewModel;
import com.example.tareaalumnocurso.viewmodel.CursoViewModel;

import java.util.ArrayList;
import java.util.List;


public class ModificarAlumnoFr extends Fragment {

    private FragmentModificarAlumnoBinding binding;
    private NavController navController;
    private AlumnoViewModel alumnoViewModel;
    private CursoViewModel cursoViewModel;
    private Alumno alumnoActual;
    private List<Curso> listaCursos;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentModificarAlumnoBinding.inflate(inflater,container,false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alumnoViewModel = new ViewModelProvider(requireActivity()).get(AlumnoViewModel.class);
        cursoViewModel = new ViewModelProvider(requireActivity()).get(CursoViewModel.class);

        navController = Navigation.findNavController(view);

        //Cargamos la lista de los curtsos en el Spinner
        cargarCursosYAlumno();

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });


        binding.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });



    }

    private void cargarCursosYAlumno() {
        // Primero observamos los cursos para llenar el spinner
        cursoViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Curso>>() {

            @Override
            public void onChanged(List<Curso> cursos) {

                listaCursos = cursos;

                // Configurar adaptador del Spinner
                List<String> nombresCursos = new ArrayList<>();
                for (Curso c : cursos) {
                    nombresCursos.add(c.getNombre());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        nombresCursos
                );

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spCursos.setAdapter(adapter);

                cargarDatosAlumno();

            }
        });
    }

    private void cargarDatosAlumno() {
        alumnoViewModel.getAlumnoSeleccionado().observe(getViewLifecycleOwner(), new Observer<Alumno>() {
            @Override
            public void onChanged(Alumno alumno) {
                if (alumno != null) {
                    alumnoActual = alumno;

                    binding.etNombre.setText(alumno.getNombre());
                    binding.etNota.setText(String.valueOf(alumno.getNota()));

                    if (listaCursos != null) {
                        for (int i = 0; i < listaCursos.size(); i++) {
                            if (listaCursos.get(i).getId() == alumno.getIdCurso()) {
                                binding.spCursos.setSelection(i);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    private void guardarCambios() {
        String nuevoNombre = binding.etNombre.getText().toString();
        String nuevaNotaStr = binding.etNota.getText().toString();

        // Validaciones básicas
        if (nuevoNombre.isEmpty() || nuevaNotaStr.isEmpty()) {
            Toast.makeText(getContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        float nuevaNota;
        try {
            nuevaNota = Float.parseFloat(nuevaNotaStr);
            if (nuevaNota < 0 || nuevaNota > 10) {
                Toast.makeText(getContext(), "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Formato de nota inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID del nuevo curso seleccionado
        int posicionSpinner = binding.spCursos.getSelectedItemPosition();
        int idNuevoCurso = listaCursos.get(posicionSpinner).getId();

        // ACTUALIZAMOS EL OBJETO ALUMNO
        alumnoActual.setNombre(nuevoNombre);
        alumnoActual.setNota(nuevaNota);
        alumnoActual.setIdCurso(idNuevoCurso);

        alumnoViewModel.actualizar(alumnoActual);

        Toast.makeText(getContext(), "Alumno actualizado", Toast.LENGTH_SHORT).show();
        navController.popBackStack();
    }
}