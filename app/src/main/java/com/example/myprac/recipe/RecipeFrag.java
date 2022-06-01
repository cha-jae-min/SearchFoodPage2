package com.example.myprac.recipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myprac.R;
import com.example.myprac.SearchData;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecipeFrag extends Fragment {

    private View view;
    private ArrayList<SearchData> recipeList;
    private int positionNum;

    private SearchData data;

    private ImageView recipeImg;
    private TextView recipeTitle;
    private TextView recipeDescript;
    private TextView recipeIngre;

    private RecyclerView recyclerView;

    private ArrayList<StepData> recipe_steps;

    private TextView small;
    private TextView middle;
    private TextView big;

    public RecipeFrag(ArrayList<SearchData> recipeList, int positionNum) {
        this.recipeList = recipeList;
        this.positionNum = positionNum;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recipe, container, false);

        recipeImg = view.findViewById(R.id.recipe_img);
        recipeTitle = view.findViewById(R.id.recipe_title);
        recipeDescript = view.findViewById(R.id.recipe_description);
        recipeIngre = view.findViewById(R.id.recipe_ingre);

        data = recipeList.get(positionNum);

        Glide.with(this).load(data.getRecipe_Image()).into(recipeImg);
        recipeTitle.setText(data.getRecipe_title());
        recipeDescript.setText(data.getRecipe_description());

        String[] r = data.getRecipe_ingre_string().split(",");
        for(String str:r) {
            String str2 = String.format("%s \n", str);
            recipeIngre.append(str2);
        }

        String[] r2 = data.getRecipe_step_string().split(",");
        recipe_steps = new ArrayList<>();
        for(String str:r2) {
            String[] r3 = str.split("--");
            StepData sd = new StepData(r3[0],r3[1]);
            recipe_steps.add(sd);
        }

        recyclerView = view.findViewById(R.id.recipe_steps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        small = view.findViewById(R.id.Small);
        middle = view.findViewById(R.id.Middle);
        big = view.findViewById(R.id.Big);

        /*여기 부분 추가 누를 경우 View가 바뀜*/
        big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepsAdapterBig stepsAdapterBig = new StepsAdapterBig(getContext(), recipe_steps);
                recyclerView.setAdapter(stepsAdapterBig);
                synchronized (view) {
                    view.notifyAll(); // mLock.wait();

                }
            }
        });
        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepsAdapter stepsAdapter = new StepsAdapter(getContext(), recipe_steps);
                recyclerView.setAdapter(stepsAdapter);
                synchronized (view) {
                    view.notifyAll(); // mLock.wait();

                }
            }
        });
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StepsAdapterSmall stepsAdapterSmall = new StepsAdapterSmall(getContext(), recipe_steps);
                recyclerView.setAdapter(stepsAdapterSmall);
                synchronized (view) {
                    view.notifyAll(); // mLock.wait();

                }
            }
        });


        StepsAdapter stepsAdapter = new StepsAdapter(getContext(), recipe_steps);
        recyclerView.setAdapter(stepsAdapter);

        return view;
    }

}
