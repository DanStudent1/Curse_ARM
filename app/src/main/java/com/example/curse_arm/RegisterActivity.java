package com.example.curse_arm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button registerBtn, goToLoginBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверка: если пользователь уже авторизован, переходим в MainActivity
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_register);

        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        registerBtn = findViewById(R.id.buttonRegister);
        goToLoginBtn = findViewById(R.id.buttonGoToLogin);

        registerBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Добавление пользователя в Firestore
                                String uid = user.getUid();
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("createdAt", System.currentTimeMillis());

                                db.collection("users").document(uid)
                                        .set(userData)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(this, MainActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(this, "Ошибка сохранения данных: " + e.getMessage(), Toast.LENGTH_LONG).show()
                                        );
                            }
                        } else {
                            Toast.makeText(this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        goToLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
