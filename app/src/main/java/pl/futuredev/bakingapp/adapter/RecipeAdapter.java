package pl.futuredev.bakingapp.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;
import pl.futuredev.bakingapp.IOnClickHandler;
import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.SecondActivity;
import pl.futuredev.bakingapp.models.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final IOnClickHandler onClickHandler;
    private List<Recipe> recipe;
    Context context;
    Recipe recipee;


    public RecipeAdapter(List<Recipe> recipe, Context context, IOnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
        this.recipe = recipe;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe)
        ImageView ivRecipe;
        @BindView(R.id.tv_servings)
        TextView tvServings;
        @BindView(R.id.tv_recipe_name)
        TextView tvRecipeName;
        @BindView(R.id.rl_single_recipe)
        RelativeLayout rlSingleRecipe;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            onClickHandler.onClick(recipee, clickPosition);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_recipe, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ImageView ivRecipe = holder.ivRecipe;
        TextView tvServings = holder.tvServings;
        TextView tvRecipeName = holder.tvRecipeName;
        RelativeLayout relativeLayout = holder.rlSingleRecipe;

        recipee = recipe.get(position);
        String cake = recipee.getName();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Blurry.with(context)
                        .radius(20)
                        .sampling(2)
                        .animate(500)
                        .async()
                        .capture(relativeLayout)
                        .into(ivRecipe);
            }
        }, 3000);

        if (cake.equals("Nutella Pie")) {
            ivRecipe.setImageResource(R.drawable.nutellapie);
        } else if (cake.equals("Yellow Cake")) {
            ivRecipe.setImageResource(R.drawable.yellowcake);
        } else if (cake.equals("Brownies")) {
            ivRecipe.setImageResource(R.drawable.brownies);
        } else {
            ivRecipe.setImageResource(R.drawable.cheesecake);
        }

        //   Picasso.get().load(R.drawable.nutellapie).into(ivRecipe);
        tvServings.setText(context.getString(R.string.servings) +recipe.get(position).getServings());
        tvRecipeName.setText(recipe.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return recipe.size();
    }
}