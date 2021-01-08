package iss.nus.androidgame;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds;

    // Constructor
//    public ImageAdapter(Context c, Integer[] i) {
//        mContext = c;
//        mThumbIds = i;
//    }
//
    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
//    public View getView(int position, View convertView, ViewGroup parent) {
//        MemoryImageView imageView;
//
//        if (convertView == null) {
//            imageView = new MemoryImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setId(position + 1);
//            imageView.setPadding(10, 10, 10, 10);
//        }
//        else
//        {
//            imageView = (MemoryImageView) convertView;
//        }
//
//        imageView.setBackgroundResource(mThumbIds[position]);
//
//        return imageView;
//    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        MemoryImageView imageView;

        if (convertView == null) {
            imageView = new MemoryImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setId(position + 1);

            Integer pos = position;
            imageView.setTag("pic" + pos.toString());
            imageView.setPadding(10, 10, 10, 10);
        }
        else
        {
            imageView = (MemoryImageView) convertView;
        }

        imageView.setBackgroundResource(R.drawable.qnmark);

        return imageView;
    }

}

