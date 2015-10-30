package com.example.preethibhaskar.itunesforandroid.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.preethibhaskar.itunesforandroid.R;
import com.example.preethibhaskar.itunesforandroid.data.Itunes;

import com.example.preethibhaskar.itunesforandroid.utils.Constants;
import com.example.preethibhaskar.itunesforandroid.utils.Util;

public class ItunesDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itunes_detail);
        if (getIntent() != null) {
            Itunes data = getIntent().getParcelableExtra(Constants.PASS_DATA);
            TextView title = (TextView) findViewById(R.id.itunes_detail_title_tv);
            title.setText(data.getTitle());

            TextView description = (TextView) findViewById(R.id.itunes_description_tv);
            description.setText(data.getDescription());

            TextView collectionName = (TextView) findViewById(R.id.itunes_collection_name_tv);
            collectionName.setText(data.getCollectionName());

            TextView collectionPrice = (TextView) findViewById(R.id.itunes_collection_price_tv);
            collectionPrice.setText(data.getCollectionPrice());

            ImageView imageView = (ImageView) findViewById(R.id.itunes_detail_image_view);
            int pos = getIntent().getIntExtra(Constants.COUNT, -1);
            Bitmap bm = null;
            // Set the image from bitmap if available
            if (pos != -1)
                bm = Util.getBitmapFromMemCache(pos);
            if (bm != null) {
                imageView.setImageBitmap(bm);
            } else
                // Download the image if there is internet connection
                if (Util.hasNetworkConnection(getApplicationContext()))
                    Util.download(getApplicationContext(), data, imageView, pos);


        }

    }
}
