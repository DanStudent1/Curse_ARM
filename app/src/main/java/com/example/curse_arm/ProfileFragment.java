package com.example.curse_arm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private TextView nameText, emailText, phoneText, storeNameText, roleText, createdAtText;
    private ImageView avatarImage;
    private Button logoutButton;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameText = view.findViewById(R.id.nameText);
        emailText = view.findViewById(R.id.emailText);
        phoneText = view.findViewById(R.id.phoneText);
        storeNameText = view.findViewById(R.id.storeNameText);
        roleText = view.findViewById(R.id.roleText);
        createdAtText = view.findViewById(R.id.createdAtText);
        avatarImage = view.findViewById(R.id.avatarImage);
        logoutButton = view.findViewById(R.id.logoutButton);

        fetchUserProfile();

        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(getContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });

        return view;
    }

    private void fetchUserProfile() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(this::populateProfile)
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Ошибка загрузки профиля", Toast.LENGTH_SHORT).show());
    }

    private void populateProfile(DocumentSnapshot doc) {
        if (doc.exists()) {
            nameText.setText(doc.getString("name"));
            emailText.setText("Email: " + doc.getString("email"));
            phoneText.setText("Телефон: " + doc.getString("phone"));
            storeNameText.setText("Магазин: " + doc.getString("storeName"));
            roleText.setText("Роль: " + doc.getString("role"));

            Long createdAtMillis = doc.getLong("createdAt");
            if (createdAtMillis != null) {
                Date date = new Date(createdAtMillis);
                String formattedDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date);
                createdAtText.setText("Дата регистрации: " + formattedDate);
            } else {
                createdAtText.setText("Дата регистрации: неизвестна");
            }

            String imageUrl = doc.getString("avatarUrl");
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(requireContext()).load(imageUrl).into(avatarImage);
            }
        } else {
            Toast.makeText(getContext(), "Профиль не найден", Toast.LENGTH_SHORT).show();
        }
    }
}
