package com.example.preethibhaskar.itunesforandroid.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.preethibhaskar.itunesforandroid.R;
import com.example.preethibhaskar.itunesforandroid.adapter.ItunesRecyclerviewAdapter;
import com.example.preethibhaskar.itunesforandroid.data.Itunes;
import com.example.preethibhaskar.itunesforandroid.listener.RecyclerItemClickListener;
import com.example.preethibhaskar.itunesforandroid.loaders.DownloadItunesDataLoader;
import com.example.preethibhaskar.itunesforandroid.utils.Constants;
import com.example.preethibhaskar.itunesforandroid.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the main launcher activity which displays the results from itunes api in Recycle view .
 */
public class DisplayItunesMainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Itunes>> {
    private RecyclerView mItunesDisplayRecyclerView;
    private ItunesRecyclerviewAdapter mAdapter;
    private ArrayList<Itunes> mDataSet;
    private final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_itunes_main);
        mItunesDisplayRecyclerView = (RecyclerView) findViewById(R.id.itunes_display_recycler_view);
        mItunesDisplayRecyclerView.setLayoutManager(mLayoutManager);
        // Recycle view does not have onclick listener we have created custom listener
        mItunesDisplayRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(),
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (mDataSet != null && mDataSet.size() > position) {
                                    Intent intent = new Intent(getApplicationContext(),
                                            ItunesDetailActivity.class);
                                    intent.putExtra(Constants.PASS_DATA, mDataSet.get(position));
                                    intent.putExtra(Constants.COUNT, position);
                                    startActivity(intent);
                                }

                            }
                        })
        );
        // Initialize the image cache to store the images
        Util.initializeCache();
        getSupportLoaderManager().initLoader(0, null, this);
    }



    @Override
    public Loader<List<Itunes>> onCreateLoader(int id, Bundle args) {
        return new DownloadItunesDataLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Itunes>> loader, List<Itunes> data) {
        mDataSet = (ArrayList<Itunes>) data;
        // If there is data , then load the data by setting the adapter with data
        if (data != null && data.size() > 0) {
            mAdapter = new ItunesRecyclerviewAdapter(getApplicationContext(), (ArrayList<Itunes>) data);
            mItunesDisplayRecyclerView.setAdapter(mAdapter);
        } else {
            TextView noInternetConnection = (TextView) findViewById(R.id.no_internet);
            noInternetConnection.setVisibility(View.VISIBLE);
            mItunesDisplayRecyclerView.setVisibility(View.GONE);
            // if no internet connection is detected then display the error message
            if (Util.hasNetworkConnection(getApplicationContext())) {
                // if we cannot reach the server then display the corresponding error message
                noInternetConnection.setText(getResources().getText(R.string.could_not_reach_server));
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Itunes>> loader) {

    }

}
