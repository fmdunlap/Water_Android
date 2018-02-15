package itp341.dunlap.forrest.water.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.adapters.CategoryListAdapter;
import itp341.dunlap.forrest.water.models.Category;
import itp341.dunlap.forrest.water.singletons.CategoryManager;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class QuestionCategoryFragment extends Fragment implements AdapterView.OnItemClickListener {

    private OnListFragmentInteractionListener mListener;

    String[] catStrings;
    ArrayList<Category> cats;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionCategoryFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QuestionCategoryFragment newInstance(int columnCount) {
        QuestionCategoryFragment fragment = new QuestionCategoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questioncategory_list, container, false);

        catStrings = getContext().getResources().getStringArray(R.array.categories);

        // Set the adapter
        ListView list = (ListView) view.findViewById(R.id.list);
        list.setOnItemClickListener(this);

        if(list.getAdapter() == null && savedInstanceState == null)
            list.setAdapter(new CategoryListAdapter(getContext(), CategoryManager.getInstance().getCategories()));
        return view;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QuestionFragment question = QuestionFragment.newInstance(((Category)view.getTag()).getAPIIndex());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.game_content_frame, question);
        ft.commit();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }




}
