package com.example.saber.slidingitem;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SlidingListView listView;
    private List<String> list = new ArrayList<String>();
    private SlidingAdapter slidingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();
        initView();
    }

    private void initView() {
        listView=(SlidingListView) findViewById(R.id.list);
        slidingAdapter = new SlidingAdapter(list,this, new SlidingAdapter.OnClickListenerEditOrDelete() {
            @Override
            public void OnClickListenerEdit(int position) {
                Snackbar.make(listView,"编辑",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void OnClickListenerDelete(int position) {
                list.remove(position);
                listView.turnToNormal();
                slidingAdapter.notifyDataSetChanged();
                Snackbar.make(listView,"删除",Snackbar.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(slidingAdapter);

    }

    public void getData() {
        for (int i=0;i<20;i++){
            list.add(new String("第"+i+"个item"));
        }
    }
}
