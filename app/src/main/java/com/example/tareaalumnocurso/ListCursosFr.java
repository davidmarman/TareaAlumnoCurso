package com.example.tareaalumnocurso;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tareaalumnocurso.databinding.CursoViewHolderBinding;
import com.example.tareaalumnocurso.databinding.FragmentListCursosBinding;
import com.example.tareaalumnocurso.entidades.Curso;
import com.example.tareaalumnocurso.viewmodel.AlumnoViewModel;
import com.example.tareaalumnocurso.viewmodel.CursoViewModel;

import java.util.List;


public class ListCursosFr extends Fragment {

    private CursoViewModel cursoViewModel;
    private NavController navController;
    private AlumnoViewModel alumnoViewModel;
    FragmentListCursosBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding= FragmentListCursosBinding.inflate(inflater,container,false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //View model compartido
        cursoViewModel = new ViewModelProvider(requireActivity()).get(CursoViewModel.class);
        alumnoViewModel = new ViewModelProvider(requireActivity()).get(AlumnoViewModel.class);
        navController = Navigation.findNavController(view);

        //Navegar a nuevo curso cuando se hace click en el floatting button
        binding.btnNuevoCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoCursoFr);
            }
        });

        //Navegar a nuevo alumno
        binding.btnNuevoAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursoViewModel.setCursoSeleccionado(null);
                navController.navigate(R.id.nuevoAlumnoFr);
            }
        });

        // Creamos el adaptador
        CursoAdapter cursoAdapter = new CursoAdapter();

        //Asociar el adaptador al recyclerView
        binding.recyclerView.setAdapter(cursoAdapter);

        //Obtener el array de elementos
        cursoViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Curso>>() {
            @Override
            public void onChanged(List<Curso> cursos) {
                cursoAdapter.establecerLista(cursos);
            }
        });


    }

    /************************************************************************************
     * CLASE ADAPTADOR
     ***********************************************************************************/
    class CursoAdapter extends RecyclerView.Adapter<CursoViewHolder>
    {

        List<Curso> cursoList;

        @NonNull
        @Override
        public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.curso_view_holder,parent,false);
            return new CursoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {

            Curso c =cursoList.get(position);

            holder.txtNombreCurso.setText(c.getNombre());


            //implementar la pulsación en el reciclerView
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alumnoViewModel.setCursoPadreSeleccionado(c);
                    cursoViewModel.setCursoSeleccionado(c);
                    navController.navigate(R.id.listAlumnosFr);
                }
            });





        }

        @Override
        public int getItemCount() {
            return cursoList != null ? cursoList.size() : 0;

        }

        public void establecerLista(List<Curso> cursos) {
            this.cursoList = cursos;
            notifyDataSetChanged();
        }

        public Curso obtenerElemento(int posicion)
        {
            return cursoList.get(posicion);
        }
    }


    /***********************************************************************************
     *  CLASE HOLDER
     **********************************************************************************/
    class CursoViewHolder extends RecyclerView.ViewHolder
    {
        TextView txtNombreCurso;

        public CursoViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtNombreCurso = itemView.findViewById(R.id.txtNombreCurso);
        }

        public void bindData(Curso c) {
            txtNombreCurso.setText(c.getNombre());
        }
    }



}