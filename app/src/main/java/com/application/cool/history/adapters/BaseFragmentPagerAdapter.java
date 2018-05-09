package com.application.cool.history.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.fragment.CommunitySubFragment;
import com.application.cool.history.fragment.EncyclopediaSubFragment;
import com.application.cool.history.models.Post;
import com.application.cool.history.util.DisplayUtil;
import com.shizhefei.view.indicator.IndicatorViewPager;

/**
 * Created by Zhenyuan Shen on 5/9/18.
 */
public class BaseFragmentPagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {

    private Context context;
    private LayoutInflater inflate;
    private String[] tabTitle;
    private Constants.EDataSource dataSource;

    public BaseFragmentPagerAdapter(FragmentManager fragmentManager,
                                    LayoutInflater inflate, Context context, Constants.EDataSource dataSource) {
        super(fragmentManager);

        this.context = context;
        this.inflate = inflate;
        this.dataSource = dataSource;

        switch (dataSource) {
            case E_RECORD:
                // TODO
                String[] recordTitle = {"人物", "全部", "事件", "地理", "艺术", "科技"};
                this.tabTitle = recordTitle;
                break;

            case E_POST:
                // TODO
                String[] postTitle = {"关注", "人物", "事件", "地理", "艺术", "科技"};
                this.tabTitle = postTitle;
                break;

            case E_USER:
                // TODO
                String[] userTitle = {"关注者", "正在关注", "最热用户", "可能喜欢"};
                this.tabTitle = userTitle;
                break;

            default:
                break;
        }
    }


    @Override
    public int getCount() {
        return tabTitle.length;
    }


    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.tabbar_title, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(tabTitle[position % tabTitle.length]);
        int padding = DisplayUtil.dipToPix(context,10);
        textView.setPadding(padding, 0, padding, 0);
        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        Bundle bundle = new Bundle();

        switch (dataSource) {
            case E_RECORD:
                EncyclopediaSubFragment encyclopediaSubFragment = new EncyclopediaSubFragment();
                bundle.putInt(EncyclopediaSubFragment.INTENT_INT_INDEX, position);
                encyclopediaSubFragment.setArguments(bundle);
                return encyclopediaSubFragment;

            case E_POST:
                CommunitySubFragment communitySubFragment = new CommunitySubFragment();
                bundle.putInt(CommunitySubFragment.INTENT_INT_INDEX, position);
                communitySubFragment.setArguments(bundle);
                return communitySubFragment;

            default:
                return null;

        }



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
