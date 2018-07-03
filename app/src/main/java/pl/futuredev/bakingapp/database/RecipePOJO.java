package pl.futuredev.bakingapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "recipe")
public class RecipePOJO {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String ingredient;

    public RecipePOJO(int id, String name, String ingredient) {
        this.id = id;
        this.name = name;
        this.ingredient = ingredient;
    }

    @Ignore
    public RecipePOJO(String name, String ingredient) {
        this.name = name;
        this.ingredient = ingredient;
    }
}
