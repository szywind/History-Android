package com.application.cool.history.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.cool.history.R;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

public class EncyclopediaFragment extends Fragment {

    private IndicatorViewPager indicatorViewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encyclopedia, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.encyclopedia_viewpager);
        ScrollIndicatorView scrollIndicatorView = (ScrollIndicatorView) view.findViewById(R.id.encyclopedia_indicator);
        float unSelectSize = 12;
        float selectSize = unSelectSize * 1.3f;
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener()
                .setColor(0xFF2196F, Color.GRAY));
        viewPager.setOffscreenPageLimit(2);
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        indicatorViewPager.setAdapter(new MyAdapter());
        return view;
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
        private String[] tabTile = {"人物", "全部", "事件", "考古", "科技", "地理", "艺术", "政治"};
        private String[] text = {"人物", "全部", "事件", "考古", "科技", "地理", "艺术", "政治"};

        @Override
        public int getCount() {
            return tabTile.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(R.layout.encyclopedia_tab_title, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabTile[position]);

            int width = getTextWidth(textView);
            int padding = dipToPix(getContext(), 8);
            //因为wrap的布局 字体大小变化会导致textView大小变化产生抖动，这里通过设置textView宽度就避免抖动现象
            //1.3f是根据上面字体大小变化的倍数1.3f设置
            textView.setWidth((int) (width * 1.3f) + padding);
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new TextView(container.getContext());

            }
            //To do
            TextView textView = (TextView) convertView;
            textView.setText(text[position]);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.GRAY);
            return convertView;
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

        @Override
        public int getItemPosition(Object object) {
//            return super.getItemPosition(object);
            // to do
            return PagerAdapter.POSITION_UNCHANGED;
        }

        private  int dipToPix(Context context, int dip) {
            int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
            return size;
        }

    }

}
