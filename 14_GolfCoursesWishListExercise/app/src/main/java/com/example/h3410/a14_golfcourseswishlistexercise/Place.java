package com.example.h3410.a14_golfcourseswishlistexercise;

import android.content.Context;

/**
 * Created by H3410 on 11.10.2017.
 */

public class Place {
    public String name;
    public String imageName;


    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }
}
