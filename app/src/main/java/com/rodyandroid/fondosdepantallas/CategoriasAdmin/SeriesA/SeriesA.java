package com.rodyandroid.fondosdepantallas.CategoriasAdmin.SeriesA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.MusicasA.MusicaA;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.AgregarPelicula;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.PeliculasA;
import com.rodyandroid.fondosdepantallas.R;

public class SeriesA extends AppCompatActivity {

    RecyclerView recyclerViewSerie;
    FirebaseDatabase mFirebasedatabase;
    DatabaseReference mReference;

    FirebaseRecyclerAdapter<Serie, ViewHolderSerie> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Serie> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setTitle("Series");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        recyclerViewSerie = findViewById(R.id.recyclerViewSerie);
        recyclerViewSerie.setHasFixedSize(true);

        mFirebasedatabase = FirebaseDatabase.getInstance();
        mReference = mFirebasedatabase.getReference("SERIES");

        ListarImagenespeSerie();

    }

    private void ListarImagenespeSerie() {

        options = new FirebaseRecyclerOptions.Builder<Serie>().setQuery(mReference, Serie.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Serie, ViewHolderSerie>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderSerie viewHolderSerie, int position, @NonNull Serie serie) {

                viewHolderSerie.SeteoSerie(
                        getApplicationContext(),
                        serie.getNombre(),
                        serie.getVistas(),
                        serie.getImagen()


                );

            }

            @NonNull
            @Override
            public ViewHolderSerie onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serie, parent, false);

                ViewHolderSerie viewHolderSerie = new ViewHolderSerie(itemView);

                viewHolderSerie.setOnClickListener(new ViewHolderSerie.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(SeriesA.this, "Item Click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SeriesA.this);
                        final String Nombre = getItem(position).getNombre();
                        final String Imagen = getItem(position).getImagen();
                        int Vista = getItem(position).getVistas();
                        String VistaString = String.valueOf(Vista);

                        String[] opciones = {"Actualizar", "Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SeriesA.this);
                                    String[] opciones = {"Actualizar", "Eliminar"};
                                    builder.setItems(opciones, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            if (i == 0) {
                                                Intent intent = new Intent(SeriesA.this, AgregarSeries.class);
                                                intent.putExtra("NOmbreEnviado",Nombre);
                                                intent.putExtra("ImagenEnviada",Imagen);
                                                intent.putExtra("Vistaenviada",VistaString);
                                                startActivity(intent);
                                            }
                                            if (i == 1) {
                                                EliminarDatos(Nombre, Imagen);
                                            }
                                        }
                                    });
                                    builder.create().show();
                                }
                                if (i == 1) {
                                    EliminarDatos(Nombre, Imagen);
                                }
                            }
                        });
                        builder.create().show();

                    }
                });
                return viewHolderSerie;
            }
        };

        recyclerViewSerie.setLayoutManager(new GridLayoutManager(SeriesA.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewSerie.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarDatos(final String NombreAcual, final String ImagenActual ){
        AlertDialog.Builder builder = new AlertDialog.Builder(SeriesA.this);
        builder.setTitle("Elminar");
        builder.setMessage("¿Desea Eliminar Imagen?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Query query = mReference.orderByChild("nombre").equalTo(NombreAcual);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren() ){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(SeriesA.this, "la imagen ha sido Eliminada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SeriesA.this, error.getMessage(), Toast.LENGTH_SHORT).show();



                    }
                });

                StorageReference ImagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
                ImagenSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(SeriesA.this, "Eliminado", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SeriesA.this,e.getMessage() ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SeriesA.this, "Cancelado por el Administrador", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agregar,menu);
        menuInflater.inflate(R.menu.menu_vista,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Agregar) {
            //Toast.makeText(this, "Agregar Imagen", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SeriesA.this,AgregarSeries.class));
            finish();
            return true; // Indica que el evento ha sido manejado
        }else if (item.getItemId() == R.id.Vista) {
            Toast.makeText(this, "Listar Imagenes", Toast.LENGTH_SHORT).show();
            return true; // Indica que el evento ha sido manejado
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}