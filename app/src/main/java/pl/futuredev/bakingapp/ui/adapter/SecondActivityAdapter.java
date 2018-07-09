package pl.futuredev.bakingapp.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;
import moe.feng.common.stepperview.VerticalStepperView;
import pl.futuredev.bakingapp.R;
import pl.futuredev.bakingapp.models.Recipe;
import pl.futuredev.bakingapp.ui.SecondActivity;
import pl.futuredev.bakingapp.ui.ThirdActivity;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Step;

public class SecondActivityAdapter extends Fragment implements IStepperAdapter {

    private VerticalStepperView mVerticalStepperView;
    private List<Step> steps;
    private Context context;
    private SecondActivity secondActivity;
    private String recipeName;
    private List<Ingredient> ingredients;
    private Step step;
    private Ingredient ingredient;
    private int recipeID;

    public void setSecondActivity(SecondActivity secondActivity) {
        this.secondActivity = secondActivity;
    }

    public static SecondActivityAdapter getInstance(Recipe recipe) {
        SecondActivityAdapter fragment = new SecondActivityAdapter();
        Bundle args = new Bundle();
        args.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) recipe.getSteps());
        args.putInt("id", recipe.getId());
        args.putString("recipeName", recipe.getName());
        args.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) recipe.getIngredients());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vertical_stepper_adapter, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            steps = getArguments().getParcelableArrayList("steps");
            recipeName = getArguments().getString("recipeName");
            ingredients = getArguments().getParcelableArrayList("ingredients");
            recipeID = getArguments().getInt("id");
        }

        mVerticalStepperView = view.findViewById(R.id.vertical_stepper_view);
        mVerticalStepperView.setStepperAdapter(this);
    }

    @Override
    public View onCreateCustomView(int index, Context context, VerticalStepperItemView parent) {
        View inflateView = LayoutInflater.from(context).inflate(R.layout.vertical_stepper_sample_item, parent, false);
        TextView contentView = inflateView.findViewById(R.id.item_content);
        Button nextButton = inflateView.findViewById(R.id.button_next);
        Button prevButton = inflateView.findViewById(R.id.button_prev);

        step = steps.get(index);
     //   ingredient = ingredients.get(index);

        nextButton.setText(index == size() - 1 ? getString(R.string.complete) : getString(android.R.string.ok));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mVerticalStepperView.nextStep()) {
                    AlertDialog.Builder alertbox = new AlertDialog.Builder(getContext());
                    alertbox.setMessage(getString(R.string.you_have_completed) + " " + recipeName);
                    alertbox.setTitle(R.string.well_done);
                    alertbox.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertbox.show();
                } else {
                    Intent intent = new Intent(context, ThirdActivity.class);
                    intent.putExtra("recipeName", recipeName);
                    intent.putExtra("step", step);
                    intent.putExtra("id", recipeID);
                    intent.putParcelableArrayListExtra("ingredients", (ArrayList<? extends Parcelable>) ingredients);
                    startActivity(intent);
                }
            }
        });
        if (index == 0) {
            prevButton.setVisibility(View.GONE);
        } else {
            prevButton.setText(R.string.back);
        }

        inflateView.findViewById(R.id.button_prev).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index != 0) {
                    mVerticalStepperView.prevStep();
                } else {
                    mVerticalStepperView.setAnimationEnabled(!mVerticalStepperView.isAnimationEnabled());
                }
            }
        });
        return inflateView;
    }


    @NonNull
    @Override
    public CharSequence getTitle(int i) {
        return steps.get(i).getShortDescription();
    }

    @Nullable
    @Override
    public CharSequence getSummary(int i) {
        return steps.get(i).getDescription();
    }

    @Override
    public int size() {
        return steps.size();
    }


    @Override
    public void onShow(int i) {

    }

    @Override
    public void onHide(int i) {

    }
}
