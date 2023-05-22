package com.example.my_program;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.BitSet;

public class MainActivity extends AppCompatActivity {


    EditText edit_iput;
    Button button_generate;
    ImageView image_qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_iput = findViewById(R.id.edit_input);
        button_generate = findViewById(R.id.button_generate);
        image_qr = findViewById(R.id.image_qr);

        button_generate.setOnClickListener(V -> {
            ganerateQR();
        });

    };

    private void ganerateQR() {
        String text = edit_iput.getText().toString();

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            image_qr.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }
}

