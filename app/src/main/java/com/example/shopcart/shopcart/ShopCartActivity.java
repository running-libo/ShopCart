package com.example.shopcart.shopcart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 购物车
 */
public class ShopCartActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.tv_nodatas)
    TextView tvNodatas;
    @Bind(R.id.tv_shopcart_totalmoney)
    TextView tvShopcartTotalmoney;
    @Bind(R.id.cb_shopcart_all)
    CheckBox cbShopcartAll;
    @Bind(R.id.tv_billing)
    TextView tvBilling;
    @Bind(R.id.rv)
    RecyclerView rv;
    private int[] pics = {R.mipmap.test1, R.mipmap.test2, R.mipmap.test3, R.mipmap.test4};
    private ArrayList<ShopcartEntity> datas = new ArrayList();
    private CommonAdapter<ShopcartEntity> adapter;
    /**
     * 用来记录checkBox列表当前选中状态，购物车id是键，是否选中状态是值
     */
    private SparseArray<Boolean> mSelectState = new SparseArray();
    /**
     * 购物车商品总价格
     */
    private float totalMoney = 0;
    /**
     * 创建数量改变观察者对象
     */
    private RecyclerView.AdapterDataObserver totalPriceObserver = new RecyclerView.AdapterDataObserver() {

        /**
         * 当Adapter的notifyDataSetChanged方法执行时被调用
         */
        public void onChanged() {
            calculateTotalPrice();
        }

        /**
         * 当Adapter 调用 notifyDataSetInvalidate方法执行时被调用
         */
        public void onInvalidated() {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new CommonAdapter<ShopcartEntity>(getApplicationContext(), R.layout.item_shopcart, datas) {
            @Override
            protected void convert(ViewHolder baseViewHolder, final ShopcartEntity entity, final int position) {
                final CheckBox cbChoose = baseViewHolder.getView(R.id.cb_shopcart);
                ImageView ivCover = baseViewHolder.getView(R.id.iv_shopcart_cover);
                TextView tvName = baseViewHolder.getView(R.id.tv_shopcart_name);
                TextView tvPrice = baseViewHolder.getView(R.id.tv_shopcart_price);
                ImageButton ibDel = baseViewHolder.getView(R.id.ib_shopcart_del);
                final TextView tvReduce = baseViewHolder.getView(R.id.tv_detail_reduce);
                TextView tvPlus = baseViewHolder.getView(R.id.tv_detail_plus);
                final TextView tvNum = baseViewHolder.getView(R.id.tv_detail_productnum);

                ivCover.setImageResource(entity.getPicRes());
                tvName.setText(entity.getProduct_name());
                tvPrice.setText(entity.getProduct_price());
                tvNum.setText("" + entity.getQuantity());

                final int id = entity.getId();
                cbChoose.setChecked(mSelectState.get(id, false));
                cbChoose.setOnClickListener(new View.OnClickListener() {     //用onclick方法而不是onChecked方法，因为是自动调用onCheckedChange方法
                    @Override
                    public void onClick(View v) {
                        //通过保存的是否选中来判断操作
                        boolean isSelected = !mSelectState.get(id,false);
                        cbChoose.setChecked(isSelected);
                        if(isSelected){
                            mSelectState.put(id, true);
                        }else{
                            mSelectState.delete(id);
                        }
                        cbShopcartAll.setChecked(mSelectState.size() == datas.size());   //判断是否达到全选
                        notifyDataSetChanged();
                    }
                });

                tvReduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quatity = (datas.get(position)).getQuantity();
                        if(quatity == 1) return;
                        (datas.get(position)).setQuantity(quatity - 1);
                        notifyDataSetChanged();
                    }
                });
                tvPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int quatity = (datas.get(position)).getQuantity();
                        if(quatity >= entity.getProduct_quantity()){
                            Toast.makeText(getApplicationContext(),"超出库存量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        (datas.get(position)).setQuantity(quatity + 1);
                        notifyDataSetChanged();
                    }
                });
            }
        };
        rv.setAdapter(adapter);
        adapter.registerAdapterDataObserver(totalPriceObserver);

        cbShopcartAll.setOnClickListener(this);
        tvBilling.setOnClickListener(this);

        initData();
    }

    /**
     * 模拟服务器数据
     */
    private void initData() {
        ArrayList list = new ArrayList();
        ShopcartEntity entity;
        for (int i = 0; i < pics.length; i++) {
            entity = new ShopcartEntity();
            entity.setId(i);
            entity.setProduct_id(i);
            entity.setProduct_name("商品" + i);
            entity.setProduct_price("199");
            entity.setProduct_status("selling");
            entity.setPicRes(pics[i]);
            entity.setQuantity(1);
            entity.setProduct_quantity(5);
            list.add(entity);
        }
        datas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_shopcart_all:
                checkAll();
                break;
            case R.id.tv_billing:
                //判断是否有一项商品的选择
                if (mSelectState.size() == 0) {
                    Toast.makeText(getApplicationContext(), "未选择商品", Toast.LENGTH_SHORT).show();
                } else {
                    //去结算
                }
                break;
        }
    }

    private void calculateTotalPrice() {
        totalMoney = 0;
        for (int i = 0; i < mSelectState.size(); i++) {
            for (ShopcartEntity entity : datas) {
                if (mSelectState.keyAt(i) == entity.getId()) {    //表明选中了当前这项
                    totalMoney += entity.getQuantity() * Float.parseFloat(entity.getProduct_price());
                }
            }
        }
        tvShopcartTotalmoney.setText("" + totalMoney);
    }

    private void checkAll() {
        mSelectState.clear();
        if (cbShopcartAll.isChecked()) {   //全选
            for (int i = 0; i < datas.size(); i++) {
                int id = datas.get(i).getId();
                mSelectState.put(id, true);
            }
            adapter.notifyDataSetChanged();
        } else {   //全不选
            adapter.notifyDataSetChanged();
        }
    }

}
