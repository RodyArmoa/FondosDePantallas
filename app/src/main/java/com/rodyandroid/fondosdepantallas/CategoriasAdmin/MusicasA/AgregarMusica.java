package com.rodyandroid.fondosdepantallas.CategoriasAdmin.MusicasA;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.AgregarPelicula;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.Pelicula;
import com.rodyandroid.fondosdepantallas.CategoriasAdmin.PeliculasA.PeliculasA;
import com.rodyandroid.fondosdepantallas.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AgregarMusica extends AppCompatActivity {


    EditText nombremusicas;
    TextView vitaMusica;
    ImageView imagenAgregarMusica;
    Button PublicarMusica;

    String RutaAlmacenamiento = "Musica_Subida/";
    String RutaBasededatos = "Musicas";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    com.google.firebase.database.DatabaseReference DatabaseReference;

    ProgressDialog progressDialog;
    String rNombre, rImagen, rVista;


   // int CODIGO_DE_SOLICITUD_IMAGEN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_musica);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        nombremusicas = findViewById(R.id.nombremusicas);
        vitaMusica = findViewById(R.id.vitaMusica);
        imagenAgregarMusica = findViewById(R.id.agregarImagenMusica);
        PublicarMusica = findViewById(R.id.PublicarMusica);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaBasededatos);
        progressDialog = new ProgressDialog(AgregarMusica.this);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            //recuperar los datos de la actividad anterior
            rNombre = intent.getString("NOmbreEnviado");
            rImagen = intent.getString("ImagenEnviada");
            rVista = intent.getString("Vistaenviada");


            //Setear

            nombremusicas.setText(rNombre);
            vitaMusica.setText(rVista);
            Picasso.get().load(rImagen).into(imagenAgregarMusica);

            //Cambiar el nombre en el ActionBar

            actionBar.setTitle("Actualizar");
            String actualizar = "Actualizar";
            //Cambiar el nombre del boton
            PublicarMusica.setText(actualizar);

        }

        imagenAgregarMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //version SDK 30

                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"),CODIGO_DE_SOLICITUD_IMAGEN);*/

                //Version de SDK por encima del 31

                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");

                ObtenerImagenGaleria.launch(intent1);
            }
        });

        PublicarMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PublicarMusica.getText().equals("Publicar")) {
                    SubirImagen();
                } else {
                    EmpezarActualizacion();
                }
            }
        });
    }

    private void EmpezarActualizacion() {

        progressDialog.setTitle("Actualizando");
        progressDialog.setMessage("Espere por favor.....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        EliminarImagenAnterior();

    }

    private void EliminarImagenAnterior() {

        StorageReference Imagen = getInstance().getReferenceFromUrl(rImagen);
        Imagen.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                //si la imagen se eliminó
                Toast.makeText(AgregarMusica.this, "La imagen anterior se eliminó", Toast.LENGTH_SHORT).show();
                SubirNuevaImagen();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AgregarMusica.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    private void SubirNuevaImagen() {
        String nuevaImagen = System.currentTimeMillis() + ".npg";
        StorageReference mStoraReference2 = mStorageReference.child(RutaAlmacenamiento + nuevaImagen);
        Bitmap bitmap = ((BitmapDrawable) imagenAgregarMusica.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = mStoraReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarMusica.this, "Nueva Imagen Cargada", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();
                ActualizarImagenBD(downloadUri.toString());
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarMusica.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void ActualizarImagenBD(final String NuevaImagen) {
        final String nombreActualizar = nombremusicas.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Musicas");

        Query query = databaseReference.orderByChild("nombre").equalTo(rNombre);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Datos a Actualizar
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ds.getRef().child("nombre").setValue(nombreActualizar);
                    ds.getRef().child("image").setValue(NuevaImagen);
                }

                progressDialog.dismiss();
                Toast.makeText(AgregarMusica.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarMusica.this, MusicaA.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SubirImagen() {

        String mNombre = nombremusicas.getText().toString();
        if (mNombre.equals("") || RutaArchivoUri == null) {
            Toast.makeText(this, "Asigne un nombre o una imagen", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("espere por favor");
            progressDialog.setMessage("Subiendo imagen de MUSICA......");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child(
                    RutaAlmacenamiento + System.currentTimeMillis() + "." + ObtenerExtenciondelArchivo(RutaArchivoUri));
            storageReference2.putFile(RutaArchivoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;

                            Uri downloadURI = uriTask.getResult();


                            String mVista = vitaMusica.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Musica musica = new Musica(downloadURI.toString(), mNombre, VISTA);
                            String ID_IMAGE = DatabaseReference.push().getKey();
                            DatabaseReference.child(ID_IMAGE).setValue(musica);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarMusica.this, "Subido", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AgregarMusica.this, MusicaA.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AgregarMusica.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            progressDialog.setTitle("Publicando");
                            progressDialog.setCancelable(false);

                        }
                    });
        }


    }

    private String ObtenerExtenciondelArchivo(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    //Version SDK 30
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CODIGO_DE_SOLICITUD_IMAGEN
                &&resultCode == RESULT_OK
                && data != null
                && data.getData() != null){

            RutaArchivoUri = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),RutaArchivoUri);
                imagenAgregarMusica.setImageBitmap(bitmap);

            }catch (Exception e){
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
    }*/

    //Version SDK +31
    private ActivityResultLauncher<Intent> ObtenerImagenGaleria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Menejar el result del intent

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //Seleccion de imageb
                        Intent data = result.getData();
                        //Obtener URi de imagen

                        RutaArchivoUri = data.getData();
                        imagenAgregarMusica.setImageURI(RutaArchivoUri);


                    } else {
                        Toast.makeText(AgregarMusica.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}