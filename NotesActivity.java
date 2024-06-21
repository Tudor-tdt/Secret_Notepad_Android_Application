package com.example.secretnotepad2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NotesActivity extends AppCompatActivity {

    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //Preluam numele de utilizator din intent
        Intent intent = getIntent();
        loggedInUsername = intent.getStringExtra("USERNAME");

        TextView welcomeTextView = findViewById(R.id.textViewWelcome);
        final EditText noteEditText = findViewById(R.id.editTextNote);
        Button saveNoteButton = findViewById(R.id.buttonSaveNote);
        Button logoutButton = findViewById(R.id.buttonLogout);

        // Afisam mesaj de bun venit care contine numele utilizatorului
        welcomeTextView.setText(getString(R.string.welcome_label, loggedInUsername));

        // Încărcam nota salvată pentru utilizatorul conectat, dacă există
        String savedNote = loadNoteFromInternalStorage(loggedInUsername);
        noteEditText.setText(savedNote);

        // Salvam nota
        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteContent = noteEditText.getText().toString();

                // Save the note to internal storage
                saveNoteToInternalStorage(loggedInUsername, noteContent);

                // Show a toast message indicating successful saving
                showToast("Note saved successfully!");
            }
        });

        // Logout button click event
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigam înapoi la ecranul de login
                Intent intent = new Intent(NotesActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finalizam activitatea curentă pentru a preveni revenirea la NotesActivity din ecranul de login
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void saveNoteToInternalStorage(String username, String noteContent) {
        String filename = "note_" + username + ".txt";
        try (FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE)) {
            fos.write(noteContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Error saving note. Try again.");
        }
    }

    private String loadNoteFromInternalStorage(String username) {
        String filename = "note_" + username + ".txt";
        try {
            byte[] buffer = new byte[1024];
            StringBuilder stringBuilder = new StringBuilder();
            int bytesRead;

            // Citim continutul fiserului(cate maxim 1024 de octeti odata), si apoi il lipim la stringBuilder
            try (FileInputStream fis = openFileInput(filename)) {
                while ((bytesRead = fis.read(buffer)) != -1) {
                    stringBuilder.append(new String(buffer, 0, bytesRead));
                }
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}