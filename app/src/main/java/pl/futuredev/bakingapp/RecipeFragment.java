package pl.futuredev.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment {

    @BindView(R.id.iv_recipe)
    ImageView ivRecipeName;
    @BindView(R.id.tv_servings)
    TextView tvServings;

    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.recipe_fragment, container, false);
        ButterKnife.bind(this, view);

        ivRecipeName.setImageResource(R.drawable.nutellapie);
        tvServings.setText("Servings: 8");
        return view;
    }


}
