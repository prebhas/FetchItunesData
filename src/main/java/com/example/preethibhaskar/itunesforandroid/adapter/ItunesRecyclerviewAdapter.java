package com.example.preethibhaskar.itunesforandroid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.preethibhaskar.itunesforandroid.R;
import com.example.preethibhaskar.itunesforandroid.data.Itunes;
import com.example.preethibhaskar.itunesforandroid.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 10/28/15.
 */
public class ItunesRecyclerviewAdapter extends RecyclerView.Adapter<ItunesRecyclerviewAdapter.ItunesViewHolder> {

    private final List<Itunes> mDataSet;
    private final Context mContext;
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItunesViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private ImageView mImageView;
        private LinearLayout mLinearLayout;

        public ItunesViewHolder(View v) {
            super(v);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linear_container);
            mTitleTextView = (TextView) v.findViewById(R.id.title);
            mDescriptionTextView = (TextView) v.findViewById(R.id.description);
            mImageView = (ImageView) v.findViewById(R.id.image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItunesRecyclerviewAdapter(Context context, ArrayList<Itunes> myDataset) {
        mDataSet = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItunesRecyclerviewAdapter.ItunesViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_itunes_recycler_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ItunesViewHolder vh = new ItunesViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ItunesViewHolder holder, int position) {
        Itunes data = mDataSet.get(position);
        holder.mDescriptionTextView.setText(data.getDescription());
        holder.mTitleTextView.setText(data.getTitle());
        if (!Util.hasNetworkConnection(mContext)) {
            Bitmap bitmap = Util.decodeBitmapFromBase64(data.getImageUrl());
            if (bitmap != null)
                Util.addBitmapToMemoryCache(position, bitmap);
            holder.mImageView.setImageBitmap(bitmap);
        } else
            Util.download(mContext, data, holder.mImageView, position);
        // add the animationgit initgit init
        setAnimation(holder.mLinearLayout,position);
    }


    /**
     * This method adds the animation to our linear layout
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
