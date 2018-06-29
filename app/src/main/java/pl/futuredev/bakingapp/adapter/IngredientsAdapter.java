package pl.futuredev.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.models.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private List<Ingredient> ingredient;

    public IngredientsAdapter(List<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int getItemCount() {
        return ingredient.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_quantity)
        TextView tvQuantity;
        @BindView(R.id.tv_measure)
        TextView tvMeasure;
        @BindView(R.id.tv_ingredient)
        TextView tvIngredient;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_single_layout, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TextView tvQuantity = holder.tvQuantity;
        TextView tvMeasure = holder.tvMeasure;
        TextView tvIngredient = holder.tvIngredient;

        tvQuantity.setText(ingredient.get(position).getQuantity() + "");
        tvMeasure.setText(ingredient.get(position).getMeasure());
        tvIngredient.setText(ingredient.get(position).getIngredient());
    }
}
