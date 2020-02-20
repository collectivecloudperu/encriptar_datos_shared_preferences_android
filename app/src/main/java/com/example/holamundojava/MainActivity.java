package com.example.holamundojava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    // Defino los objetos de la interface
    Button button;
    EditText correo, asunto, mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Datos", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // Datos a guardar en Shared Preferences
        editor.putString("Nombre", "Gelatina de Fresa");
        editor.putString("Precio", "2.50");
        editor.putString("Stock", "35");

        editor.apply();
        */

        String encriptar = null;
        try {
            encriptar = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = null;
        try {
            sharedPreferences = EncryptedSharedPreferences.create(
                    "Datos Encriptados",
                    encriptar,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Guardo los datos encriptandolos en Shared Preferences
        sharedPreferences.edit()
                .putString("Nombre", "Gelatina de Fresa")
                .putString("Precio", "2.50")
                .putString("Stock", "35")
                .apply();


        // Obtengo los elementos de la interface
        correo = findViewById(R.id.caja_correo);
        asunto = findViewById(R.id.caja_asunto);
        mensaje = findViewById(R.id.caja_mensaje);
        button = findViewById(R.id.btn_enviar);

        // El setOnClickListener del botón Enviar
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view)
            {
                String enviarcorreo = correo.getText().toString();
                String enviarasunto = asunto.getText().toString();
                String enviarmensaje = mensaje.getText().toString();

                // Defino mi Intent y hago uso del objeto ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);

                // Defino los Strings Email, Asunto y Mensaje con la función putExtra
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { enviarcorreo });
                intent.putExtra(Intent.EXTRA_SUBJECT, enviarasunto);
                intent.putExtra(Intent.EXTRA_TEXT, enviarmensaje);

                // Establezco el tipo de Intent
                intent.setType("message/rfc822");

                // Lanzo el selector de cliente de Correo
                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Elije un cliente de Correo:"));
            }
        });
    }

}
