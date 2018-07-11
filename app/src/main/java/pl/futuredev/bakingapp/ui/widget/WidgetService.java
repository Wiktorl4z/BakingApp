package pl.futuredev.bakingapp.ui.widget;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.database.entity.RecipeDataBase;
import pl.futuredev.bakingapp.database.entity.RecipePOJO;

public class WidgetService extends RemoteViewsService {

    private RecipeDataBase recipeDataBase;
    private LiveData<List<RecipePOJO>> recipes;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetViewsFactory(this.getApplicationContext());
    }

    public class WidgetViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;

        public WidgetViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            recipeDataBase = RecipeDataBase.getInstance(getApplicationContext());
            recipes = recipeDataBase.recipeDao().loadAllRecipes();
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return (recipes != null) ? recipes.getValue().size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            if (recipes == null) return null;


            String recipeName = recipes.getValue().get(position).getName();
            String recipeIngredient = recipes.getValue().get(position).getName();

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_item);

            remoteViews.setTextViewText(R.id.tv_widget_recipe_name, recipeName);
            remoteViews.setViewVisibility(R.id.tv_widget_recipe_name, View.VISIBLE);

            remoteViews.setTextViewText(R.id.tv_widget_recipe_details, recipeIngredient);
            remoteViews.setViewVisibility(R.id.tv_widget_recipe_details, View.VISIBLE);

            Intent fillIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.tv_widget_recipe_name, fillIntent);
            remoteViews.setOnClickFillInIntent(R.id.tv_widget_recipe_details, fillIntent);

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