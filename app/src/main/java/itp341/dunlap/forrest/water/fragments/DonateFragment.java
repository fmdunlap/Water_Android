package itp341.dunlap.forrest.water.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.helpers.CurrencyFormatInputFilter;
import itp341.dunlap.forrest.water.views.QuestionTextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DonateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DonateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonateFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton menuFAB;
    private EditText otherEditText;
    private QuestionTextView oneDollarButton;
    private QuestionTextView fiveDollarButton;
    private QuestionTextView tenDollarButton;
    private QuestionTextView twentyfiveDollarButton;
    private QuestionTextView purchaseButton;


    public DonateFragment() {
        // Required empty public constructor
    }

    public static DonateFragment newInstance() {
        DonateFragment fragment = new DonateFragment();
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


        View view = inflater.inflate(R.layout.fragment_donate, container, false);

        menuFAB = (FloatingActionButton) view.findViewById(R.id.menu_fab);
        menuFAB.setOnClickListener(this);

        otherEditText = (EditText) view.findViewById(R.id.donate_other_et);
        otherEditText.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
        otherEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        oneDollarButton = (QuestionTextView) view.findViewById(R.id.one_dollar_button);
        oneDollarButton.setOnClickListener(this);

        fiveDollarButton = (QuestionTextView) view.findViewById(R.id.five_dollar_button);
        fiveDollarButton.setOnClickListener(this);

        tenDollarButton = (QuestionTextView) view.findViewById(R.id.ten_dollar_button);
        tenDollarButton.setOnClickListener(this);

        twentyfiveDollarButton = (QuestionTextView) view.findViewById(R.id.twentyfive_dollar_button);
        twentyfiveDollarButton.setOnClickListener(this);

        purchaseButton = (QuestionTextView) view.findViewById(R.id.donate_button);
        purchaseButton.setOnClickListener(this);

        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onMenuButtonPressed() {
        if(mListener != null)
            mListener.onDonateInteraction(getResources().getInteger(R.integer.menu_drawer_action), 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == menuFAB.getId()) onMenuButtonPressed();
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
        void onDonateInteraction(int action, double val);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
