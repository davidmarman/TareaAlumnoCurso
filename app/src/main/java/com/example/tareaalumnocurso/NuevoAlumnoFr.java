package com.example.tareaalumnocurso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tareaalumnocurso.databinding.FragmentNuevoAlumnoBinding;
import com.example.tareaalumnocurso.entidades.Alumno;
import com.example.tareaalumnocurso.entidades.Curso;
import com.example.tareaalumnocurso.viewmodel.AlumnoViewModel;
import com.example.tareaalumnocurso.viewmodel.CursoViewModel;

import java.util.ArrayList;
import java.util.List;


public class NuevoAlumnoFr extends Fragment {

    FragmentNuevoAlumnoBinding binding;
    AlumnoViewModel alumnoViewModel;
    CursoViewModel cursoViewModel;
    private NavController navController;

    Spinner spinnerCurso;

    List<Curso> cursosPosibles;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentNuevoAlumnoBinding.inflate(inflater,container,false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alumnoViewModel = new ViewModelProvider(requireActivity()).get(AlumnoViewModel.class);
        cursoViewModel = new ViewModelProvider(requireActivity()).get(CursoViewModel.class);
        navController = Navigation.findNavController(view);

        configurarSpinner();

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = binding.etNombre.getText().toString();
                String notaString = binding.etNota.getText().toString();
                String cursoSeleccionado = binding.spCursos.getSelectedItem().toString();

                int idCurso = 0;

                // Validaciones de los campos
                if(nombre.isEmpty()){
                    Toast.makeText(getContext(),"Añade un Nombre",Toast.LENGTH_SHORT).show();
                }

                if(notaString.isEmpty()){
                    Toast.makeText(getContext(),"Añade una nota",Toast.LENGTH_SHORT).show();
                }

                // Validamos la nota
                float nota;
                try{
                    nota = Float.parseFloat(notaString);
                }catch (NumberFormatException e){
                    Toast.makeText(getContext(),"Formato de numero no valido",Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validamos que este entre cero y diez
                if(nota < 0 || nota > 10){
                    Toast.makeText(getContext(),"La nota tiene que estar entre 0 y 10",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cursosPosibles != null){
                    for(Curso c : cursosPosibles){
                        if(c.getNombre().equals(cursoSeleccionado)){
                            idCurso = c.getId();
                        }
                    }
                }
                Alumno a = new Alumno(nombre,nota,idCurso);
                alumnoViewModel.insertar(a);

                navController.popBackStack();
            }
        });

        binding.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });
    }


    private void configurarSpinner(){
        //Obtenemos el curso desde el view model compartido
        Curso cursoPreseleccionado = null;
        if(cursoViewModel.getCursoSeleccionado().getValue()!=null){
            cursoPreseleccionado = cursoViewModel.getCursoSeleccionado().getValue();
        }

        //Guardamos el id en una variable
        final Integer idCursoBloquear = (cursoPreseleccionado != null) ? cursoPreseleccionado.getId() : null;


        cursoViewModel.obtener().observe(getViewLifecycleOwner(),cursos -> {
            cursosPosibles = cursos;


            if (cursos == null || cursos.isEmpty()) {
                Toast.makeText(getContext(), "Debes crear un CURSO primero", Toast.LENGTH_LONG).show();
                binding.btnGuardar.setEnabled(false); // Desactivamos el botón guardar
                return; // Salimos para no intentar llenar el spinner
            } else {
                binding.btnGuardar.setEnabled(true); // Reactivamos si hay cursos
            }



            List<String> nombres = new ArrayList<>();
            for(Curso c : cursos){
                nombres.add(c.getNombre());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    nombres
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spCursos.setAdapter(adapter);

            // Si venimos de un curso especifico el id no es nulo
            if(idCursoBloquear != null){
                // Recorremos la lista para encontrar la posicion
                for(int i = 0;i< cursos.size(); i++){
                    if (cursos.get(i).getId() == idCursoBloquear){
                        //Seleccionamos el curso automaticamente
                        binding.spCursos.setSelection(i);

                        //Bloqueamos el spinner para que no se pueda tocar
                        binding.spCursos.setEnabled(false);

                        break;
                    }
                }
            }
        });
    }
}