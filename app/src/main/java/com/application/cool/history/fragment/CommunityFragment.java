package com.application.cool.history.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.adapters.BaseFragmentPagerAdapter;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.models.State;
import com.application.cool.history.util.DisplayUtil;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.HashSet;
import java.util.List;


public class CommunityFragment extends Fragment {

    private IndicatorViewPager indicatorViewPager;

    private LayoutInflater inflate;

//    private String[] tabTitle = {"关注", "人物", "事件", "地理", "艺术", "科技"};


    @Override
    public void onResume() {
        super.onResume();
    }

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

        viewPager.setOffscreenPageLimit(2);

        UserManager userManager = UserManager.getSharedInstance(getContext());

        if (userManager.isLogin()) {
            State.currentSubscribeTopics.clear();
            List<String> users = userManager.getSubscribeTopics(userManager.currentUser());
            State.currentSubscribeTopics = new HashSet<>(users);
        } else {
            State.currentSubscribeTopics.clear();
        }

        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        inflate = LayoutInflater.from(getActivity().getApplicationContext());
        indicatorViewPager.setAdapter(new BaseFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                inflate, getContext(), Constants.EDataSource.E_TOPIC));

        return view;
    }
}
