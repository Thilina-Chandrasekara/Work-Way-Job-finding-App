package com.example.workway;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {
    private Context context;
    private List<ApplicationData> applicationList;
    private DatabaseReference applicationReference;

    public ApplicationAdapter(Context context, List<ApplicationData> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
        this.applicationReference = FirebaseDatabase.getInstance().getReference("Applications");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_application, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApplicationData application = applicationList.get(position);
        holder.nameTextView.setText(application.getName());
        holder.educationTextView.setText(application.getEducation());
        holder.experienceTextView.setText(application.getExperience());

        // Handle Download CV button click
        holder.downloadCvButton.setOnClickListener(v -> {
            String pdfUrl = application.getPdfUrl();
            if (pdfUrl != null && !pdfUrl.isEmpty()) {
                downloadCv(pdfUrl);
            } else {
                Toast.makeText(context, "PDF URL is not available or invalid.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Delete button click
        holder.deleteButton.setOnClickListener(v -> {
            deleteApplication(application, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView educationTextView;
        TextView experienceTextView;
        Button downloadCvButton;
        Button deleteButton;  // Add Delete button

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            educationTextView = itemView.findViewById(R.id.educationTextView);
            experienceTextView = itemView.findViewById(R.id.experienceTextView);
            downloadCvButton = itemView.findViewById(R.id.downloadCvButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // Method to download the CV
    private void downloadCv(String pdfUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(pdfUrl));
        context.startActivity(intent);
    }

    // Method to delete an application
    private void deleteApplication(ApplicationData application, int position) {
        applicationReference.child(application.getId()).removeValue().addOnSuccessListener(aVoid -> {
            Toast.makeText(context, "Application deleted successfully", Toast.LENGTH_SHORT).show();
            applicationList.remove(position);
            notifyItemRemoved(position);
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to delete application", Toast.LENGTH_SHORT).show();
        });
    }
}
