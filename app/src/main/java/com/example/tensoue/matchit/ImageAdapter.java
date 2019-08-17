package com.example.tensoue.matchit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.github.siyamed.shapeimageview.ShapeImageView;

public class ImageAdapter extends BaseAdapter {
    private final Context context;
    private int numOfCards;
    private int width;
    private int height;
    private int image;


    public ImageAdapter(Context context) {
        this.context = context;
    }

    public  void setCount(int num) {
        this.numOfCards = num;
    }

    public  void setImage(int num) {
        this.image = num;
    }

    public  void setSize(int x, int y) {
        this.width = x;
        this.height = y;
    }

    @Override
    public int getCount() {
        return numOfCards;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RoundedImageView shapeImageView;
        if (convertView == null) {
            shapeImageView = new RoundedImageView(this.context);
            shapeImageView.setRadius(15);
            shapeImageView.setBorderWidth(5);
            shapeImageView.setSquare(true);
            shapeImageView.setBorderColor(R.color.colorSecondary);
            shapeImageView.setLayoutParams(new GridView.LayoutParams(this.width, this.height));
            shapeImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        }
        else
            shapeImageView = (RoundedImageView)convertView;
        shapeImageView.setImageResource(image);
        return shapeImageView;
    }
}