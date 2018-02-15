package itp341.dunlap.forrest.water.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.github.clans.fab.FloatingActionButton;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.fragments.QuestionCategoryFragment;
import itp341.dunlap.forrest.water.fragments.QuestionFragment;

/*
    MAIN GAME ACTIVITY
    Activity used to encapsulate all of the game tasks.
 */

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private QuestionCategoryFragment mCategoryFragment;

    private FrameLayout contentFrame;

    FloatingActionButton backArrow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if(getSupportFragmentManager().findFragmentById(R.id.game_content_frame) != null){
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.game_content_frame));
        }

        contentFrame = (FrameLayout) findViewById(R.id.game_content_frame);

        mCategoryFragment = new QuestionCategoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.game_content_frame, mCategoryFragment).commit();

        backArrow = (FloatingActionButton) findViewById(R.id.back_fab);
        backArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //If the current fragment is the question fragment...
        //get rid of the textview for drops
        if(getSupportFragmentManager().findFragmentById(R.id.game_content_frame) instanceof QuestionFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.game_content_frame, mCategoryFragment).commit();
            LinearLayout linLay = (LinearLayout) findViewById(R.id.linLay_game_action_bar);

            int numChildren = linLay.getChildCount();

            for(int i = 1; i < numChildren; i++){
                linLay.removeViewAt(i);
            }
        }
        else
            onBackPressed();
    }
}
