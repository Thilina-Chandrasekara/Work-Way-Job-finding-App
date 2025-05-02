package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfoActivity extends AppCompatActivity {

    private TextView userInfoTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userInfoTextView = findViewById(R.id.userInfoTextView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Applications");

        loadUserInformation();
    }

    private void loadUserInformation() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder userInfo = new StringBuilder();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Application application = dataSnapshot.getValue(Application.class);
                    if (application != null) {
                        userInfo.append("Name: ").append(application.name).append("\n")
                                .append("Education: ").append(application.education).append("\n")
                                .append("Experience: ").append(application.experience).append("\n")
                                .append("CV: ").append(application.pdfUrl).append("\n\n");
                    }
                }
                userInfoTextView.setText(userInfo.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userInfoTextView.setText("Failed to load user information");
            }
        });
    }

    // Application model class
    public static class Application {
        public String name;
        public String education;
        public String experience;
        public String pdfUrl;

        public Application() { }

        public Application(String name, String education, String experience, String pdfUrl) {
            this.name = name;
            this.education = education;
            this.experience = experience;
            this.pdfUrl = pdfUrl;
        }
    }
}
