package com.example.qr_code;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class qrcode_scanner extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 123;
    boolean scan_check = false;
    private CodeScanner mCodeScanner;
    TextView text_scan;
    Button button_scan, button_copy, button_scaner_home;

    /**
     * Метод onCreate вызывается при создании активности
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        // Проверка и запрос разрешений на камеру
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            setupScanner();
        }

        // Инициализация кнопок
        button_scan = findViewById(R.id.button_scan);
        button_copy = findViewById(R.id.button_copy);
        button_scaner_home = findViewById(R.id.button_scaner_home);
        text_scan = findViewById(R.id.text_scan);

        // Обработка нажатия кнопки для сканирования QR-кода
        button_scan.setOnClickListener(V -> {
            mCodeScanner.startPreview();
            scan_check = true;
            text_scan.setText(null);
        });

        button_copy.setOnClickListener(V -> {
            String textToCopy = text_scan.getText().toString();

            if (textToCopy != null && !textToCopy.isEmpty() && scan_check) {
                // Получение объекта ClipboardManager
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                // Создание объекта ClipData для копирования текста
                ClipData clipData = ClipData.newPlainText("Label", textToCopy);

                // Копирование данных в буфер обмена
                clipboardManager.setPrimaryClip(clipData);

                dialog_good_copy_text_window();
            } else {
                dialog_error_zero_count_scan_window();
            }
        });

        // Обработка нажатия кнопки для возвращения на главный экран
        button_scaner_home.setOnClickListener(V -> {
            onBackPressed();
        });
    }

    /**
     * Метод, вызываемый после получения разрешений от пользователя
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupScanner();
        } else {
            onBackPressed();
        }
    }

    /**
     * Метод для настройки сканера QR-кода
     */
    private void setupScanner() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text_scan = findViewById(R.id.text_scan);
                        text_scan.setText(result.getText());
                    }
                });
            }
        });
    }

    /**
     * Метод для отображения диалогового окна при ошибке отсутствия сканирования
     */
    private void dialog_error_zero_count_scan_window() {
        Dialog dialog_error = new Dialog(qrcode_scanner.this);
        dialog_error.setContentView(R.layout.error_zero_count_scan_window);

        Button button_ok = dialog_error.findViewById(R.id.button_error_zero_count_scan_ok);
        button_ok.setOnClickListener(V -> {
            dialog_error.cancel();
        });

        dialog_error.show();
    }

    /**
     * Метод для отображения диалогового окна при успешном копировании текста
     */
    private void dialog_good_copy_text_window() {
        Dialog dialog_good = new Dialog(qrcode_scanner.this);
        dialog_good.setContentView(R.layout.good_copy_text_window);

        Button button_ok = dialog_good.findViewById(R.id.button_good_copy_text_ok);
        button_ok.setOnClickListener(V -> {
            dialog_good.cancel();
        });

        dialog_good.show();
    }
}