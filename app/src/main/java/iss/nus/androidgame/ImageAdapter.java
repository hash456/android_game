package iss.nus.androidgame;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.HashMap;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] mThumbIds;
    private HashMap<Integer, MemoryImageView> lookup = new HashMap<>();

    // Constructor
    public ImageAdapter(Context c, Integer[] i) {
        mContext = c;
        mThumbIds = i;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public MemoryImageView getSelectedImageView(int position) {
        return lookup.get(position);
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        MemoryImageView imageView;

        if (convertView == null) {
            imageView = new MemoryImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setId(View.generateViewId());
            imageView.setPadding(10, 10, 10, 10);
        }
        else
        {
            imageView = (MemoryImageView) convertView;
        }

        imageView.setBackgroundResource(mThumbIds[position]);
        lookup.put(position, imageView);

        return imageView;
    }
}

