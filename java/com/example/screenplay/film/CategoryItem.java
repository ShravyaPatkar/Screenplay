package com.example.screenplay.film;

public class CategoryItem
{
    public String description,language,image;

    public CategoryItem(String description, String language, String image)
    {
        this.description = description;
        this.language = language;
        this.image = image;

    }


    public CategoryItem() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
