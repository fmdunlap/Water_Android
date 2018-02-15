package itp341.dunlap.forrest.water.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.views.NavDrawerItem;

/**
 * Created by FDUNLAP on 4/27/2017.
 */

public class NavListAdapter extends ArrayAdapter<NavDrawerItem> {

    public NavListAdapter(@NonNull Context context, ArrayList<NavDrawerItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NavDrawerItem item = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_navdrawer, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.navdrawer_textview);
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.navdrawer_icon);

        tvTitle.setText(item.getTitle());
        ivIcon.setImageDrawable(item.getIcon());



        return convertView;
    }
}
