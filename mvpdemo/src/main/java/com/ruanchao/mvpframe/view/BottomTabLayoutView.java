package com.ruanchao.mvpframe.view;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ruanchao.mvpframe.R;

import java.util.ArrayList;
import java.util.List;

public class BottomTabLayoutView extends LinearLayout implements View.OnClickListener {

    Context mContext;
    ViewPager mViewPager;
    LinearLayout mMenuHomeLayout;
    ImageView mMenuHomeImage;
    TextView mMenuHomeText;
    LinearLayout mMenuNearbyLayout;
    ImageView mMenuNearbyImage;
    TextView mMenuNearbyText;
    LinearLayout mMenuDiscoverLayout;
    ImageView mMenuDiscoverImage;
    TextView mMenuDiscoverText;
    LinearLayout mMenuOrderLayout;
    ImageView mMenuOrderImage;
    TextView mMenuOrderText;
    LinearLayout mMenuMineLayout;
    ImageView mMenuMineImage;
    TextView mMenuMineText;

    @Override
    public void onClick(View v) {

        int i = v.getId();
        switch (i) {
            case R.id.ll_menu_home_page:
                mViewPager.setCurrentItem(0);
                selectTab(MenuTab.HOME);
                break;
            case R.id.ll_menu_nearby:
                mViewPager.setCurrentItem(1);
                selectTab(MenuTab.NEARBY);
                break;
            case R.id.ll_menu_discover:
                mViewPager.setCurrentItem(2);
                selectTab(MenuTab.DISCOVER);
                break;
            case R.id.ll_menu_mine:
                mViewPager.setCurrentItem(5);
                selectTab(MenuTab.MINE);
                break;
            case R.id.ll_menu_order:
                mViewPager.setCurrentItem(3);
                selectTab(MenuTab.ORDER);
                break;
        }
    }

    /**
     * tab的枚举类型
     */
    public enum MenuTab {
        HOME,
        NEARBY,
        DISCOVER,
        ORDER,
        MINE
    }

    public BottomTabLayoutView(Context context) {
        super(context);
        init(context);
    }

    public BottomTabLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomTabLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BottomTabLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 外部调用初始化，传入必要的参数
     *
     * @param fm
     */
    public void initView(FragmentManager fm, List<Fragment> fragmentList) {
        PagerAdapter pagerAdapter = new PagerAdapter(fm, fragmentList);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                //将ViewPager与下面的tab关联起来
                switch (position) {
                    case 0:
                        selectTab(MenuTab.HOME);
                        break;
                    case 1:
                        selectTab(MenuTab.NEARBY);
                        break;
                    case 2:
                        selectTab(MenuTab.DISCOVER);
                        break;
                    case 3:
                        selectTab(MenuTab.ORDER);
                        break;
                    case 4:
                        selectTab(MenuTab.MINE);
                        break;
                    default:
                        selectTab(MenuTab.HOME);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    private void init(Context context) {
        mContext = context;
        View view = View.inflate(context,R.layout.bottom_tab_layout, this);
        mViewPager = view.findViewById(R.id.vp_tab_widget);
        mMenuHomeLayout = view.findViewById(R.id.ll_menu_home_page);
        mMenuHomeLayout.setOnClickListener(this);
        mMenuHomeImage = view.findViewById(R.id.iv_menu_home);
        mMenuHomeText = view.findViewById(R.id.tv_menu_home);
        mMenuNearbyLayout = view.findViewById(R.id.ll_menu_nearby);
        mMenuNearbyLayout.setOnClickListener(this);
        mMenuNearbyImage = view.findViewById(R.id.iv_menu_nearby);
        mMenuNearbyText = view.findViewById(R.id.tv_menu_nearby);
        mMenuDiscoverLayout = view.findViewById(R.id.ll_menu_discover);
        mMenuDiscoverLayout.setOnClickListener(this);
        mMenuDiscoverImage = view.findViewById(R.id.iv_menu_discover);
        mMenuDiscoverText = view.findViewById(R.id.tv_menu_discover);
        mMenuOrderLayout = view.findViewById(R.id.ll_menu_order);
        mMenuOrderLayout.setOnClickListener(this);
        mMenuOrderImage = view.findViewById(R.id.iv_menu_order);
        mMenuOrderText = view.findViewById(R.id.tv_menu_order);
        mMenuMineLayout = view.findViewById(R.id.ll_menu_mine);
        mMenuMineLayout.setOnClickListener(this);
        mMenuMineImage = view.findViewById(R.id.iv_menu_mine);
        mMenuMineText = view.findViewById(R.id.tv_menu_mine);

        selectTab(MenuTab.HOME);
    }

    public void selectTab(MenuTab tab) {
        //先将所有tab取消选中，再单独设置要选中的tab
        unCheckedAll();

        if (tab == MenuTab.HOME){
            mMenuHomeLayout.setActivated(true);
        }else if (tab == MenuTab.DISCOVER){
            mMenuDiscoverLayout.setActivated(true);
        }else if (tab == MenuTab.MINE){
            mMenuMineLayout.setActivated(true);
        }else if (tab == MenuTab.NEARBY){
            mMenuNearbyLayout.setActivated(true);
        }else if (tab == MenuTab.ORDER){
            mMenuOrderLayout.setActivated(true);
        }
    }

    private void unCheckedAll() {
        mMenuHomeLayout.setActivated(false);
        mMenuDiscoverLayout.setActivated(false);
        mMenuMineLayout.setActivated(false);
        mMenuNearbyLayout.setActivated(false);
        mMenuOrderLayout.setActivated(false);
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> mFragmentList = new ArrayList();

        public PagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {

            return mFragmentList.size();
        }
    }
}
