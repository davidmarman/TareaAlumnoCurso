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
import android.widget.Toast;

import com.example.tareaalumnocurso.databinding.AlumnoViewHolderBinding;
import com.example.tareaalumnocurso.databinding.FragmentListAlumnosBinding;
import com.example.tareaalumnocurso.entidades.Alumno;
import com.example.tareaalumnocurso.entidades.Curso;
import com.example.tareaalumnocurso.viewmodel.AlumnoViewModel;
import com.example.tareaalumnocurso.viewmodel.CursoViewModel;

import java.util.List;


public class listAlumnosFr extends Fragment {

    private FragmentListAlumnosBinding binding;
    private AlumnoViewModel alumnoViewModel;
    private CursoViewModel cursoViewModel;
    private NavController navController;

    Integer cursoSeleccionado;

    Curso cursoPadre;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentListAlumnosBinding.inflate(inflater,container,false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // View model compartdo
        alumnoViewModel = new ViewModelProvider(requireActivity()).get(AlumnoViewModel.class);
        cursoViewModel = new ViewModelProvider(requireActivity()).get(CursoViewModel.class);
        navController = Navigation.findNavController(view);

        cursoSeleccionado = cursoViewModel.getCursoSeleccionado().getValue().getId();

        // Recoger el curso seleccionado
        cursoViewModel.obtenerById(cursoSeleccionado).observe(getViewLifecycleOwner(), new Observer<Curso>() {
            @Override
            public void onChanged(Curso curso) {
                cursoPadre = curso;
                binding.txtTituloAlumnos.setText(curso.getNombre());
            }
        });

        alumnoViewModel.getCursoPadreSeleccionado().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                cursoSeleccionado = integer;
            }
        });

        // Navegar a nuevo Alumno
        binding.btnNuevoAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoAlumnoFr);
            }
        });

        //Creamos el adaptadpr
        ElementosAdapter elementosAdapter = new ElementosAdapter();

        //Eliminar parpadeo de la animacion
        ((androidx.recyclerview.widget.SimpleItemAnimator) binding.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        // Asociar el adaptador al recyclerView
        binding.recyclerView.setAdapter(elementosAdapter);

        // Obtenemos el array de elementos
        alumnoViewModel.obtenerAlumnoByCurso(cursoSeleccionado).observe(getViewLifecycleOwner(), new Observer<List<Alumno>>() {
            @Override
            public void onChanged(List<Alumno> alumnos) {
                elementosAdapter.establecerLista(alumnos);
            }
        });


        binding.btnNuevoAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nuevoAlumnoFr);
            }
        });




    }

    public class ElementosAdapter extends RecyclerView.Adapter<AlumnoViewHolder>{

        List<Alumno> elementos;

        //Variable para controlar cual item esta abierto
        private int expandedPosition = -1;

        @NonNull
        @Override
        public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AlumnoViewHolder(AlumnoViewHolderBinding.inflate(getLayoutInflater(),parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
            Alumno elemento = elementos.get(position);

            holder.binding.tvNombre.setText(elemento.getNombre());
            holder.binding.tvNota.setText(String.valueOf(elemento.getNota()));
            holder.binding.tvCurso.setText(cursoPadre.getNombre());

            // Verificamos si la posicion actual es la expandida
            final boolean isExpanded = position == expandedPosition;

            holder.binding.layoutDetalles.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            // Cambiar el fondo del item si está activo
            holder.itemView.setActivated(isExpanded);

            // Rotar la flechita
            holder.binding.imgArrow.setRotation(isExpanded ? 180f : 0f);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int posicionActual = holder.getAdapterPosition();

                    // Evitar clicks fantasma si la lista se esta moviendo
                    if(posicionActual == RecyclerView.NO_POSITION) return;

                    // Transicion Suave
                    android.transition.TransitionManager.beginDelayedTransition(binding.recyclerView);

                    int previousExpanded = expandedPosition;

                    // Si clicamos el que esta abierto, lo cerramos. Si no, abrimos el nuevo
                    expandedPosition = (isExpanded)? -1 : posicionActual;

                    // Notificamos el cambio
                    notifyItemChanged(previousExpanded);
                    notifyItemChanged(posicionActual);
                }
            });

            holder.binding.btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteConfirmationDialog(getContext(),elemento);
                }
            });

            holder.binding.btnModificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alumnoViewModel.setAlumnoSeleccionado(elemento);

                    navController.navigate(R.id.modificarAlumnoFr);
                }
            });


        }

        /*
         * Muestra el diálogo de confirmación antes de borrar una tarea.
         */
        private void showDeleteConfirmationDialog(Context context, Alumno a) {
            new AlertDialog.Builder(context)
                    // Título del diálogo
                    .setTitle("Confirmar Borrado")
                    // Pregunta de comprobación
                    .setMessage("¿Está seguro de borrar este alumno: \"" +
                            a.getNombre() + "\"? Esta acción es irreversible.")
                    // Botón de confirmación (Positivo)
                    .setPositiveButton("Sí, Borrar", (dialog, which) -> {
                        // Si el usuario hace clic en "Sí, Borrar",ejecutamos la acciónalumnoViewModel.eliminar(a);
                        alumnoViewModel.eliminar(a);
                        Toast.makeText(context, "Alumno '" + a.getNombre() +
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
            return elementos != null ? elementos.size() : 0;
        }

        public void establecerLista(List<Alumno> elementos){
            this.elementos = elementos;
            notifyDataSetChanged();
        }

        //Metodo del adaptador para obtener un elemento del recyclerViwe
        public Alumno obtenerElemento(int posicion){
            return elementos.get(posicion);
        }
    }
    class AlumnoViewHolder extends RecyclerView.ViewHolder{
        final AlumnoViewHolderBinding binding;

        public AlumnoViewHolder(AlumnoViewHolderBinding binding){
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}