package itp341.dunlap.forrest.water.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.models.Question;
import itp341.dunlap.forrest.water.singletons.QuestionManager;
import itp341.dunlap.forrest.water.singletons.UserManager;
import itp341.dunlap.forrest.water.views.QuestionTextView;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link QuestionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionFragment extends Fragment implements View.OnClickListener{
    private static final int QUESTION_REQUEST_CODE = 50;
    private OnFragmentInteractionListener mListener;

    private QuestionTextView questionText;

    private LinearLayout gameActionBar;

    private TextView dropsText;

    private RelativeLayout mainLayout;
    private ProgressBar progressBar;

    private QuestionTextView resultView;


    RequestQueue queue;
    private FloatingActionButton drawerFAB;
    private LinearLayout answerButtonLayout;

    Question currentQuestion;

    public QuestionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(int categoryApiIndex) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt("index", categoryApiIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

//        drawerFAB = (FloatingActionButton) view.findViewById(R.id.menu_fab);
//        drawerFAB.setOnClickListener(this);

        gameActionBar = (LinearLayout) getActivity().findViewById(R.id.linLay_game_action_bar);
        gameActionBar.setGravity(Gravity.CENTER_VERTICAL);

        dropsText = createDropsText();

        gameActionBar.addView(dropsText);

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        new AdRequest.Builder().addTestDevice("3D05DC4385849EC3BE58616815C07132");
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        questionText = (QuestionTextView) view.findViewById(R.id.tv_question);
        queue = Volley.newRequestQueue(getContext());
        answerButtonLayout = (LinearLayout) view.findViewById(R.id.answer_button_linear_layout);

        QuestionManager.getInstance().clearAndLoadNewQuestions(getApiCat(), queue, this);

        progressBar = (ProgressBar) view.findViewById(R.id.question_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        mainLayout = (RelativeLayout) view.findViewById(R.id.question_relative_layout);
        mainLayout.setVisibility(View.INVISIBLE);

        resultView = (QuestionTextView) view.findViewById(R.id.correct_view);

        return view;
    }

    private TextView createDropsText() {
        TextView dt = new TextView(this.getActivity().getApplicationContext());
        dt.setText(getString(R.string.tv_raised_default).replace("#",((Integer)UserManager.getInstance().getCurrentUser().getDrops()).toString()));
        dt.setGravity(Gravity.CENTER_HORIZONTAL);
        dt.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        dt.setTextSize(20.0f);
        return dt;
    }

    private void updateDropsText(TextView drops){
        drops.setText(getString(R.string.tv_raised_default).replace("#",((Integer)UserManager.getInstance().getCurrentUser().getDrops()).toString()));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onMenuButtonPressed() {
        if (mListener != null) {
            mListener.onQuestionInteraction(getResources().getInteger(R.integer.menu_drawer_action));
        }
    }

    private int getApiCat(){
        return getArguments().getInt("index",0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getTag() == currentQuestion) {

            QuestionTextView b = (QuestionTextView) v;

            if (b.getText().toString().compareTo(currentQuestion.getCorrectAnswer()) == 0) {
                //true implies that the user selected the correct answer
                showAnswer(true);
            } else {
                showAnswer(false);
            }

            nextQuestion(false);
        }
    }

    private void showAnswer(boolean userWasCorrect) {
        answerButtonLayout.removeAllViews();
        answerButtonLayout.setVisibility(View.GONE);

        if(userWasCorrect){
            resultView.setText(getString(R.string.correct));
            UserManager.getInstance().addDrops(1);
            updateDropsText(dropsText);
        } else {
            resultView.setText(getString(R.string.incorrect));
        }

        resultView.setVisibility(View.VISIBLE);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // Hide your View after 3 seconds
                resultView.setVisibility(View.GONE);
                answerButtonLayout.setVisibility(View.VISIBLE);
                questionText.setText(currentQuestion.getText());
            }
        }, 1000);
    }

    public void onFinishQuestionLoad() {
        progressBar.setVisibility(View.INVISIBLE);
        mainLayout.setVisibility(View.VISIBLE);


        nextQuestion(true);

    }

    private void nextQuestion(boolean displayTextImmediately){


        currentQuestion = QuestionManager.getInstance().nextQuestion(queue);
        if(displayTextImmediately)
            questionText.setText(currentQuestion.getText());

        int numAnswers = currentQuestion.getIncAnswers().size() + 1;

        Vector<QuestionTextView> buttonsToBeAdded = new Vector<>();


        //If this isn't a true/false question
        if(currentQuestion.getIncAnswers().size() != 1) {

            ArrayList<QuestionTextView> buttons = new ArrayList<>();

            for(int i = 0; i < numAnswers; i++){
                QuestionTextView b = new QuestionTextView(getContext());

                //If we've exhausted the incorrect answers add the correct answer
                if(i == numAnswers-1) {
                    b.setText(currentQuestion.getCorrectAnswer());
                } else {
                    b.setText(currentQuestion.getIncAnswers().get(i));
                }

                b.setTag(currentQuestion);
                b.setOnClickListener(this);

                buttons.add(b);
            }

            Collections.shuffle(buttons);
            for(int i = 0; i < buttons.size(); i++){
                buttonsToBeAdded.add(buttons.get(i));
            }

        } else {
            //Add true, then false.
            QuestionTextView tButton = new QuestionTextView(getContext());
            QuestionTextView fButton = new QuestionTextView(getContext());

            Log.d(null, "Correct Answer: " + currentQuestion.getCorrectAnswer());

            tButton.setText(getString(R.string.true_button_text));
            fButton.setText(getString(R.string.false_button_text));

            tButton.setTag(currentQuestion);
            fButton.setTag(currentQuestion);

            tButton.setOnClickListener(this);
            fButton.setOnClickListener(this);

            buttonsToBeAdded.add(tButton);
            buttonsToBeAdded.add(fButton);
        }

        for(int i = 0; i < buttonsToBeAdded.size(); i++){
            buttonsToBeAdded.get(i).setTextSize(20f);
            buttonsToBeAdded.get(i).setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,25,0,25);
            buttonsToBeAdded.get(i).setLayoutParams(params);

            int widthDP = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    300,
                    getContext().getResources().getDisplayMetrics()
            );

            int heightDP = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    60,
                    getContext().getResources().getDisplayMetrics()
            );

            buttonsToBeAdded.get(i).setWidth(widthDP);
            buttonsToBeAdded.get(i).setHeight(heightDP);
            buttonsToBeAdded.get(i).setTextColor(Color.BLACK);
            answerButtonLayout.addView(buttonsToBeAdded.get(i));
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onQuestionInteraction(int action);
    }


}
