package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.user.myapplication.setting_setup.setting_setup;

import java.util.ArrayList;
import java.util.List;

public class setting_friend extends AppCompatActivity {
    Button add_friend_btn, friend_delete;
    View timekeeper_logo;

    private GridView gridView;
    private boolean isShowDelete = false;
    private GridViewAdapter mGridAdapter;
    private List<AreaEntity> areaEneities = new ArrayList<AreaEntity>();
    AlphaAnimation alphaAnimation1 = new AlphaAnimation(1.0f, 0.1f);
    AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.1f, 1.0f);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_friend);

        //增加好友
        gridView = findViewById(R.id.friend_list);
        for (int i = 0; i < 10; i++) {
            AreaEntity areaEntity = new AreaEntity(i + "", R.drawable.ai_open, "" + i);
            areaEneities.add(areaEntity);
        }

        //好友長按事件
        mGridAdapter = new GridViewAdapter(areaEneities, setting_friend.this);
        gridView.setAdapter(mGridAdapter);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (isShowDelete) {
                    isShowDelete = false;
                } else {
                    isShowDelete = true;
                }
                mGridAdapter.setIsShowDelete(isShowDelete);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });

        //點選 ADD FRIEND 按鈕
        add_friend_btn = findViewById(R.id.add_friend_btn);
        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(setting_friend.this, setting_friend_search.class);
                startActivity(intent1);
            }
        });

        //回首頁
        timekeeper_logo = findViewById(R.id.timekeeper_logo);
        timekeeper_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(setting_friend.this, mainpage.class);
                startActivity(intent2);
            }
        });
    }

    //跳出選單
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.mainpage_menu, popup.getMenu());
        popup.show();

        //點擊選單選項，然後換頁
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_setup:
                        Intent intent3 = new Intent(setting_friend.this, setting_setup.class);
                        startActivity(intent3);
                        return true;

                    case R.id.action_friends:
                        Intent intent4 = new Intent(setting_friend.this, setting_friend.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        });
    }

    // 按返回鍵取消delete狀態
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowDelete == true) {
                isShowDelete = false;
                mGridAdapter.setIsShowDelete(false);
            } else {
                finish();
            }
        }
        return false;
    }


    // 按空白處取消delete狀態
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View v = gridView;

            if (isShouldHideInput(v, ev)) {
                if (isShowDelete == true) {
                    isShowDelete = false;
                    mGridAdapter.setIsShowDelete(false);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }


    class GridViewAdapter extends BaseAdapter {
        private boolean isShowDelete; //根据这个布尔型变量来判断是否显示删除图标，true是显示，false是不显示
        private List<AreaEntity> areaEneity;
        private LayoutInflater mInflater;
        private Context mContext;

        public GridViewAdapter(List<AreaEntity> areaEneities, Context context) {
            super();
            this.areaEneity = areaEneities;
            this.mInflater = LayoutInflater.from(context);
            this.mContext = context;
        }

        public void setIsShowDelete(boolean isShowDelete) {
            this.isShowDelete = isShowDelete;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return areaEneity.size();
        }

        @Override
        public Object getItem(int position) {
            return areaEneity.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.setting_friend_item, null);
                holder = new ViewHolder();
                holder.img = convertView.findViewById(R.id.friend_photo);
                holder.name_tv = convertView.findViewById(R.id.friend_name);
                holder.delete_btn = convertView.findViewById(R.id.friend_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name_tv.setText(areaEneity.get(position).getArea());
            holder.delete_btn.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
            holder.img.setAlpha(isShowDelete ? 0.5f : 1.0f);
            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    areaEneity.remove(areaEneity.get(position));
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder {
            private Button delete_btn;
            private ImageView img;
            private TextView name_tv;
        }
    }

    class AreaEntity {
        private String id;
        private int pic;
        private String area;

        public AreaEntity(String id, int pic, String area) {
            this.id = id;
            this.pic = pic;
            this.area = area;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getPic() {
            return pic;
        }

        public void setPic(Integer pic) {
            this.pic = pic;
        }

        public String getArea() {
            return area;
        }

    }
}
