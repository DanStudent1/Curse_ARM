package com.example.curse_arm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.CaptureActivity;

public class QRScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int QR_SCAN_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            startScanner();
        }
    }

    private void startScanner() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, QR_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_SCAN_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String scannedResult = data.getStringExtra("SCAN_RESULT");
            if (scannedResult != null) {
                Toast.makeText(this, "Сканировано: " + scannedResult, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Сканирование прошло, но результат не получен", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Сканирование отменено", Toast.LENGTH_SHORT).show();
        }

        finish(); // Закрываем сканер
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // ← Обязательный вызов!

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner();
            } else {
                Toast.makeText(this, "Разрешение на камеру не предоставлено", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
