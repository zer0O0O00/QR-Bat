package com.example.qr_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 123;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;
    Button button_list_generate, button_list_scanner, button_info_window;

    /**
     * Метод onCreate вызывается при создании активности
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация кнопок
        button_list_generate = findViewById(R.id.button_list_generate);
        button_list_scanner = findViewById(R.id.button_list_scanner);
        button_info_window = findViewById(R.id.button_info_window);

        // Обработка нажатия кнопки для сканера QR-кода
        button_list_scanner.setOnClickListener(V -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("com.example.QR-Chan.qrcode_scanner");
                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        });

        // Обработка нажатия кнопки для генератора QR-кода
        button_list_generate.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("com.example.QR-Chan.qrcode_generater");
                startActivity(intent);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
            }
        });

        // Обработка нажатия кнопки для информационного окна
        button_info_window.setOnClickListener(V -> {
            dialog_info_window();
        });
    }

    /**
     * Метод, вызываемый после получения разрешений от пользователя
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("com.example.QR-Chan.qrcode_scanner");
                startActivity(intent);
            } else {
                onBackPressed();
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("com.example.QR-Chan.qrcode_generater");
                startActivity(intent);
            } else {
                onBackPressed();
            }
        }
    }

    /**
     * Методы для отображения информационного окна
     */
    private void dialog_info_window() {
        Dialog dialog_info = new Dialog(MainActivity.this);
        dialog_info.setContentView(R.layout.info_window);

        Button button_info = dialog_info.findViewById(R.id.button_info_window);
        button_info.setOnClickListener(V -> {
            dialog_info.cancel();
        });
        dialog_info.show();
    }
}