package itp341.dunlap.forrest.water.models;

import android.graphics.drawable.Drawable;

/**
 * Created by FDUNLAP on 4/27/2017.
 */

public class Category {

    boolean mIsGeneral;
    String mTitle;
    int mAPIIndex;
    //TODO get icons for all categories.
    Drawable mIcon;

    public Category(String title, boolean isGeneral, int index){
        mTitle = title;
        mIsGeneral = isGeneral;
        mAPIIndex = index;
    }

    public int getAPIIndex() {
        return mAPIIndex;
    }

    public String getTitle(){ return mTitle; }
    public boolean isGeneral(){return mIsGeneral;}

    public void setIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public Drawable getIcon() {
        return mIcon;
    }
}
