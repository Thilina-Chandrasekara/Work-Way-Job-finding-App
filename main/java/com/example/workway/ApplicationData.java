package com.example.workway;

public class ApplicationData {
    private String id; // Unique identifier for the application
    private String name;
    private String education;
    private String experience;
    private String pdfUrl;

    // Default constructor
    public ApplicationData() {
        // Default constructor required for Firebase
    }

    // Parameterized constructor
    public ApplicationData(String id, String name, String education, String experience, String pdfUrl) {
        this.id = id;
        this.name = name;
        this.education = education;
        this.experience = experience;
        this.pdfUrl = pdfUrl;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }
}
