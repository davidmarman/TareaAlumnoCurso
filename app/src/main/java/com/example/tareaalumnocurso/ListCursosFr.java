package com.example.tareaalumnocurso;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
            return new CursoViewHolder(CursoViewHolderBinding.inflate(getLayoutInflater(),parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {

            Curso c =cursoList.get(position);

            holder.binding.txtNombreCurso.setText(c.getNombre());

            holder.binding.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog(getContext(),c);
                }
            });

            holder.binding.btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursoViewModel.setCursoSeleccionado(c);

                    navController.navigate(R.id.modificarCursoFr);

                }
            });


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

        /*
         * Muestra el diálogo de confirmación antes de borrar una tarea.
         */
        private void showDeleteConfirmationDialog(Context context, Curso c) {
            new AlertDialog.Builder(context)
                    // Título del diálogo
                    .setTitle("Confirmar Borrado")
                    // Pregunta de comprobación
                    .setMessage("¿Está seguro de borrar este curso: \"" +
                            c.getNombre() + "\"? Se borraran todos los alumnos relacionados. Esta acción es irreversible.")
                    // Botón de confirmación (Positivo)
                    .setPositiveButton("Sí, Borrar", (dialog, which) -> {
                        // Si el usuario hace clic en "Sí, Borrar",ejecutamos la acciónalumnoViewModel.eliminar(a);
                        cursoViewModel.eliminar(c);
                        Toast.makeText(context, "Curso '" + c.getNombre() +
                                "' eliminado.", Toast.LENGTH_SHORT).show();
                    })
                    // Botón de cancelación (Negativo)
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        // Si el usuario hace clic en "Cancelar", simplementese cierra el diálogo
                        dialog.dismiss();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert) // Icono de alerta
                    .show();
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
        final CursoViewHolderBinding binding;

        public CursoViewHolder(CursoViewHolderBinding binding)
        {
            super(binding.getRoot());
            this.binding = binding;
        }

    }



}