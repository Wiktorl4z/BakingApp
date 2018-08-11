package pl.futuredev.bakingapp.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
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
import pl.futuredev.bakingapp.test.SimpleIdlingResource;
import pl.futuredev.bakingapp.ui.activities.RecipeStepsActivity;
import pl.futuredev.bakingapp.ui.activities.StepDetailActivity;
import pl.futuredev.bakingapp.models.Ingredient;
import pl.futuredev.bakingapp.models.Step;
import pl.futuredev.bakingapp.ui.interfaces.IOnClickHandler;

public class StepsFragment extends Fragment implements IStepperAdapter {

    private static final String STEPS = "steps";
    private static final String STEP = "step";
    private static final String ID = "id";
    private static final String RECIPE_NAME = "recipeName";
    private static final String INGREDIENTS = "ingredients";
    private static final String TABLET = "tablet";
    private static final String OK = "OK";


    private VerticalStepperView mVerticalStepperView;
    private List<Step> steps;
    private Context context;
    private RecipeStepsActivity recipeStepsActivity;
    private String recipeName;
    private List<Ingredient> ingredients;
    private Step step;
    private Ingredient ingredient;
    private int recipeID;
    private IOnClickHandler iOnClickHandler;
    private Boolean tablet;

    public void setRecipeStepsActivity(RecipeStepsActivity recipeStepsActivity) {
        this.recipeStepsActivity = recipeStepsActivity;
    }

    public StepsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            iOnClickHandler = (IOnClickHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public static StepsFragment getInstance(Recipe recipe, Boolean tablet) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(STEPS, (ArrayList<? extends Parcelable>) recipe.getSteps());
        args.putInt(ID, recipe.getId());
        args.putString(RECIPE_NAME, recipe.getName());
        args.putParcelableArrayList(INGREDIENTS, (ArrayList<? extends Parcelable>) recipe.getIngredients());
        args.putBoolean(TABLET, tablet);
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
            steps = getArguments().getParcelableArrayList(STEPS);
            recipeName = getArguments().getString(RECIPE_NAME);
            ingredients = getArguments().getParcelableArrayList(INGREDIENTS);
            recipeID = getArguments().getInt(ID);
            tablet = getArguments().getBoolean(TABLET);
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
        nextButton.setText(index == size() - 1 ? getString(R.string.complete) : getString(android.R.string.ok));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mVerticalStepperView.nextStep()) {
                    AlertDialog.Builder alertBox = new AlertDialog.Builder(getContext());
                    alertBox.setMessage(getString(R.string.you_have_completed) + " " + recipeName);
                    alertBox.setTitle(R.string.well_done);
                    alertBox.setNeutralButton(OK,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                    alertBox.show();
                } else {
                    if (tablet) {
                        iOnClickHandler.onClickStep(index, recipeName, step, recipeID, ingredients);
                    } else {
                        Intent intent = new Intent(context, StepDetailActivity.class);
                        intent.putExtra(RECIPE_NAME, recipeName);
                        intent.putExtra(STEP, step);
                        intent.putExtra(ID, recipeID);
                        intent.putParcelableArrayListExtra(INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
                        startActivity(intent);
                    }
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
                    if (tablet)
                        iOnClickHandler.onClickStep(index, recipeName, step, recipeID, ingredients);
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
