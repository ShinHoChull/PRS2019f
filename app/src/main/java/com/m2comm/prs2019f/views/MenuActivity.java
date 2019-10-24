package com.m2comm.prs2019f.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.m2comm.prs2019f.R;
import com.m2comm.prs2019f.modules.common.AnimatedExpandableListView;
import com.m2comm.prs2019f.modules.common.Custom_SharedPreferences;
import com.m2comm.prs2019f.modules.common.Globar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuActivity extends Activity implements View.OnClickListener {

    private AnimatedExpandableListView listView;
    private ExampleAdapter adapter;
    private ArrayList<GroupItem> header;

    private Globar g;

    private LinearLayout boothBt , menuBt , menuLogin ;
    private ImageView closeBt, menu_homeImg , loginIcon;
    private TextView loginText , logout;


    private Custom_SharedPreferences csp;

    private void listenerRegister() {

        this.closeBt.setOnClickListener(this);
        this.boothBt.setOnClickListener(this);
        this.menu_homeImg.setOnClickListener(this);
        this.menuBt.setOnClickListener(this);
        this.menuLogin.setOnClickListener(this);
        this.logout.setOnClickListener(this);
    }

    private void init() {
        this.g = new Globar(this);
        this.csp = new Custom_SharedPreferences(this);

        this.closeBt = findViewById(R.id.menu_closeBt);
        this.boothBt = findViewById(R.id.boothBt);
        this.menu_homeImg = findViewById(R.id.menu_homeImg);
        this.menuBt = findViewById(R.id.menu_setting);
        this.menuLogin = findViewById(R.id.menu_login);
        this.loginIcon = findViewById(R.id.menu_loginIcon);
        this.loginText = findViewById(R.id.menu_loginText);
        this.logout = findViewById(R.id.menu_logout);


        this.listenerRegister();

        this.header = new ArrayList<>();

        header.add(new GroupItem(R.drawable.side_m_1, "PRS KOREA 2019",
                new ArrayList<ChildItem>(Arrays.asList(new ChildItem("• Welcome Message"),
                        new ChildItem("•  Overview")))));

        header.add(new GroupItem(R.drawable.side_m_2, "Program at a glance",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.side_m_3, "Day Program",
                new ArrayList<ChildItem>(Arrays.asList(
                        new ChildItem("• Nov. 8 (Fri)"),
                        new ChildItem("• Nov. 9 (Sat)"),
                        new ChildItem("• Nov. 10 (Sun)")))));

        header.add(new GroupItem(R.drawable.side_m_4, "Invited Speakers",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.side_m_5, "Abstract",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.side_m_6, "Photo Gallery",
                new ArrayList<ChildItem>());

        header.add(new GroupItem(R.drawable.side_m_7, "Location / Venue",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.side_m_8, "Question / Feedback",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.side_m_9, "Sponsors",
                new ArrayList<ChildItem>()));

        header.add(new GroupItem(R.drawable.side_m_10, "Notice",
                new ArrayList<ChildItem>());

        if (this.csp.getValue("sid","").equals("")) {
            this.loginIcon.setImageResource(R.drawable.btn_d_login1);
            this.loginText.setText("로그인");
        } else {
            this.loginIcon.setImageResource(R.drawable.btn_d_fav_off1);
            this.loginText.setText("즐겨찾기");
            this.logout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.init();

        this.adapter = new ExampleAdapter(this);
        this.adapter.setData(this.header);

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            int lastClickedPosition = -1;
            GroupHolder oldHolder = null;

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {

                //GroupHolder holder = (GroupHolder) v.getTag();

                if (header.get(groupPosition).items.size() <= 0) {
                    moveView(groupPosition, 0);
                    return true;
                }
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                    //changefoldImg(R.string.plus, holder);
                } else {
                    //changefoldImg(R.string.minus, holder);
                    listView.collapseGroupWithAnimation(lastClickedPosition);
                    if (oldHolder != null && groupPosition != lastClickedPosition) {
                        //changefoldImg(R.string.plus, oldHolder);
                    }
                    listView.expandGroupWithAnimation(groupPosition);
                }

                lastClickedPosition = groupPosition;
                //oldHolder = (GroupHolder) v.getTag();
                return true;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                moveView(groupPosition, childPosition);
                return false;
            }
        });
    }

    private void changefoldImg(int text, GroupHolder holder) {
        Typeface fontAwsome = Typeface.createFromAsset(getAssets(), "fa_solid_900.ttf");
        holder.foldImg.setTypeface(fontAwsome);
        holder.foldImg.setText(text);
        holder.foldImg.setTextColor(getResources().getColor(R.color.main_color_gray));
    }

    private static class GroupItem {
        int img;
        String title;
        List<ChildItem> items;

        public GroupItem(int img, String title, List<ChildItem> items) {
            this.img = img;
            this.title = title;
            this.items = items;
        }
    }

    private static class ChildItem {
        String title;

        public ChildItem(String title) {
            this.title = title;
        }
    }

    private static class ChildHolder {
        TextView title;
        TextView hint;
    }

    private static class GroupHolder {
        TextView title;
        ImageView headerIconImg;
        TextView foldImg;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;
        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.menu_list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.menu_child_textTitle);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);

            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.menu_group_item, parent, false);
                holder.headerIconImg = convertView.findViewById(R.id.menuImg);
                holder.foldImg = convertView.findViewById(R.id.foldImg);
                holder.title = convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.headerIconImg.setImageResource(item.img);
            holder.title.setText(item.title);

            if (getRealChildrenCount(groupPosition) <= 0) {
                holder.foldImg.setVisibility(View.INVISIBLE);
            } else {
                holder.foldImg.setVisibility(View.VISIBLE);
            }

            if (listView.isGroupExpanded(groupPosition)) {
                changefoldImg(R.string.minus, holder);
            } else {
                changefoldImg(R.string.plus, holder);
            }

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.menu_homeImg:
                Intent main = new Intent(this, MainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(main);
                finish();
                break;

            case R.id.menu_closeBt:
                finish();
                overridePendingTransition(0, R.anim.anim_slide_out_left);
                break;

            case R.id.menu_setting:
                Intent setting = new Intent(this, SettingActivity.class);
                startActivity(setting);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;

            case R.id.boothBt:
                Intent booth = new Intent(this, ContentsActivity.class);
                booth.putExtra("content", true);
                booth.putExtra("paramUrl", this.g.urls.get("boothEvent"));
                startActivity(booth);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                finish();
                break;

            case R.id.menu_login:
                if (isLogin().equals("") == false) {
                    Intent content = new Intent(this, ContentsActivity.class);
                    content.putExtra("paramUrl", this.g.urls.get("fav"));
                    startActivity(content);
                } else {
                    this.g.loginMove(this);
                }
                finish();
                break;

            case R.id.menu_logout:
                csp.put("sid","");
                csp.put("isLogin",false);
                finish();
                break;

        }
    }

    public String isLogin() {
        return this.csp.getValue("sid", "");
    }


    private void moveView(int groupPostion, int childPosition) {

        if (groupPostion == 1 && childPosition == 0) {
            Intent glance = new Intent(this, GlanceActivity.class);
            startActivity(glance);
            overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            finish();
            return;
        } else if ( groupPostion == 2 || groupPostion == 4 ) {
            if (!this.csp.getValue("isLogin",false)) {
                this.g.loginMove(this);
                return;
            }
        } else if (groupPostion == 8) {
            if (!this.csp.getValue("isLogin",false)) {
                this.g.loginMove(this);
                return;
            }
            Intent photo = new Intent(this, PhotoActivity.class);
            photo.putExtra("choice","99");
            this.startActivity(photo);
            return;
        }

        Intent content = new Intent(this, ContentsActivity.class);
        if (groupPostion == 0 || groupPostion == 3 || groupPostion == 5 || groupPostion == 9 ) {
            content.putExtra("content", true);
        }
        content.putExtra("paramUrl", this.g.linkUrl[groupPostion][childPosition]);
        content.putExtra("title", this.header.get(groupPostion).title);
        startActivity(content);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        overridePendingTransition(0, R.anim.anim_slide_out_left);
    }
}
