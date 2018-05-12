package com.application.cool.history.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.UserManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.fragment.BookmarkSubFragment;
import com.application.cool.history.fragment.CommunitySubFragment;
import com.application.cool.history.fragment.EncyclopediaSubFragment;
import com.application.cool.history.fragment.ForumSubFragment;
import com.application.cool.history.fragment.SearchSubFragment;
import com.application.cool.history.fragment.SocialSubFragment;
import com.application.cool.history.models.Post;
import com.application.cool.history.models.Record;
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

    private String topicName;
    private String searchWord;
    private String userId;

    public BaseFragmentPagerAdapter(FragmentManager fragmentManager,
                                    LayoutInflater inflate, Context context,
                                    Constants.EDataSource dataSource) {

        this(fragmentManager, inflate, context, dataSource, null);
    }

    public BaseFragmentPagerAdapter(FragmentManager fragmentManager,
                                    LayoutInflater inflate, Context context,
                                    Constants.EDataSource dataSource, String word) {
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

            case E_TOPIC:
                // TODO
                if (com.application.cool.history.managers.UserManager.getSharedInstance(context).isLogin()) {
                    String[] topicTitle = {"关注", "人物", "事件", "地理", "艺术", "科技"};
                    this.tabTitle = topicTitle;

                } else {
                    String[] topicTitle = {"人物", "事件", "地理", "艺术", "科技"};
                    this.tabTitle = topicTitle;

                }
                break;

            case E_POST:
                // TODO
                this.topicName = word;

                String[] postTitle = {"最新发布", "最多回复", "最多喜欢"};
                this.tabTitle = postTitle;
                break;

            case E_USER:
                // TODO
                String[] userTitle = {"关注者", "正在关注", "最热用户", "可能喜欢"};
                this.tabTitle = userTitle;
                break;

            case E_SEARCH:
                // TODO
                this.searchWord = word;

                String[] searchTitle = {"人物", "事件", "社区", "用户"};
                this.tabTitle = searchTitle;
                break;

            case E_BOOKMARK:
                // TODO
                this.userId = word;

                String[] bookmarkTitle = {"发帖", "回帖", "喜欢", "收藏"};
                this.tabTitle = bookmarkTitle;
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

            case E_TOPIC:
                CommunitySubFragment communitySubFragment = new CommunitySubFragment();
                bundle.putInt(CommunitySubFragment.INTENT_INT_INDEX, position);
                communitySubFragment.setArguments(bundle);
                return communitySubFragment;

            case E_POST:
                ForumSubFragment forumSubFragment = new ForumSubFragment();
                bundle.putInt(ForumSubFragment.INTENT_INT_INDEX, position);
                bundle.putString(ForumSubFragment.INTENT_TOPIC_NAME, topicName);
                forumSubFragment.setArguments(bundle);
                return forumSubFragment;

            case E_USER:
                SocialSubFragment socialSubFragment = new SocialSubFragment();
                bundle.putInt(SocialSubFragment.INTENT_INT_INDEX, position);
                socialSubFragment.setArguments(bundle);
                return socialSubFragment;

            case E_SEARCH:
                SearchSubFragment searchSubFragment = new SearchSubFragment();
                bundle.putInt(SearchSubFragment.INTENT_INT_INDEX, position);
                bundle.putString(SearchSubFragment.INTENT_SEARCH_WORD, searchWord);
                searchSubFragment.setArguments(bundle);
                return searchSubFragment;

            case E_BOOKMARK:
                BookmarkSubFragment bookmarkSubFragment = new BookmarkSubFragment();
                bundle.putInt(BookmarkSubFragment.INTENT_INT_INDEX, position);
                bundle.putString(BookmarkSubFragment.INTENT_USER_INDEX, userId);
                bookmarkSubFragment.setArguments(bundle);
                return bookmarkSubFragment;

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
