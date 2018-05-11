package com.application.cool.history.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.adapters.BaseFragmentPagerAdapter;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.util.App;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;


public class SearchFragment extends Fragment {

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private SearchView searchView;
    private TextView searchHint;
    private LinearLayout searchResultLayout;
    private ListView searchRecomList;

    @Override
    public void onResume() {
        super.onResume();
//        searchResultLayout.setVisibility(View.INVISIBLE);
//        searchHint.setVisibility(View.INVISIBLE);
//        searchRecomList.setVisibility(View.VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.search_viewpager);
        ScrollIndicatorView scrollIndicatorView = (ScrollIndicatorView) view.findViewById(R.id.search_indicator);

        searchView = (SearchView) view.findViewById(R.id.search);
        searchHint = (TextView) view.findViewById(R.id.search_hint);
        searchResultLayout = (LinearLayout) view.findViewById(R.id.search_result);
        searchRecomList = (ListView) view.findViewById(R.id.search_recommend);

        searchResultLayout.setVisibility(View.INVISIBLE);
        searchHint.setVisibility(View.INVISIBLE);
        searchRecomList.setVisibility(View.VISIBLE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                App.hideSoftKeyboard(getActivity());

                if (query.isEmpty()) {
                    searchRecomList.setVisibility(View.VISIBLE);
                    searchResultLayout.setVisibility(View.INVISIBLE);
                    searchHint.setVisibility(View.INVISIBLE);
                } else {
                    searchRecomList.setVisibility(View.INVISIBLE);
                    searchResultLayout.setVisibility(View.VISIBLE);

                    indicatorViewPager.setAdapter(
                            new BaseFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                                    inflate, getContext(), Constants.EDataSource.E_SEARCH, query));

                    searchHint.setVisibility(View.INVISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecomList.setVisibility(View.INVISIBLE);
                searchResultLayout.setVisibility(View.INVISIBLE);

                searchHint.setVisibility(View.VISIBLE);

                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchResultLayout.setVisibility(view.INVISIBLE);
                searchHint.setVisibility(view.INVISIBLE);

                searchRecomList.setVisibility(view.VISIBLE);

                App.hideSoftKeyboard(getActivity());
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResultLayout.setVisibility(view.INVISIBLE);
                searchHint.setVisibility(view.VISIBLE);

                searchRecomList.setVisibility(view.INVISIBLE);
            }
        });

        searchView.setQueryHint("请输入所要查询的关键词");

        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener()
                .setColor(0xFF2196F3, Color.GRAY).setSize(selectSize, unSelectSize));

        scrollIndicatorView.setScrollBar(new ColorBar(getContext(), 0xFF2196F3, 4));

        viewPager.setOffscreenPageLimit(2);
//        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
//        indicatorViewPager.setAdapter(new PagerAdapter());

        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        inflate = LayoutInflater.from(getActivity().getApplicationContext());

        return view;
    }

}