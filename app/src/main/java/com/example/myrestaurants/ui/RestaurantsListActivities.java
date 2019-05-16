package com.example.myrestaurants.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myrestaurants.R;
import com.example.myrestaurants.adapters.RestaurantListAdapter;
import com.example.myrestaurants.models.Restaurant;
import com.example.myrestaurants.services.YelpServices;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.util.ArrayList;


public class RestaurantsListActivities extends AppCompatActivity {
    TextView mLocationTextView;
    ListView mListView;
    private RestaurantListAdapter mAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;


    public static final String TAG = RestaurantsListActivities.class.getSimpleName();

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);

        ButterKnife.bind(this);


        Intent intent = getIntent();
        String location = intent.getStringExtra("location");
        getRestaurants(location);

    }

    private void getRestaurants(String location) {
        final YelpServices yelpService = new YelpServices();
        yelpService.findRestaurants(location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);

                RestaurantsListActivities.this.runOnUiThread(new Runnable() {




                        @Override
                        public void run () {
                            mAdapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
                            mRecyclerView.setAdapter(mAdapter);
                            RecyclerView.LayoutManager layoutManager =
                                    new LinearLayoutManager(RestaurantsListActivities.this);
                            mRecyclerView.setLayoutManager(layoutManager);
                            mRecyclerView.setHasFixedSize(true);
                        }

                    });
            };
        });
    }
}