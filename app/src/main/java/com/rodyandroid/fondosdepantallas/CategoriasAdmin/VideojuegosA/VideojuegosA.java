package com.rodyandroid.fondosdepantallas.CategoriasAdmin.VideojuegosA;

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
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.AgregarPelicula;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.PeliculasA;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.ViewHolderPelicula;
import com.rodyandroid.fondosdepantallas.R;

public class VideojuegosA extends AppCompatActivity {

    RecyclerView recyclerViewVideojuegos;
    FirebaseDatabase mFirebasedatabase;
    DatabaseReference mReference;

    FirebaseRecyclerAdapter<VideoJuego, com.rodyandroid.fondosdepantallas.CategoriasAdmin.VideojuegosA.ViewHolderVideojuegos> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<VideoJuego> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videojuegos);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setTitle("Videojuegos");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerViewVideojuegos = findViewById(R.id.recyclerViewVideojuegos);
        recyclerViewVideojuegos.setHasFixedSize(true);

        mFirebasedatabase = FirebaseDatabase.getInstance();
        mReference = mFirebasedatabase.getReference("VIDEOJUEGOS");

        ListarImagenesVideojuegos();
    }

    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    private void ListarImagenesVideojuegos() {

        options = new FirebaseRecyclerOptions.Builder<VideoJuego>().setQuery(mReference, VideoJuego.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<VideoJuego, ViewHolderVideojuegos>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderVideojuegos viewHolderVideojuegos, int position, @NonNull VideoJuego videoJuego) {

                viewHolderVideojuegos.SeteoVideojuego(
                        getApplicationContext(),
                        videoJuego.getNombre(),
                        videoJuego.getVistas(),
                        videoJuego.getImagen()


                );

            }

            @NonNull
            @Override
            public ViewHolderVideojuegos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videojueg, parent, false);

                ViewHolderVideojuegos viewHolderVideojuegos = new ViewHolderVideojuegos(itemView);

                viewHolderVideojuegos.setOnClickListener(new ViewHolderVideojuegos.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(VideojuegosA.this, "Item Click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void OnItemLongClick(View view, int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(VideojuegosA.this);
                        final String Nombre = getItem(position).getNombre();
                        final String Imagen = getItem(position).getImagen();
                        int Vista = getItem(position).getVistas();
                        String VistaString = String.valueOf(Vista);

                        String[] opciones = {"Actualizar", "Eliminar"};
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i == 0) {
                                    Intent intent = new Intent(VideojuegosA.this, AgregarVideojuegos.class);
                                    intent.putExtra("NOmbreEnviado",Nombre);
                                    intent.putExtra("ImagenEnviada",Imagen);
                                    intent.putExtra("Vistaenviada",VistaString);
                                    startActivity(intent);                                }
                                if (i == 1) {
                                    EliminarDatos(Nombre, Imagen);
                                }
                            }
                        });
                        builder.create().show();

                    }
                });
                return viewHolderVideojuegos;
            }
        };

        recyclerViewVideojuegos.setLayoutManager(new GridLayoutManager(VideojuegosA.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewVideojuegos.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarDatos(final String NombreAcual, final String ImagenActual) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VideojuegosA.this);
        builder.setTitle("Elminar");
        builder.setMessage("¿Desea Eliminar Imagen?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Query query = mReference.orderByChild("nombre").equalTo(NombreAcual);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(VideojuegosA.this, "la imagen ha sido Eliminada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(VideojuegosA.this, error.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                });

                StorageReference ImagenSeleccionada = getInstance().getReferenceFromUrl(ImagenActual);
                ImagenSeleccionada.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(VideojuegosA.this, "Eliminado", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VideojuegosA.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VideojuegosA.this, "Cancelado por el Administrador", Toast.LENGTH_SHORT).show();
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
           startActivity(new Intent(VideojuegosA.this, AgregarVideojuegos.class));
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