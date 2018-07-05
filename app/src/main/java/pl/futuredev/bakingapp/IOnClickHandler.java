package pl.futuredev.bakingapp;

import android.widget.ListAdapter;

import java.util.List;

import pl.futuredev.bakingapp.models.Recipe;

public interface IOnClickHandler {

    void onClick(int clickedItemIndex);
}
