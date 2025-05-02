package com.example.workway;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserDetailActivity extends AppCompatActivity {

    TextView detailDesc, detailTitle, detailLang;
    ImageView detailImage;
    EditText nameInput, educationInput, experienceInput;
    Button chooseFileButton, applyButton;
    Uri pdfUri;
    String key = "";
    String imageUrl = "";

    private final ActivityResultLauncher<Intent> pdfPickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    pdfUri = result.getData().getData();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        detailDesc = findViewById(R.id.detailDesc);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        detailLang = findViewById(R.id.detailLang);
        nameInput = findViewById(R.id.nameInput);
        educationInput = findViewById(R.id.educationInput);
        experienceInput = findViewById(R.id.experienceInput);
        chooseFileButton = findViewById(R.id.chooseFileButton);
        applyButton = findViewById(R.id.applyButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            detailDesc.setText(bundle.getString("Description"));
            detailTitle.setText(bundle.getString("Title"));
            detailLang.setText(bundle.getString("Language"));
            key = bundle.getString("Key");
            imageUrl = bundle.getString("Image");
            // Load image using Picasso or any other image loading library
            // Picasso.get().load(bundle.getString("Image")).into(detailImage);
        }

        chooseFileButton.setOnClickListener(v -> openFilePicker());

        applyButton.setOnClickListener(v -> uploadData());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        pdfPickerLauncher.launch(Intent.createChooser(intent, "Select CV PDF"));
    }

    private void uploadData() {
        String name = nameInput.getText().toString();
        String education = educationInput.getText().toString();
        String experience = experienceInput.getText().toString();

        if (name.isEmpty() || education.isEmpty() || experience.isEmpty() || pdfUri == null) {
            Toast.makeText(this, "Please fill all fields and select a PDF", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child("CVs/" + System.currentTimeMillis() + ".pdf");

        storageReference.putFile(pdfUri).addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Applications");
                String applicationId = reference.push().getKey();
                Application application = new Application(name, education, experience, uri.toString(), key, imageUrl);

                if (applicationId != null) {
                    reference.child(applicationId).setValue(application)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(UserDetailActivity.this, "Application submitted", Toast.LENGTH_SHORT).show();
                                finish(); // Close activity after submission
                            })
                            .addOnFailureListener(e -> Toast.makeText(UserDetailActivity.this, "Failed to submit application", Toast.LENGTH_SHORT).show());
                }
            });
        }).addOnFailureListener(e -> Toast.makeText(UserDetailActivity.this, "Failed to upload PDF", Toast.LENGTH_SHORT).show());
    }

    // Application model class
    public static class Application {
        public String name;
        public String education;
        public String experience;
        public String pdfUrl;
        public String key;
        public String imageUrl;

        public Application() { }

        public Application(String name, String education, String experience, String pdfUrl, String key, String imageUrl) {
            this.name = name;
            this.education = education;
            this.experience = experience;
            this.pdfUrl = pdfUrl;
            this.key = key;
            this.imageUrl = imageUrl;
        }
    }
}
