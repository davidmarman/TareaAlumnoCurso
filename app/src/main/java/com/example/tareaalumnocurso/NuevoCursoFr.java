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

import com.example.tareaalumnocurso.databinding.FragmentNuevoCursoBinding;
import com.example.tareaalumnocurso.entidades.Curso;
import com.example.tareaalumnocurso.viewmodel.CursoViewModel;


public class NuevoCursoFr extends Fragment {

    FragmentNuevoCursoBinding binding;
    CursoViewModel cursoViewModel;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentNuevoCursoBinding.inflate(inflater,container,false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cursoViewModel = new ViewModelProvider(requireActivity()).get(CursoViewModel.class);
        navController = Navigation.findNavController(view);

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = binding.etNombre.getText().toString();

                Curso c = new Curso(nombre);

                cursoViewModel.insertar(c);

                navController.popBackStack();
            }
        });
    }
}