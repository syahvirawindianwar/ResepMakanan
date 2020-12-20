package com.example.resepmakanan;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class TambahActivity extends AppCompatActivity {
    EditText makanan,bahan,durasi,porsi,langkah,deskripsi;
    ImageView img;
    Button tambah,save;
    Resep resep;
    Database databaseHelper;
    Bitmap bitmap;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";
    AlertDialog alertDialog,alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        makanan=findViewById(R.id.editTextFood);
        porsi=findViewById(R.id.editTextPorsi);
        durasi=findViewById(R.id.editTextDurasi);
        bahan=findViewById(R.id.editTextBahan);
        langkah=findViewById(R.id.editTextLangkah);
        deskripsi=findViewById(R.id.editTextDeskripsi);
        img=findViewById(R.id.imageView4);
        tambah=findViewById(R.id.tambahFoto);
        save=findViewById(R.id.button4);

        databaseHelper = new Database(this);
        resep = new Resep();

//        permissionCamera();

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showAlert();
                permissionCamera();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsAlert();
            }
        });
    }
    void saving(){
        ByteArrayOutputStream bytenya = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytenya);
        byte[] foto = bytenya.toByteArray();

        resep.setUsername(Preferences.getLoggedInUser(getBaseContext()));
        resep.setMakanan(makanan.getText().toString());
        resep.setPorsi(Integer.parseInt(porsi.getText().toString()));
        resep.setDurasi(Integer.parseInt(durasi.getText().toString()));
        resep.setBahan(bahan.getText().toString());
        resep.setLangkah(langkah.getText().toString());
        resep.setDeskripsi(deskripsi.getText().toString());
        resep.setFoto(foto);

        databaseHelper.tambahResep(resep);
        Intent good = new Intent(TambahActivity.this , ResepActivity.class);
        startActivity(good);
    }
    void permissionCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (getFromPref(this, ALLOW_KEY)) {
                showAlert();
            } else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)

                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
            openCamera();
        }
    }
    public static void saveToPreferences(Context context, String key, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, allowed);
        prefsEditor.commit();
    }

    public static Boolean getFromPref(Context context, String key) {
        SharedPreferences myPrefs = context.getSharedPreferences(CAMERA_PREF,
                Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(key, false));
    }

    private void showAlert() {
        alert = new AlertDialog.Builder(TambahActivity.this).create();
        alert.setTitle("Alert");
        alert.setMessage("App needs to access the Camera.");

        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

        alert.setButton(AlertDialog.BUTTON_POSITIVE, "ALLOW",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(TambahActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);
//                        openCamera();
                    }
                });
        alert.show();
    }

    private void showSettingsAlert() {
        alertDialog = new AlertDialog.Builder(TambahActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Yakin untuk menyimpan?");

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Ya",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saving();
                        dialog.dismiss();
                    }
                });

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Tidak",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean
                                showRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale(
                                        this, permission);

                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            saveToPreferences(TambahActivity.this, ALLOW_KEY, true);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static void startInstalledAppDetailsActivity(final Activity context) {
        if (context == null) {
            return;
        }

        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    private void openCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent,MY_PERMISSIONS_REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requescode : "+requestCode);
        if(requestCode == MY_PERMISSIONS_REQUEST_CAMERA){
            if(resultCode == RESULT_OK) {
                onCaptureImageResult(data);
//                saving();
            }
        }
    }
    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");

        img.setMaxWidth(200);
        img.setImageBitmap(bitmap);
    }
}