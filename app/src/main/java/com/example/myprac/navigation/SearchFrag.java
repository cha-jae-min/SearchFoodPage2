package com.example.myprac.navigation;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;

import com.example.myprac.MainActivity;
import com.example.myprac.R;
import com.example.myprac.SearchAdapter;
import com.example.myprac.SearchData;
import com.example.myprac.SearchViewModel;
import com.example.myprac.databinding.ActivityMainBinding;
import com.example.myprac.databinding.SearchfragBinding;
import com.example.myprac.recipe.StepData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Tree;
import com.ramotion.foldingcell.FoldingCell;


public class SearchFrag extends Fragment {

    private ArrayList<SearchData> recipe_menu;
    private ArrayList<SearchData> search_menu;


    private LinearLayoutManager linearLayoutManager;

    private View view;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private SearchAdapter searchAdapter;

    private ImageButton search_button;
    private EditText search_bar;
    private Spinner spinner;

    private String k;

    private LinearLayout kindHash;
    private Button rollButton;

    private FoldingCell fc;

    private ImageView bread;
    private ImageView cake;
    private ImageView deli;
    private ImageView dessert;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searchfrag, container, false);
        recyclerView = view.findViewById(R.id.foodSearchRecyclerView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recipe_menu = new ArrayList<>();
        search_menu = new ArrayList<>();
        search_button = view.findViewById(R.id.search_button);
        search_bar = view.findViewById(R.id.search_bar);
        spinner = view.findViewById(R.id.search_spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                k = null;
                k =  parent.getItemAtPosition(position).toString();
                Log.e("MainActivity: ",k);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();; //?????? ????????? ???????????? ????????? ?????? ?????? ????????? ????????? ???????????? ?????? ?????? ??????
            }
        });

        search_bar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            search(); //?????? ????????? ???????????? ????????? ?????? ?????? ????????? ????????? ???????????? ?????? ?????? ??????
                            break;
                    }
                    return true;
                }
                return false;
            }
        });

        search_bar.addTextChangedListener(new TextWatcher() {  //???????????? ??? ????????? ??????
            // ???????????? ???????????? ????????? ??????????????? ??????
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // ???????????? ?????? ???????????? API
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // charSequence??? ?????? ?????? start??? ????????? ???????,
                // EditText??? ????????? ?????? ???
                if(recipe_menu !=null){
                    if(charSequence.length()==0){
                        search();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

                // ????????? ????????? ???

            }
        });
        bread = view.findViewById(R.id.bread);
        cake = view.findViewById(R.id.cake);
        deli = view.findViewById(R.id.deli);
        dessert = view.findViewById(R.id.dessert);

        bread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFilter("BREAD","??????");
            }
        });
        bread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFilter("brea","??????");
            }
        });
        cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFilter("cake","??????");
            }
        });
        deli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFilter("deli","??????");
            }
        });
        dessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFilter("dessert","??????");
            }
        });


        database = FirebaseDatabase.getInstance(); //?????????????????? ??????
        databaseReference = database.getReference("Recipe"); //DB ????????? ??????
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //????????????????????? ???????????? ???
                recipe_menu.clear(); //?????? ????????? ???????????? ?????? ?????????
                TreeSet<String> treeSet = new TreeSet<>();// ?????? ??????
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SearchData searchData = snapshot.getValue(SearchData.class);
                    recipe_menu.add(searchData);
                }
                for(SearchData data : recipe_menu){
                    treeSet.add(data.getKind()); //recipe_menu??? ?????? kind??? treeSet?????? ??????????????? ?????? ??????
                }

                for(String kind: treeSet){
                    createButton(kind);
                }


                search();

                rollButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fc.toggle(true);
                    }
                });

                searchAdapter.notifyDataSetChanged();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //????????? ????????? ??? ??????
            }
        });


        kindHash = view.findViewById(R.id.kindHash);
        searchAdapter = new SearchAdapter(recipe_menu, getContext());
        recyclerView.setAdapter(searchAdapter);
        rollButton = view.findViewById(R.id.kindHashRollButton);
        fc = view.findViewById(R.id.kindHashRoll);


        ((MainActivity)getActivity()).setRecipeList(recipe_menu);

        return view;
    }
    public void searchFilter(String str, String k){
        search_menu.clear();

        for(SearchData f : recipe_menu) {
            if(k.equals("??????")){
                if(f.getRecipe_title().contains(str)){
                    search_menu.add(f);
                }
            }
            else if(k.equals("??????")){
                if(f.getRecipe_description().contains(str)){
                    search_menu.add(f);
                }
            }
            else if(k.equals("??????")){
                if(f.getKind().contains(str)){
                    search_menu.add(f);
                }
            }

        }
        searchAdapter.filterList(search_menu);
    }

    public void search(){
        String str = search_bar.getText().toString();
        Log.e("SearchFrag",str);
        searchFilter(str,k);
    }
    private void createButton(String str){

        Button button = new Button(view.getContext());

        button.setText(str);

        button.setMaxLines(1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);


        params.leftMargin = 5;
        button.setLayoutParams(params);
        button.setHorizontallyScrolling(false);
        button.setTransformationMethod(null);// ?????? ????????? ????????? ??????
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFilter(button.getText().toString(),"??????");
            }
        });

        kindHash.addView(button);
    }

}
