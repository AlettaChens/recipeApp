package com.example.graduate.findingcooking.bean;

public class History {
    private Long id;
    private Long recipeId;
    private Long userId;
    private String readTime;
    private float preference;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public float getPreference() {
        return preference;
    }

    public void setPreference(float preference) {
        this.preference = preference;
    }
}
