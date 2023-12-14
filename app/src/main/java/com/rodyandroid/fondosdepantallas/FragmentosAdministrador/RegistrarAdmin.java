package com.rodyandroid.fondosdepantallas.FragmentosAdministrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rodyandroid.fondosdepantallas.MainActivityAdministrador;
import com.rodyandroid.fondosdepantallas.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;


public class RegistrarAdmin extends Fragment {

    TextView fechadeRegistro;
    EditText Correo, Password, Nombre,Apellido,Edad;

    Button Registrar;


    FirebaseAuth auth;

    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_regristro_admin, container, false);

        /*Campo Registro*/
        fechadeRegistro = view.findViewById(R.id.fechadeRegistro);
        Correo = view.findViewById(R.id.Correo);
        Password= view.findViewById(R.id.Password);
        Nombre = view.findViewById(R.id.Nombre);
        Apellido = view.findViewById(R.id.Apellido);
        Edad = view.findViewById(R.id.Edad);
        /*Campo Registro*/


        /*Boton registrar*/
        Registrar = view.findViewById(R.id.Registrar);


        auth= FirebaseAuth.getInstance();//estamos inicializando firebase authentication

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d 'de' MMM 'del' ' yyyy '");//formato de fecha: 14 de febrero 2023
        String SFecha = fecha.format(date);
        fechadeRegistro.setText(SFecha);


        //Al hacer clic en el boton Registrar

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Convertimos a String Edittext Correo y contraseña
                String correo = Correo.getText().toString();
                String pass = Password.getText().toString();
                String nombre = Nombre.getText().toString();
                String apellidos = Apellido.getText().toString();
                String edad = Edad.getText().toString();

                if (correo.equals("") ||pass.equals("") || nombre.equals("") || apellidos.equals("") || edad.equals("")){

                    Toast.makeText(getActivity(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }else {

                    //Validacion del correo electronico, comprueba el "@" y el ".com"
                    if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                        Correo.setError("Correo Invalido");
                        Correo.setFocusable(true);
                    } else if (pass.length() < 6) {
                        Password.setError("La contraseña debe ser mayor o igual a 6");
                        Password.setFocusable(true);
                    } else {
                        RegistroAdministradores(correo, pass);
                    }
                }
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);

        return view;
    }

    //Metodo para registrar Administradores
    private void RegistroAdministradores(String correo, String pass) {

        progressDialog.show();

        auth.createUserWithEmailAndPassword(correo,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si el administrador fue creado correctamente

                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            assert  user != null;//afirmar de que el admin  no es nulo

                            //Convertir a cadena los datos de los administradores

                            String UID = user.getUid();
                            String correo = Correo.getText().toString();
                            String pass = Password.getText().toString();
                            String nombre = Nombre.getText().toString();
                            String apellido = Apellido.getText().toString();
                            String edad= Edad.getText().toString();
                            int EdadInt = Integer.parseInt(edad);

                            HashMap<Object, Object > Administradores = new HashMap<>();

                            Administradores.put("UID",UID);
                            Administradores.put("CORREO", correo);
                            Administradores.put("PASSWORD",pass);
                            Administradores.put("NOMBRES", nombre);
                            Administradores.put("APELLIDOS", apellido);
                            Administradores.put("EDAD", EdadInt);
                            Administradores.put("IMAGEN","");

                            //Inicializar FirebaseDataBase
                            FirebaseDatabase database= FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("BASE DE DATOS ADMINISTRADORES");
                            reference.child(UID).setValue(Administradores);

                            startActivity( new Intent(getActivity(), MainActivityAdministrador.class));
                            Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Ha ocurrido un ERROR", Toast.LENGTH_SHORT).show();
                        }
                        
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}