package itp341.dunlap.forrest.water.views;

import android.content.Context;
import android.graphics.drawable.Drawable;

import itp341.dunlap.forrest.water.R;

/**
 * Created by FDUNLAP on 4/27/2017.
 */

public class NavDrawerItem {

    public String mTitle;
    public Drawable mIcon;
    public type mType;
    public Context context;

    public enum type{
        Home,
        Map,
        Profile,
        Donate,
        Settings
    }

    public NavDrawerItem(String title, type type, Context context){
        this.context = context;

        mTitle = title;
        mType = type;

        setIcon();
    }

    private void setIcon() {

        switch(mType){
            case Home:
                mIcon = context.getDrawable(R.drawable.ic_action_home);
                break;
            case Map:
                mIcon = context.getDrawable(R.drawable.ic_action_map);
                break;
            case Profile:
                mIcon = context.getDrawable(R.drawable.ic_action_person);
                break;
            case Donate:
                mIcon = context.getDrawable(R.drawable.ic_action_waterdrop);
                break;
            case Settings:
                mIcon = context.getDrawable(R.drawable.ic_action_settings);
                break;
        }

    }

    public Drawable getIcon() {
        return mIcon;
    }

    public String getTitle() {
        return mTitle;
    }
}
