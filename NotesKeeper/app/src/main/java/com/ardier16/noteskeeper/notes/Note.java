package com.ardier16.noteskeeper.notes;

import java.util.Date;

public class Note {
    private int id;
    private String title;
    private String description;
    private NotePriority priority;
    private Date dateCreated;
    private String imagePath;

    public Note(String title, String description,
                NotePriority priority, String imageUri) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dateCreated = new Date();
        this.imagePath = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NotePriority getPriority() {
        return priority;
    }

    public void setPriority(NotePriority priority) {
        this.priority = priority;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imageUri) {
        this.imagePath = imageUri;
    }
}


