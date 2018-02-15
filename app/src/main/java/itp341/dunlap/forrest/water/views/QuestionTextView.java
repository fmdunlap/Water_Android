package itp341.dunlap.forrest.water.views;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import itp341.dunlap.forrest.water.R;

/**
 * Created by FDUNLAP on 4/26/2017.
 */

public class QuestionTextView extends android.support.v7.widget.AppCompatTextView {

    public QuestionTextView(Context context) {
        super(context);
        setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.question_text_selector, null));
    }

    public QuestionTextView(Context context, AttributeSet attrs){
        super(context, attrs);

        setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.question_text_selector, null));

    }
    public QuestionTextView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.question_text_selector, null));
    }
}
