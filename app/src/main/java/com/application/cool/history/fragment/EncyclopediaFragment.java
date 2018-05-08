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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.adapters.RecordListAdapter;
import com.application.cool.history.util.DisplayUtil;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

public class EncyclopediaFragment extends Fragment {

    private IndicatorViewPager indicatorViewPager;

    private RecordListAdapter adapter;

    private LayoutInflater inflate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_encyclopedia, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.encyclopedia_viewpager);
        ScrollIndicatorView scrollIndicatorView = (ScrollIndicatorView) view.findViewById(R.id.encyclopedia_indicator);

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
        indicatorViewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager()));

        return view;
    }


    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        private String[] tabTile = {"人物", "全部", "事件", "地理", "艺术", "科技"};
//        private String[] text = {"人物", "全部", "事件", "考古", "科技", "地理", "艺术"};

        @Override
        public int getCount() {
            return tabTile.length;
        }

//        @Override
//        public View getViewForTab(int position, View convertView, ViewGroup container) {
//            if (convertView == null) {
//                convertView = getLayoutInflater().inflate(R.layout.encyclopedia_tab_title, container, false);
//            }
//            TextView textView = (TextView) convertView;
//            textView.setText(tabTile[position]);
//
//            int witdh = getTextWidth(textView);
//            int padding = DisplayUtil.dipToPix(getContext(), 8);
//            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
//            //1.3f是根据上面字体大小变化的倍数1.3f设置
//            textView.setWidth((int) (witdh * 1.3f) + padding);
//
//            return convertView;
//        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.encyclopedia_tab_title, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabTile[position % tabTile.length]);
            int padding = DisplayUtil.dipToPix(getContext(),10);
            textView.setPadding(padding, 0, padding, 0);
            return convertView;
        }

//        @Override
//        public View getViewForPage(int position, View convertView, ViewGroup container) {
//            if (convertView == null) {
//                convertView = new TextView(container.getContext());
//            }
//            TextView textView = (TextView) convertView;
//            textView.setText(text[position]);
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextColor(Color.GRAY);
//            return convertView;
//        }

        @Override
        public Fragment getFragmentForPage(int position) {
            MoreFragment fragment = new MoreFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(MoreFragment.INTENT_INT_INDEX, position);

//            bundle.putString(MoreFragment.INTENT_TYPE, tabTile[position]);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            //这是ViewPager适配器的特点,有两个值 POSITION_NONE，POSITION_UNCHANGED，默认就是POSITION_UNCHANGED,
            // 表示数据没变化不用更新.notifyDataChange的时候重新调用getViewForPage
            return PagerAdapter.POSITION_NONE;
        }

        private int getTextWidth(TextView textView) {
            if (textView == null) {
                return 0;
            }
            Rect bounds = new Rect();
            String text = textView.getText().toString();
            Paint paint = textView.getPaint();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int width = bounds.left + bounds.width();
            return width;
        }

    }

}
