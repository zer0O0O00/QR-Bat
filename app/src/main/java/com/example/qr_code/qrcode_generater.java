package com.example.qr_code;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class qrcode_generater extends AppCompatActivity {

    boolean ganerate_check = false;
    Button button_generate, button_save, button_generator_home;
    EditText edit_input;
    ImageView image_qrcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generater);

            button_generate = (Button) findViewById(R.id.button_generate);
            button_save = (Button) findViewById(R.id.button_save);
            button_generator_home = (Button) findViewById(R.id.button_ganerator_home);
            button_generate = findViewById(R.id.button_generate);

            image_qrcode = findViewById(R.id.image_qrcode);

            edit_input = findViewById(R.id.edit_input);

            // Выход в главное меню
            button_generator_home.setOnClickListener(V -> {
                onBackPressed();
            });


            // Гениратор QR
            button_generate.setOnClickListener(V -> {
                String inputText = edit_input.getText().toString().trim();

                if (inputText.isEmpty())
                    dialog_error_text_window();

                else
                    ganerateQR();
            });

            button_save.setOnClickListener(this::onClick);
    };

    private void ganerateQR() {
        String text = edit_input.getText().toString();

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            image_qrcode.setImageBitmap(bitmap);
            ganerate_check = true;
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }
    private void dialog_good_save_window(){

        Dialog dialog_good = new Dialog(qrcode_generater.this);
        dialog_good.setContentView(R.layout.good_save_window);

        Button button_ok = dialog_good.findViewById(R.id.button_good_save_qr_ok);

        button_ok.setOnClickListener(V ->{
            dialog_good.cancel();
        });

        dialog_good.show();
    }
    private void dialog_error_text_window(){

        Dialog dialog_error = new Dialog(qrcode_generater.this);
        dialog_error.setContentView(R.layout.error_text_window);

        Button button_ok = dialog_error.findViewById(R.id.button_error_ok);

        button_ok.setOnClickListener(V ->{
            dialog_error.cancel();
        });

        dialog_error.show();
    }
    private void dialog_error_save_window(){

        Dialog dialog_error = new Dialog(qrcode_generater.this);
        dialog_error.setContentView(R.layout.error_save_window);

        Button button_ok = dialog_error.findViewById(R.id.button_error_save_ok);

        button_ok.setOnClickListener(V ->{
            dialog_error.cancel();
        });

        dialog_error.show();
    }
    private void dialog_error_zero_count_window(){

        Dialog dialog_error = new Dialog(qrcode_generater.this);
        dialog_error.setContentView(R.layout.error_zero_count_generate_window);

        Button button_ok = dialog_error.findViewById(R.id.button_error_zero_count_generate_ok);

        button_ok.setOnClickListener(V ->{
            dialog_error.cancel();
        });

        dialog_error.show();
    }

    private void onClick(View v) {

        // Получение ссылки на изображение

        if(!ganerate_check)
            dialog_error_zero_count_window();

        else {
            Drawable drawable = image_qrcode.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // Получение директории для сохранения изображения
            String downloadDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

            // Создание файла с уникальным именем
            String fileName = "qrcode_" + System.currentTimeMillis() + ".png";
            File file = new File(downloadDirectory, fileName);

            try {
                // Сохранение изображения в файл
                FileOutputStream outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                // Обновление медиа-сканирования для отображения изображения в галерее
                MediaScannerConnection.scanFile(qrcode_generater.this, new String[]{file.getAbsolutePath()}, null, null);

                dialog_good_save_window();
            } catch (IOException e) {
                e.printStackTrace();
                dialog_error_save_window();
            }
        }
    }
}