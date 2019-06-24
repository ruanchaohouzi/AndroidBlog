package com.ruanchao.androidblog.ui.collection

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.androidblog.R
import com.ruanchao.androidblog.adapter.PublicNumberListAdapter
import com.ruanchao.androidblog.bean.UserInfo
import com.ruanchao.androidblog.databinding.CollectionFragmentBinding
import com.ruanchao.androidblog.event.LoginUserMsg
import com.ruanchao.androidblog.ui.base.BaseFragment
import com.ruanchao.androidblog.ui.home.BlogDetailActivity
import com.ruanchao.androidblog.ui.login.LoginActivity
import com.ruanchao.androidblog.utils.obtainViewModel
import com.ruanchao.androidblog.utils.toast
import com.ruanchao.androidblog.view.AddCollectionDialog
import com.ruanchao.androidblog.view.MultiStateView
import kotlinx.android.synthetic.main.collection_fragment.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CollectionFragment : BaseFragment() {

    lateinit var viewBinding: CollectionFragmentBinding
    lateinit var vm: CollectionViewModel
    var mCurrentPage: Int = 0
    var addCollectionDialog: AddCollectionDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        vm = (activity as AppCompatActivity).obtainViewModel(CollectionViewModel::class.java)
        viewBinding = CollectionFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = vm
            lifecycleOwner = this@CollectionFragment
        }
        return viewBinding.root
    }

    override fun initData() {
        StatusBarUtil.immersive(activity as Activity, activity!!.getColor(R.color.public_number_tab_bg))
        StatusBarUtil.setPaddingSmart(activity as Activity, toolbar)
        val listAdapter = PublicNumberListAdapter(activity as Context, vm) {
            BlogDetailActivity.start(activity as Context, it.link)
        }
        listAdapter.isCollectionAdapter = true
        srf_artical_refresh?.setOnRefreshListener {
            mCurrentPage = 0
            loadData()
        }
        public_number_list?.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
            addItemDecoration(
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            )
        }
        public_number_list?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //当前RecyclerView显示出来的最后一个的item的position
                var lastPosition: Int = -1
                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return
                }
                val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager
                lastPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是倒数第三个item的position，开始预加载
                //如果相等则说明已经滑动到最后了
                if (lastPosition >= recyclerView.layoutManager!!.itemCount - 1) {
                    //防止多次重复加载
                    if (!vm.isGetCollectionListLoading) {
                        ++mCurrentPage
                        loadData()
                    }
                }
            }
        })

        btn_collect_login.setOnClickListener {
            LoginActivity.start(activity as Context)
        }

        iv_add_collection.setOnClickListener {
            if (UserInfo.getInstance()?.user == null){
                toast("请先登录账号")
            }else {
                addCollectionDialog = AddCollectionDialog()
                addCollectionDialog?.show(fragmentManager!!, "addCollection")
            }
        }

        vm.dataInfos.observe(this, Observer {
            it?.let {
                if (mCurrentPage == 0) {
                    if (it.size == 0) {
                        stateView.viewState = MultiStateView.VIEW_STATE_EMPTY
                    } else {
                        listAdapter.resetDatas(it)
                        stateView.viewState = MultiStateView.VIEW_STATE_CONTENT
                        isShowCollectContent(true)
                    }
                } else {
                    if (it.size == 0) {
                        toast("没有更多数据")
                        mCurrentPage--
                    } else {
                        listAdapter.addDatas(it)
                    }
                }
            }
        })

        vm?.collectListErrInfo?.observe(this, Observer {
            it?.let {
                if (it == -1001){
                    //需要先登录账号
                    isShowCollectContent(false)
                }else{
                    stateView.viewState = MultiStateView.VIEW_STATE_ERROR
                    Toast.makeText(activity, "获取收藏列表失败", Toast.LENGTH_LONG).show()
                }

            }
        })

        vm?.addCollectionInfo?.observe(this, Observer {
            it?.let {
                listAdapter.addCollectedData(it)
                addCollectionDialog?.dismiss()
            }
        })

        vm?.unCollectInfo.observe(this, Observer {
            it?.let {
                listAdapter.removeUnCollectedData(it)
            }
        })

        loadData()
    }

    private fun loadData() {
        if (UserInfo.getInstance()?.user == null){
            isShowCollectContent(false)
        }else{
            viewBinding.viewModel?.getCollectList(mCurrentPage)
        }
    }

    override fun reload() {
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(userMsg: LoginUserMsg){
        if (userMsg.user == null){
           //退出账号后
            mCurrentPage = 0;
            isShowCollectContent(false)
        }else {
            //重新登录后
            loadData()
        }
    }

    fun isShowCollectContent(show: Boolean){
        stateView.viewState = MultiStateView.VIEW_STATE_CONTENT
        if (show){
            rl_collect_login_tip_layout.visibility = View.GONE
            srf_artical_refresh.visibility = View.VISIBLE
        }else{
            rl_collect_login_tip_layout.visibility = View.VISIBLE
            srf_artical_refresh.visibility = View.GONE
        }
    }

}