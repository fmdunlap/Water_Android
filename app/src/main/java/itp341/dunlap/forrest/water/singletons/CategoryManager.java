package itp341.dunlap.forrest.water.singletons;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.activities.App;
import itp341.dunlap.forrest.water.models.Category;

/**
 * Created by FDUNLAP on 4/27/2017.
 */

public class CategoryManager {

    ArrayList<Category> category_list;
    HashMap<Category, Integer> category_API_indexes;
    String[] catStrings;

    private static final CategoryManager ourInstance = new CategoryManager();

    public static CategoryManager getInstance() {
        return ourInstance;
    }

    private CategoryManager() {
        category_list = new ArrayList<>();
        category_API_indexes = new HashMap<>();
    }

    public ArrayList<Category> getCategories() {
        return category_list;
    }

    public Category getCategoryAt(int i){
        if(category_list.size() < i){
            return category_list.get(i);
        }
        return null;
    }

    public void giveStrings(String[] c){
        catStrings = c;
        populateCategories();
    }


    private void populateCategories(){
        for(String str : catStrings){
            String title;
            boolean isGeneral = false;
            int index;
            Drawable icon;

            if(str.startsWith("gen_")){
                isGeneral = true;
            }
            title = str.substring(str.lastIndexOf("_")+1);

            index = lookUpIndex(title);
            icon = lookUpIcon(title);
            Category c = new Category(title,isGeneral,index);
            c.setIcon(icon);
            category_list.add(c);
        }
    }

    private Drawable lookUpIcon(String title) {
        switch(title){
            case "General":
                return App.getContext().getDrawable(R.drawable.ic_category_gk);
            case "Books":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Entertainment":
                return App.getContext().getDrawable(R.drawable.ic_category_film);
            case "Science":
                return App.getContext().getDrawable(R.drawable.ic_category_beaker);
            case "Other":
                return App.getContext().getDrawable(R.drawable.ic_category_dots);
            case "Comics":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Television":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Video Games":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Board Games":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Science and Nature":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Computers":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Mathematics":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Mythology":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Sports":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Geography":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "History":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Politics":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Art":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Celebrities":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Animals":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Vehicles":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            case "Gadgets":
                return App.getContext().getDrawable(R.drawable.ic_category_book);
            default:
                return App.getContext().getDrawable(R.drawable.ic_category_book);

        }
    }

    private int lookUpIndex(String title) {

        switch(title){
            case "General Knowledge":
                return 9;
            case "Books":
                return 10;
            case "Film":
                return 11;
            case "Music":
                return 12;
            case "Musicals and Theater":
                return 13;
            case "Comics":
                return 29;
            case "Television":
                return 14;
            case "Video Games":
                return 15;
            case "Board Games":
                return 16;
            case "Science and Nature":
                return 17;
            case "Computers":
                return 18;
            case "Mathematics":
                return 19;
            case "Mythology":
                return 20;
            case "Sports":
                return 21;
            case "Geography":
                return 22;
            case "History":
                return 23;
            case "Politics":
                return 24;
            case "Art":
                return 25;
            case "Celebrities":
                return 26;
            case "Animals":
                return 27;
            case "Vehicles":
                return 28;
            case "Gadgets":
                return 30;
            default:
                return 0;

        }
    }
}
