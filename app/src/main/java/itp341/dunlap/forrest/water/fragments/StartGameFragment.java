package itp341.dunlap.forrest.water.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.activities.MainActivity;

public class StartGameFragment extends Fragment implements View.OnClickListener {

    Button startGameButton;
    FloatingActionButton menuFAB;
    ImageView aboutButton;

    public StartGameFragment() {
        // Required empty public constructor
    }
    public static StartGameFragment newInstance() {
        StartGameFragment fragment = new StartGameFragment();
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
        View v = inflater.inflate(R.layout.fragment_startgame, container, false);

        startGameButton = (Button) v.findViewById(R.id.game_start_button);
        startGameButton.setOnClickListener(this);

        menuFAB = (FloatingActionButton) v.findViewById(R.id.menu_fab);
        menuFAB.setOnClickListener(this);


        aboutButton = (ImageView) v.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);

        return v;
    }

    //TODO: Implement about and leaderboard pages.

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == startGameButton.getId())
        ((MainActivity) getActivity()).startPressed();
        if(v.getId() == menuFAB.getId())
            ((MainActivity)getActivity()).onQuestionInteraction(getResources().getInteger(R.integer.menu_drawer_action));
        if(v.getId() == aboutButton.getId())
            startAboutDialog();
    }

    private void startAboutDialog() {
        AboutDialogFragment aboutFrag = AboutDialogFragment.newInstance(getString(R.string.about_water));
        aboutFrag.show(getActivity().getSupportFragmentManager(), "about");
    }


}

