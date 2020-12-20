package com.example.resepmakanan;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView makanan,bahan,durasi,porsi,langkah,deskripsi;
    int porsii,durasii;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActionBar().hide();
        setContentView(R.layout.activity_detail);
        makanan=findViewById(R.id.textView4);
        porsi=findViewById(R.id.textView55);
        durasi=findViewById(R.id.textView66);
        bahan=findViewById(R.id.textView77);
        langkah=findViewById(R.id.textView88);
        deskripsi=findViewById(R.id.textView99);
        img=findViewById(R.id.imageView3);

        Bundle bundle = getIntent().getExtras();
        porsii=bundle.getInt("porsi",0);
        durasii=bundle.getInt("durasi",0);

        makanan.setText(bundle.getString("makanan"));
        porsi.setText(String.valueOf(porsii));
        durasi.setText(String.valueOf(durasii));
        bahan.setText(bundle.getString("bahan"));
        langkah.setText(bundle.getString("langkah"));
        deskripsi.setText(bundle.getString("deskripsi"));

        byte[] buktiImage = bundle.getByteArray("foto");
        Bitmap bitmap = BitmapFactory.decodeByteArray(buktiImage, 0, buktiImage.length);
        img.setImageBitmap(bitmap);
    }
}