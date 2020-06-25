package com.example.fridge_project.repoData;

import com.example.fridge_project.database.Recipe;

public class RecipeShortD {
    private String name ;
    private String description ;

    public RecipeShortD(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public RecipeShortD(Recipe recipe){
        this.name = recipe.getName()  ;
        this.description = recipe.getDescription() ;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
