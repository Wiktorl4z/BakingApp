package pl.futuredev.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.futuredev.bakingapp.models.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final IOnClickHandler onClickHandler;
    private Recipe recipe;

    public RecipeAdapter(Recipe recipe, IOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
        this.recipe = recipe;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe)
        ImageView ivRecipe;
        @BindView(R.id.tv_servings)
        TextView tvServings;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            onClickHandler.onClick(clickPosition);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_fragment, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageView ivRecipe = holder.ivRecipe;
        TextView tvServings = holder.tvServings;

        ivRecipe.setImageResource(R.drawable.nutellapie);
        //   Picasso.get().load(R.drawable.nutellapie).into(ivRecipe);
        tvServings.setText("");

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
