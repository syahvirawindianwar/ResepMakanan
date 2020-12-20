package com.example.resepmakanan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private final AppCompatActivity activity = LoginActivity.this;

    private Context context;
    private Database databaseHelper;
    private Pengguna pengguna;
    EditText username,password;
    Button login,daftar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username    = findViewById(R.id.editTextTextPersonName);
        password    = findViewById(R.id.editTextTextPassword);
        login       = findViewById(R.id.button);
        daftar       = findViewById(R.id.button5);
        initObjects();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running();
            }
        });
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daftarin = new Intent(LoginActivity.this,DaftarActivity.class);
                startActivity(daftarin);
            }
        });
    }
    private void initObjects() {
        databaseHelper = new Database(activity);
        pengguna = new Pengguna();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(), ResepActivity.class));
            finish();
        }
    }
    void running(){
        if (databaseHelper.checkUser(username.getText().toString().trim()
                , password.getText().toString().trim())) {
            Log.d("username",username.getText().toString());
            Log.d("password",password.getText().toString());

            Preferences.setLoggedInUser(getBaseContext(),username.getText().toString().trim());
            Preferences.setLoggedInStatus(getBaseContext(),true);
            Log.d("usernamenya",Preferences.getLoggedInUser(getBaseContext()));
//            Log.d("password",pengguna.getPassword());
            Toast.makeText(this,"Username atau password benar",Toast.LENGTH_LONG).show();
            Intent accountsIntent = new Intent(activity, ResepActivity.class);
            accountsIntent.putExtra("Username", username.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
            finish();
        } else {
            Toast.makeText(this,"Username atau password salah",Toast.LENGTH_LONG).show();
        }
    }
    private void emptyInputEditText() {
        username.setText(null);
        password.setText(null);
    }
}