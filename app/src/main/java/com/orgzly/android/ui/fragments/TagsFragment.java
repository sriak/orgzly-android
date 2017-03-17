package com.orgzly.android.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.orgzly.BuildConfig;
import com.orgzly.R;
import com.orgzly.android.Shelf;
import com.orgzly.android.ui.ActionModeListener;
import com.orgzly.android.ui.FragmentListener;
import com.orgzly.android.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by raph on 2/21/17.
 */

public class TagsFragment extends ListFragment {
    private static final String TAG = FiltersFragment.class.getName();

    public static final String FRAGMENT_TAG = FiltersFragment.class.getName();

    private TagsFragmentListener mListener;

    protected Shelf mShelf;

    public static TagsFragment getInstance() {
        return new TagsFragment();
    }

    @Override
    public void onAttach(Context context) {
        if (BuildConfig.LOG_DEBUG) LogUtils.d(TAG, context);
        super.onAttach(context);

        /* This makes sure that the container activity has implemented
         * the callback interface. If not, it throws an exception
         */
        try {
            mListener = (TagsFragmentListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement " + BookFragment.BookFragmentListener.class);
        }
    }

    @Override
    public void onDetach() {
        if (BuildConfig.LOG_DEBUG) LogUtils.d(TAG);
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onResume() {
        if (BuildConfig.LOG_DEBUG) LogUtils.d(TAG);
        super.onResume();

        announceChangesToActivity();
    }

    private void announceChangesToActivity() {
        if (mListener != null) {
            mListener.announceChanges(
                    FRAGMENT_TAG,
                    getString(R.string.tags),
                    null,
                    getListView().getCheckedItemCount());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tags,container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShelf = new Shelf(getActivity().getApplicationContext());
        List<String> knownTags = new ArrayList<>(Arrays.asList(mShelf.getAllTags(0)));
        Collections.sort(knownTags, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.toLowerCase().compareTo(o2.toLowerCase());
            }
        });

        /* White text on light gray background, when using android.R.layout.simple_dropdown_item_1line
         * See https://code.google.com/p/android/issues/detail?id=5237#c8
         */
        // ArrayAdapter <String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, knownTags);
        ArrayAdapter <String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, knownTags);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String tag = (String) l.getAdapter().getItem(position);
        mListener.onFilterRequest(tag);
    }

    public interface TagsFragmentListener extends FragmentListener {
        void onFilterRequest(String filter);
    }
}
