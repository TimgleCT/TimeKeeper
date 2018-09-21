package com.example.user.myapplication.setting_setup;

import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.myapplication.setting_setup.CardAdapter;
import com.example.user.myapplication.setting_setup.CardItem;
import com.example.user.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;
    private ListView locationlist;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.setting_setup_adapter, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(CardItem item, View view) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        FrameLayout frameLayout = view.findViewById(R.id.info);
        Button button1 = view.findViewById(R.id.button1);
        Button button2 = view.findViewById(R.id.button2);
        View view1 = view.findViewById(R.id.view);
        titleTextView.setText(item.getTitle());
        String type = item.getType();
        if (type == "location"){
            frameLayout.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);
            button2.setText("ADD");
            locationlist = view.findViewById(R.id.location_list);
            String[] str = {"HOME","WORK","SCHOOL"};
            ArrayAdapter adapter = new ArrayAdapter(view.getContext(), R.layout.setting_setup_location_item, R.id.textView1, str);
            locationlist.setAdapter(adapter);
        }else if(type == "info"){
            locationlist = view.findViewById(R.id.location_list);
            locationlist.setVisibility(View.GONE);
        }


    }

}
