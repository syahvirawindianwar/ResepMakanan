package com.example.resepmakanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResepActivity extends AppCompatActivity {
    ListView listView;
    TextView logout;
    private List<Resep> reseps = new ArrayList<Resep>();
    private Database databaseHelper;
    private ResepAdapter adapter;
    Button tambah;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listnya);
        tambah = findViewById(R.id.button2);
        logout = findViewById(R.id.textViewLogout);

        initObjects();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // ambil posisi yg diklik
                Resep clicked = (Resep) listView.getItemAtPosition(position);

                Intent intent2 = new Intent(getApplicationContext(),
                        DetailActivity.class);
                intent2.putExtra("makanan", clicked.getMakanan());
                intent2.putExtra("porsi", clicked.getPorsi());
                intent2.putExtra("durasi", clicked.getDurasi());
                intent2.putExtra("bahan", clicked.getBahan());
                intent2.putExtra("langkah", clicked.getLangkah());
                intent2.putExtra("deskripsi", clicked.getDeskripsi());
                intent2.putExtra("foto",clicked.getFoto());
                startActivity(intent2);
            }
        });
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent news= new Intent(ResepActivity.this,TambahActivity.class);
                startActivity(news);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLogout();
            }
        });
        listView.setAdapter(adapter);
    }
    private void initObjects() {
        databaseHelper = new Database(this);
        getData();
        adapter = new ResepAdapter(reseps, this);
        Log.d("username_aktif",Preferences.getLoggedInUser(getBaseContext()));
    }
    @SuppressLint("StaticFieldLeak")
    private void getData() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                reseps.clear();
                reseps.addAll(databaseHelper.getAllResep(Preferences.getLoggedInUser(getBaseContext())));
                Log.d("jumlah_resep", String.valueOf(reseps.size()));
                Log.d("porsi", String.valueOf(reseps.get(0).getPorsi()));
                Log.d("deskripsi", reseps.get(0).getDeskripsi());
                Log.d("jumlah_resep", String.valueOf(databaseHelper.getAllResep(Preferences.getLoggedInUser(getBaseContext())).size()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }
    void showDialogLogout(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to Sign Out?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("sign out","Keluarrr");
                        Preferences.clearLoggedInUser(getBaseContext());
                        Intent iLogout = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(iLogout);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                });
        dialog= builder.create();
        dialog.show();
    }
}