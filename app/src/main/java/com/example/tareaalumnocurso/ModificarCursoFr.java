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
import android.widget.Toast;

import com.example.tareaalumnocurso.databinding.FragmentModificarCursoBinding;
import com.example.tareaalumnocurso.entidades.Curso;
import com.example.tareaalumnocurso.viewmodel.CursoViewModel;


public class ModificarCursoFr extends Fragment {

    private FragmentModificarCursoBinding binding;
    private CursoViewModel cursoViewModel;
    private NavController navController;

    private Curso cursoActual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentModificarCursoBinding.inflate(inflater,container,false)).getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cursoViewModel = new ViewModelProvider(requireActivity()).get(CursoViewModel.class);
        navController = Navigation.findNavController(view);

        cursoViewModel.getCursoSeleccionado().observe(getViewLifecycleOwner(), new Observer<Curso>() {
            @Override
            public void onChanged(Curso curso) {
                if(curso != null){
                    cursoActual = curso;

                    binding.etNombre.setText(cursoActual.getNombre());
                }
            }
        });


        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = binding.etNombre.getText().toString();

                //Validamos que el nombre no esta vacio
                if(nuevoNombre.isEmpty()){
                    Toast.makeText(getContext(),"El nombre no puede estar vacío",Toast.LENGTH_SHORT).show();
                    return;
                }

                cursoViewModel.actualizar(cursoActual,nuevoNombre);

                Toast.makeText(getContext(),"Curso actualizado correctamente",Toast.LENGTH_SHORT).show();

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
}