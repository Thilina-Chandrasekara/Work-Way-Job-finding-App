package com.example.workway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewApplicationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ApplicationAdapter applicationAdapter;
    private List<ApplicationData> applicationList;
    private DatabaseReference applicationReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applications);

        recyclerView = findViewById(R.id.recyclerViewApplications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        applicationList = new ArrayList<>();
        applicationAdapter = new ApplicationAdapter(this, applicationList);
        recyclerView.setAdapter(applicationAdapter);

        applicationReference = FirebaseDatabase.getInstance().getReference("Applications");
        applicationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    ApplicationData application = itemSnapshot.getValue(ApplicationData.class);
                    if (application != null) {
                        application.setId(itemSnapshot.getKey());  // Set ID from Firebase key
                        applicationList.add(application);
                    }
                }
                applicationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
