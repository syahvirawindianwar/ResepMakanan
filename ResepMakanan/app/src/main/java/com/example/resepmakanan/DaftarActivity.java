package com.example.resepmakanan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class DaftarActivity extends AppCompatActivity {
    private final AppCompatActivity activity = DaftarActivity.this;

    private Context context;
    private Database databaseHelper;
    private Pengguna pengguna;
    EditText username,password,password1;
    Button daftar,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        username    = findViewById(R.id.editTextTextPersonName1);
        password    = findViewById(R.id.editTextTextPassword1);
        password1   = findViewById(R.id.editTextTextPassword2);
        daftar      = findViewById(R.id.daftar);
        login      = findViewById(R.id.button6);
        initObjects();
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginin = new Intent(DaftarActivity.this,LoginActivity.class);
                startActivity(loginin);
            }
        });
    }
    private void initObjects() {
        databaseHelper = new Database(activity);
        pengguna = new Pengguna();
    }
    void running(){
        if (!isInputEditTextMatches(password, password1,
                "Password tidak cocok")) {
            Log.d("cek","Password tidak cocok");
            Toast.makeText(this,"Password tidak cocok",Toast.LENGTH_LONG);
            return;
        }
        if (!databaseHelper.checkUser(username.getText().toString().trim())) {

            pengguna.setNama(username.getText().toString().trim());
            pengguna.setPassword(password.getText().toString().trim());
            Log.d("username",pengguna.getNama());
            Log.d("password",pengguna.getPassword());
            databaseHelper.tambahPengguna(pengguna);
            Log.d("daftar","success");

            // Snack Bar to show success message that record saved successfully
            Toast.makeText(this,"Register berhasil",Toast.LENGTH_LONG).show();
            emptyInputEditText();
            Intent signIntent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(signIntent);


        } else {
            Toast.makeText(this,"Username sudah digunakan",Toast.LENGTH_LONG).show();
        }
    }
    public boolean isInputEditTextMatches(EditText textInputEditText1, EditText textInputEditText2, String message) {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        Log.d("value1",value1);
        Log.d("value2",value2);
        if (!value1.contentEquals(value2)) {
            textInputEditText2.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        }
        return true;
    }
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void emptyInputEditText() {
        username.setText(null);
        password.setText(null);
        password1.setText(null);
    }

}