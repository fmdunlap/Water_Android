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
import itp341.dunlap.forrest.water.models.Category;

/**
 * Created by FDUNLAP on 4/30/2017.
 */

public class CategoryListAdapter extends ArrayAdapter<Category> {
    public CategoryListAdapter(@NonNull Context context, ArrayList<Category> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Category item = getItem(position);

        if(item.isGeneral()){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category_title, parent, false);
            TextView generalTitle = (TextView) convertView.findViewById(R.id.categories_title_view);
            generalTitle.setText(item.getTitle());
            convertView.setClickable(false);
            ImageView iconIV = (ImageView) convertView.findViewById(R.id.categories_icon);
            if(item.getIcon() != null && iconIV != null) {
                iconIV.setImageDrawable(item.getIcon());
            }
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_category, parent, false);

            TextView tvTitle = (TextView) convertView.findViewById(R.id.categories_title_view);

            tvTitle.setText(item.getTitle());
        }



        convertView.setTag(item);

        return convertView;
    }
}
