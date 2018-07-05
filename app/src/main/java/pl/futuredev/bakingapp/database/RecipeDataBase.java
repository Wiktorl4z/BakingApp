package pl.futuredev.bakingapp.database;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import pl.futuredev.bakingapp.R;

@Database(entities = {RecipePOJO.class}, version = 4, exportSchema = false)
@TypeConverters({IngredientConverter.class})
public abstract class RecipeDataBase extends RoomDatabase {


    private static final String LOG_TAG = RecipeDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "likedRecipe";
    private static RecipeDataBase recipeDataBaseInstance;

    public static RecipeDataBase getInstance(Context context) {
        if (recipeDataBaseInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, context.getString(R.string.creating_new_database_instance));
                recipeDataBaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipeDataBase.class, RecipeDataBase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, context.getString(R.string.getting_database_instance));
        return recipeDataBaseInstance;
    }

    public abstract RecipeDao recipeDao();


    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
