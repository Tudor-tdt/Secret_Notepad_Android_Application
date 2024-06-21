package com.example.secretnotepad2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new UserDatabaseHelper(this);

        final EditText usernameEditText = findViewById(R.id.editTextUsername);
        final EditText passwordEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.buttonLogin);
        Button registerButton = findViewById(R.id.buttonRegister);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                // Verificam daca utilizatorul exista in baza de date
                if (dbHelper.isUserValid(enteredUsername, enteredPassword)) {
                    // Navigam la NotesActivity
                    Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                    intent.putExtra("USERNAME", enteredUsername);
                    startActivity(intent);
                } else {
                    // Date incorecte
                    showToast("Invalid username or password. Please try again.");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Deschidem ecranul de inregistrare cand apasam butonul 'Register'
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}