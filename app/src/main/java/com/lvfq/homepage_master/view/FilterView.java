package com.lvfq.homepage_master.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lvfq.homepage_master.R;
import com.lvfq.homepage_master.adapter.ListViewBaseAdapter;
import com.lvfq.homepage_master.impl.IAreaCallBack;
import com.lvfq.homepage_master.impl.IFilterCallBack;
import com.lvfq.homepage_master.util.V;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lvfq.homepage_master.R.id.tv_tip1;
import static com.lvfq.homepage_master.R.id.tv_tip2;
import static com.lvfq.homepage_master.R.id.tv_tip3;

/**
 * Created by
 * Date
 */

public class FilterView extends LinearLayout implements View.OnClickListener {
    private Context context;

    private TextView[] tips = new TextView[3];

    private View fl_content;
//    private View ll_content;

    private ListView lv_filter_single;
//    private ListView lv_filter_city;
//    private ListView lv_filter_area;

    private ListViewBaseAdapter<String> lvSingleAdapter;
    private ListViewBaseAdapter<String> lvSingleAdapter1;
    private ListViewBaseAdapter<String> lvAdapterCity;
    private ListViewBaseAdapter<String> lvAdapterArea;
//
//    private String[] c = new String[]{"全部科室","头颈外科", "胸科", "妇瘤科", "核医学科","介入科","皮肤科","外科",
//            "儿科","内科","骨科","肿瘤科","疼痛科","中医科"};
//
//    private String[][] a = new String[][]{
//            {"头颈外科门诊1", "头颈外科门诊2", "头颈外科门诊3"},
//            {"肺部肿瘤内科门诊7"},
//            {"妇科肿瘤中心一门诊","妇科肿瘤中心二门诊"},
//            {"核医学治疗科门诊"},
//            {"介入科门诊"},
//            {"皮肤科门诊","伤口造口门诊"},
//            {"泌尿外科门诊", "肝胆胰外科门诊", "胃肠外科中心一门诊", "胃肠外科中心二门诊", "胸外科中心一门诊",
//                    "胸外科中心二门诊", "胸外科中心三门诊","神经颅脑外科门诊","乳腺外科中心二门诊","乳腺外科中心一门诊"},
//            {"儿科门诊"},
//            {"综合内科一门诊","血液淋巴瘤科门诊"},
//            {"骨软组织外科门诊"},
//            {"肿瘤通科二门诊", "腹部肿瘤内科门诊", "头颈放疗科一门诊", "头颈放疗科二门诊", "日渐肿瘤化疗区门诊",
//                    "综合特需肿瘤科门诊", "肿瘤通科一门诊", "胸部放疗科一门诊", "胸部放疗科二门诊","胸部肿瘤内科门诊","覆盆放疗科门诊","腹部放疗科门诊"},
//            {"疼痛门诊"},
//            {"肿瘤中医科一门诊", "肿瘤中医科二门诊", "肿瘤中医科三门诊"}};

    private List<String> expList = new ArrayList<>();   // 经验列表
    private List<String> positionList = new ArrayList<>();  // 岗位列表
    private List<String> positionList1 = new ArrayList<>();  // 岗位列表
//    private List<String> citys = new ArrayList<>(); // 城市列表
//    private List<List<String>> areas = new ArrayList<>();   // 区域列表

    private List<String> singleList1 = new ArrayList<>();    // 存放当前ListView 所填充数据
    private List<String> singleList = new ArrayList<>();    // 存放当前ListView 所填充数据

    private int curCity = 0;
    private int curIndex = -1;

    private IFilterCallBack areaCallBack;
    private IFilterCallBack filterCallBack;


    public FilterView(Context context) {
        this(context, null);
    }

    public FilterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        this.context = context;
        initData();
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_filter, null);
        addView(view);

        initView(view);

        initAdapter();

    }

    /**
     * 实例化组件
     * @param view
     */
    private void initView(View view) {
        tips[0] = V.find(view, tv_tip1);
        tips[1] = V.find(view, tv_tip2);
        tips[2] = V.find(view, tv_tip3);

        fl_content = V.find(view, R.id.fl_content);
//        ll_content = V.find(view, R.id.ll_content);

        lv_filter_single = V.find(view, R.id.lv_filter_single);
//        lv_filter_city = V.find(view, R.id.lv_filter_city);
//        lv_filter_area = V.find(view, R.id.lv_filter_area);

        view.findViewById(R.id.fl_tip1).setOnClickListener(this);
        view.findViewById(R.id.fl_tip2).setOnClickListener(this);
        view.findViewById(R.id.fl_tip3).setOnClickListener(this);
        fl_content.setOnClickListener(this);
    }

    /**
     * 初始化 Adapter
     */
    private void initAdapter() {
        lvSingleAdapter = new ListViewBaseAdapter<String>(context, R.layout.item_filter_single, singleList) {
            @Override
            public void convert(int position, final String item) {
                TextView textView = f(R.id.tv_item_single);
                textView.setText(item);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // TODO: 2017/3/31 列表点击事件
                        if (filterCallBack != null) {
                            filterCallBack.callBack(curIndex, item);
                            tips[curIndex].setText(item);
//                            tips[curIndex].setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                        changeFilterStatus(curIndex);
                    }
                });
                if (textView.getText().toString().equals(tips[curIndex].getText().toString())){
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    textView.setTextColor(getResources().getColor(R.color.color_66));
                }
            }
        };
        lvSingleAdapter1 = new ListViewBaseAdapter<String>(context, R.layout.item_filter_single, singleList1) {
            @Override
            public void convert(int position, final String item) {
                TextView textView = f(R.id.tv_item_single);
                textView.setText(item);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // TODO: 2017/3/31 列表点击事件
                        if (filterCallBack != null) {
                            filterCallBack.callBack(curIndex, item);
                            tips[curIndex].setText(item);
//                            tips[curIndex].setTextColor(getResources().getColor(R.color.colorAccent));
                        }
                        changeFilterStatus(curIndex);
                    }
                });
                if (textView.getText().toString().equals(tips[curIndex].getText().toString())){
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    textView.setTextColor(getResources().getColor(R.color.color_66));
                }
            }
        };

//        lvAdapterCity = new ListViewBaseAdapter<String>(context, R.layout.item_filter_list1, citys) {
//            @Override
//            public void convert(final int position, String item) {
//                TextView textView = f(R.id.tv_item_city);
//                textView.setText(item);
//                if (curCity == position) {
//                    textView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_bright));
//                } else {
//                    textView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
//                }
//                if (position==0){
//                    itemView.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // 确定回调
//                            if (areaCallBack != null) {
//                                areaCallBack.callBack(citys.get(curCity), "全部科室");
//                                tips[curIndex].setText("全部科室");
////                                tips[curIndex].setTextColor(getResources().getColor(R.color.color_66));
//                            }
//                            changeFilterStatus(curIndex);
//                        }
//                    });
//                }else {
//                    itemView.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            singleList.clear();
//                            List<String> list = areas.get(position-1);
//                            if (list != null && list.size() > 0) {
//                                singleList.addAll(list);
//                            }
//                            curCity = position;
//                            notifyDataSetChanged();
//                            lvAdapterArea.notifyDataSetChanged();
//                        }
//                    });
//                }
//
//            }
//        };

//        lvAdapterArea = new ListViewBaseAdapter<String>(context, R.layout.item_filter_list2, singleList) {
//            @Override
//            public void convert(int position, final String item) {
//                TextView textView = f(R.id.tv_item_area);
//                textView.setText(item);
//                itemView.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // 确定回调
//                        if (areaCallBack != null) {
//                            areaCallBack.callBack(citys.get(curCity), item);
//                            tips[curIndex].setText(item);
//                        }
//                        changeFilterStatus(curIndex);
//                    }
//                });
//                if (textView.getText().toString().equals(tips[curIndex].getText().toString())){
//                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
//                }else {
//                    textView.setTextColor(getResources().getColor(R.color.color_66));
//                }
//            }
//        };

        lv_filter_single.setAdapter(lvSingleAdapter);
//        lv_filter_city.setAdapter(lvAdapterCity);
//        lv_filter_area.setAdapter(lvAdapterArea);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_tip1:
                singleList.clear();
                curCity = 0;
                singleList.addAll(positionList1);
                changeFilterStatus(0);
                break;
            case R.id.fl_tip2:
                singleList.clear();
                singleList.addAll(positionList);
                changeFilterStatus(1);
                break;
            case R.id.fl_tip3:
                singleList.clear();
                singleList.addAll(expList);
                changeFilterStatus(2);
                break;
            case R.id.fl_content:
                changeFilterStatus(curIndex);
                break;
        }
    }

    /**
     * 改变筛选条件状态
     * @param index
     */
    private void changeFilterStatus(int index) {
        if (curIndex == index) {
            fl_content.setVisibility(GONE);
            tips[index].setSelected(false);
            curIndex = -1;
        } else {
            if (fl_content.getVisibility() != VISIBLE) {
                fl_content.setVisibility(VISIBLE);
            }
          /*  if (index == 0) {
//                ll_content.setVisibility(VISIBLE);
                lv_filter_single.setVisibility(GONE);

                if (lvAdapterCity != null) {
                    lvAdapterCity.notifyDataSetChanged();
                }
                if (lvAdapterArea != null) {
                    lvAdapterArea.notifyDataSetChanged();
                }
            } else {
//                ll_content.setVisibility(GONE);
                lv_filter_single.setVisibility(VISIBLE);

                if (lvSingleAdapter != null) {
                    lvSingleAdapter.notifyDataSetChanged();
                }
            }*/
            lv_filter_single.setVisibility(VISIBLE);

            if (lvSingleAdapter != null) {
                lvSingleAdapter.notifyDataSetChanged();
            }
            tips[index].setSelected(true);
            if (curIndex != -1) {
                tips[curIndex].setSelected(false);
            }
            curIndex = index;
        }
    }


    /**
     * 初始化模拟数据
     */
    private void initData() {
        initArea();
        initPosition();
    }


    /**
     * 模拟区域数据
     */
    private void initArea() {
//        citys = Arrays.asList(c);
//        for (int i = 0; i < a.length; i++) {
//            areas.add(Arrays.asList(a[i]));
//        }
        positionList1.add("全部医院");
        positionList1.add("四川省人民医院");
        positionList1.add("四川大学华西医院");
        positionList1.add("四川省中医院");

    }

    /**
     * 模拟岗位数据
     */
    private void initPosition() {
        positionList.add("全部医院");
        positionList.add("四川省人民医院");
        positionList.add("四川大学华西医院");
        positionList.add("四川省中医院");
        positionList.add("四川省妇幼保健院");
        positionList.add("成都市第一人民医院");
        positionList.add("成都市妇幼保健院");
        positionList.add("成都铁路中心医院（成都大学附属医院）");
        positionList.add("四川省骨科医院（成都体育学院附属医院）");
        positionList.add("中国人民解放军成都军区总医院");
        positionList.add("中国人民解放军第四五二医院（空军成都医院）");
        positionList.add("中国人民解放军四十七医院（成都医学院第一附属医院）");
        positionList.add("武警四川总队医院");
    }

    /**
     * 设置区域点击回调
     * @param areaCallBack
     */
    public void setAreaCallBack(IFilterCallBack areaCallBack) {
        this.areaCallBack = areaCallBack;
    }

    /**
     * 单选项点击回调
     * @param filterCallBack
     */
    public void setFilterCallBack(IFilterCallBack filterCallBack) {
        this.filterCallBack = filterCallBack;
    }

}
