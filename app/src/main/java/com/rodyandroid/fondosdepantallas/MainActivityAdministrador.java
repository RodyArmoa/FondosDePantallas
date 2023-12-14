package com.rodyandroid.fondosdepantallas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rodyandroid.fondosdepantallas.FragmentosAdministrador.InicioAdmin;
import com.rodyandroid.fondosdepantallas.FragmentosAdministrador.ListarAdmin;
import com.rodyandroid.fondosdepantallas.FragmentosAdministrador.PerfilAdmin;
import com.rodyandroid.fondosdepantallas.FragmentosAdministrador.RegistrarAdmin;

public class MainActivityAdministrador extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrador);

        Toolbar toolbar = findViewById(R.id.toobarA);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout_A);

        NavigationView navigationView = findViewById(R.id.nav_viewA);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        //Fragmento por defecto
        if (savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                    new InicioAdmin()).commit();
            navigationView.setCheckedItem(R.id.InicioAdmin);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.InicioAdmin){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                    new InicioAdmin()).commit();

        }
        if(item.getItemId() == R.id.PerfilAdmin){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                    new PerfilAdmin()).commit();

        }
        if(item.getItemId() == R.id.RegistrarAdmin){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                    new RegistrarAdmin()).commit();

        }
        if(item.getItemId() == R.id.ListarAdmin){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                    new ListarAdmin()).commit();

        }
        if(item.getItemId() == R.id.Salir){
            CerrarSession();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void ComprobandoIniciodesecion(){

        if (user!=null){
            //si el administrador a iniciado sesion
            Toast.makeText(this, "Se ha iniciado Sesion", Toast.LENGTH_SHORT).show();
        }else {
            //Si no ha iniciado session es porque el usuario es un cliente
            startActivity(new Intent(MainActivityAdministrador.this,MainActivity.class));
            finish();
        }
    }

    private void CerrarSession(){
        firebaseAuth.signOut();
        startActivity(new Intent(MainActivityAdministrador.this, MainActivity.class));
        Toast.makeText(this, "Cerraste session exitosamente.", Toast.LENGTH_SHORT).show();
    }
    protected void onStart(){
        ComprobandoIniciodesecion();
        super.onStart();
    }

}

