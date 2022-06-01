package com.example.myprac.navigation;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myprac.HomeAdapter;
import com.example.myprac.MainActivity;
import com.example.myprac.R;
import com.example.myprac.SearchData;
import com.example.myprac.home.BannerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;

public class HomeFrag extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private HomeAdapter homeAapter;
    private ArrayList<SearchData> recipe_menu;

    ViewPager2 homeBanner;
    ViewPager2 homeBanner2;
    private CircleIndicator3 mIndicator;
    BannerAdapter bannerAdapter;
    BannerAdapter bannerAdapter2;

    int firstImgCount = 0;
    private Handler headerHandler = new Handler();
    private Runnable headerRunnable = new Runnable() {
        @Override
        public void run() {
            homeBanner2.setCurrentItem(homeBanner2.getCurrentItem() + 1, true);

        }
    };

    int[] images = {R.drawable.banner_01, R.drawable.banner_02,
            R.drawable.banner_03}; //이미지 주소를 넣는다

    int[] images2 = {R.drawable.banner_04, R.drawable.banner_05};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homefrag, container, false);

        /*images.add(R.drawable.banner_01);
        images.add(R.drawable.banner_02);
        images.add(R.drawable.banner_03);
        images2.add(R.drawable.banner_04);
        images2.add(R.drawable.banner_05);*/


        firstImgCount = images.length;
        homeBanner = view.findViewById(R.id.home_banner);
        bannerAdapter = new BannerAdapter(images, 0);
        homeBanner.setAdapter(bannerAdapter);

        mIndicator = (CircleIndicator3) view.findViewById(R.id.banner_indicator);
        mIndicator.createIndicators(images.length,0);

        homeBanner2 = view.findViewById(R.id.home_banner_2);
        bannerAdapter2 = new BannerAdapter(images2, 1);
        homeBanner2.setAdapter(bannerAdapter2);

        homeBanner.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%firstImgCount);
            }
        });

        homeBanner2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                headerHandler.removeCallbacks(headerRunnable);
                headerHandler.postDelayed(headerRunnable, 7000); //슬라이드 7초 지속
            }
        });

        /*
        database = FirebaseDatabase.getInstance(); //데이터베이스 연동
        databaseReference = database.getReference("Recipe"); //DB 테이블 연결
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //데이터베이스를 받아오는 곳
                recipe_menu.clear(); //기존 배열이 남아있지 않게 초기화
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SearchData searchData = snapshot.getValue(SearchData.class);
                    recipe_menu.add(searchData);

                }
                homeAapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //에러가 발생할 시 실행
            }
        });


        homeAapter = new HomeAdapter(getContext(),recipe_menu);
        recyclerView.setAdapter(homeAapter);

        ((MainActivity)getActivity()).setRecipeList(recipe_menu);*/

        return view;
    }
    @Override
    public void onPause(){
        super.onPause();
        headerHandler.removeCallbacks(headerRunnable);
    }
    @Override
    public void onResume() {
        super.onResume();
        headerHandler.postDelayed(headerRunnable, 7000);
    }
}
