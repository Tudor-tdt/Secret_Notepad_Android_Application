package com.example.secretnotepad2;

import android.os.Bundle;
import android.content.ContentValues;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new UserDatabaseHelper(this);

        final EditText usernameEditText = findViewById(R.id.editTextUsername);
        final EditText passwordEditText = findViewById(R.id.editTextPassword);
        final EditText confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword);
        Button saveButton = findViewById(R.id.buttonSave);
        Button cancelButton = findViewById(R.id.buttonCancel);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    showToast("Please fill in all fields.");
                } else if (!password.equals(confirmPassword)) {
                    showToast("Passwords do not match.");
                } else {
                    // Verificam daca utilizatorul exista deja in baza de date
                    if (dbHelper.isUserExists(username)) {
                        showToast("Username already exists. Choose a different one.");
                    } else {
                        // Salvam noul utilizator in baza de date
                        if (saveUserToDatabase(username, password)) {
                            showToast("User registered successfully.");
                            finish(); // Ne intoarcem la ecranul de login
                        } else {
                            showToast("Error registering user. Try again.");
                        }
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Ne intoarcem la ecranul de login
            }
        });
    }

    private boolean saveUserToDatabase(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(UserDatabaseHelper.COLUMN_USERNAME, username);
        values.put(UserDatabaseHelper.COLUMN_PASSWORD, password);

        long newRowId = dbHelper.getWritableDatabase().insert(UserDatabaseHelper.TABLE_USERS, null, values);

        return newRowId != -1;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}