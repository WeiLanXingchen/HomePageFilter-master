package com.lvfq.homepage_master;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.kevin.wraprecyclerview.WrapRecyclerView;
import com.lvfq.homepage_master.impl.IAreaCallBack;
import com.lvfq.homepage_master.impl.IFilterCallBack;
import com.lvfq.homepage_master.util.V;
import com.lvfq.homepage_master.view.FilterView;

import java.util.ArrayList;
import java.util.List;
/**
 * RecyclerViewScrollActivity
 * @author
 * @date
 * @mainFunction :
 */

public class RecyclerViewScrollActivity extends FragmentActivity {
    private FilterView filterView;
    private RecyclerView recyclerView;
    private RecyclerViewBaseAdapter mAdapter;
    private String strArea = "科室"; // 选择科室
    private String strPosition = "医院"; // 选择医院
    private List<String> testList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_scroll);
        initView();
        initData();
        initAdapter();
        itemClick();
    }

    private void itemClick() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                Log.i("@@@",position+"");
            }
        });
    }

    private void initView() {
        filterView = V.find(this, R.id.main_filter);
        recyclerView = (WrapRecyclerView) findViewById(R.id.recyclerview);

    }
    private void initData() {
        addModel(0, 25);
       /* filterView.setAreaCallBack(new IAreaCallBack() {
            @Override
            public void callBack(String city, String area) {
                strArea = city + " - " + area;
                mAdapter.notifyDataSetChanged();
            }
        });*/

        filterView.setFilterCallBack(new IFilterCallBack() {
            @Override
            public void callBack(int index, String string) {
                switch (index) {
                    case 1:
                        strPosition = string;
                        break;
                    case 0:
                        strArea = string;
                        break;
                }
                mAdapter.notifyDataSetChanged();
            }
        });
//        filterView.setAreaCallBack(new IFilterCallBack() {
//            @Override
//            public void callBack(int index, String string) {
//                switch (index) {
//                    case 0:
//                        strArea = string;
//                        break;
////                    case 2:
////                        strExp = string;
////                        break;
//                }
//                mAdapter.notifyDataSetChanged();
//            }
//        });

    }

    private void initAdapter() {
        mAdapter = new RecyclerViewBaseAdapter(R.layout.item_main_listview,testList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START /*| ItemTouchHelper.END*/);//设置双向滑动功能
        // 开启滑动删除
        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
        mAdapter.openLoadAnimation();
    }

    /**
     * 创建模拟数据
     *
     * @param startIndex
     * @param length
     */
    private void addModel(int startIndex, int length) {
        for (int i = startIndex; i < startIndex + length; i++) {
            testList.add("");
        }
    }
    public class RecyclerViewBaseAdapter extends BaseItemDraggableAdapter<String,BaseViewHolder> {
        public RecyclerViewBaseAdapter(int layoutResId, List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
           helper.setText(R.id.tv_item_index,helper.getPosition()+"");
           helper.setText(R.id.tv_item_position,strPosition+(helper.getPosition()+1)+"");
           helper.setText(R.id.tv_item_area,strArea+(helper.getPosition()+1)+"");
        }
    }
    OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {}
        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {}
        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {}

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
//            Paint mPaint = new Paint();
            // 初始化画笔、Rect
            Paint mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);   // 设置样式
            mPaint.setColor(getResources().getColor(R.color.white));
            mPaint.setTextSize(60);
            // 将坐标原点移到控件中心
            canvas.translate(-dX/4, viewHolder.itemView.getHeight() / 2);
            // y轴
            canvas.drawLine(0, -viewHolder.itemView.getHeight() / 2, 0,
                    viewHolder.itemView.getHeight() / 2, mPaint);
            // 文字baseline在y轴方向的位置
            float baseLineY = Math.abs(mPaint.ascent() + mPaint.descent()) / 2;
            canvas.drawColor(getResources().getColor(R.color.red));
//            mPaint.setTextAlign(Paint.Align.CENTER);
            // 绘制字符串
            canvas.drawText("删 除", -dX/4, baseLineY, mPaint);
        }
    };
}
