package com.application.cool.history.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.cool.history.R;
import com.application.cool.history.adapters.BaseFragmentPagerAdapter;
import com.application.cool.history.constants.Constants;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

/**
 * Created by Zhenyuan Shen on 5/9/18.
 */

public class BookmarkFragment extends Fragment {

    public static final String INTENT_USER_INDEX = "intent_user_id";

    private IndicatorViewPager indicatorViewPager;

    private LayoutInflater inflate;

    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.base_viewpager);
        ScrollIndicatorView scrollIndicatorView = (ScrollIndicatorView) view.findViewById(R.id.base_indicator);

        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener()
                .setColor(0xFF2196F3, Color.GRAY).setSize(selectSize, unSelectSize));

        scrollIndicatorView.setScrollBar(new ColorBar(getContext(), 0xFF2196F3, 4));

        // viewPager.setOffscreenPageLimit(0);


        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        inflate = LayoutInflater.from(getActivity().getApplicationContext());


        userId =  getArguments().getString(INTENT_USER_INDEX);

        indicatorViewPager.setAdapter(
                new BaseFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                        inflate, getContext(), Constants.EDataSource.E_BOOKMARK, userId));

        return view;
    }
}
