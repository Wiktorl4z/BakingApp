package pl.futuredev.bakingapp.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.database.entity.RecipeDataBase;
import pl.futuredev.bakingapp.database.entity.RecipePOJO;
import pl.futuredev.bakingapp.models.Ingredient;

public class WidgetService extends RemoteViewsService {

    private RecipeDataBase recipeDataBase;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewsFactory(this.getApplicationContext());
    }

    public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;
        List<RecipePOJO> recipes;
        List<RecipePOJO> ingredients;
        String summery;
        String listIngredients;

        public WidgetViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            recipeDataBase = RecipeDataBase.getInstance(getApplicationContext());
            recipes = recipeDataBase.recipeDao().loadAllRecipesSync();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return (recipes != null) ? recipes.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (recipes == null) return null;
            String recipeName = recipes.get(position).getName();
            List<Ingredient> ingredientList = recipes.get(position).getIngredient();

            List<String> strings = new ArrayList<>(ingredientList.size());
            for (Object object : ingredientList) {
                strings.add(Objects.toString(object, null));
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

            StringBuilder sb = new StringBuilder();
            for (String s : strings)
            {
                sb.append(s);
                sb.append("\t");
            }

            remoteViews.setTextViewText(R.id.tvWidgetRecipeDetails, sb.toString());
            remoteViews.setViewVisibility(R.id.tvWidgetRecipeDetails, View.VISIBLE);

            remoteViews.setTextViewText(R.id.tv_recipe_widget_name, recipeName);
            remoteViews.setViewVisibility(R.id.tv_recipe_widget_name, View.VISIBLE);

            Intent fillIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.tv_recipe_widget_name, fillIntent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
