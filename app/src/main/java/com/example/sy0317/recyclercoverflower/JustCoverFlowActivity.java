package com.example.sy0317.recyclercoverflower;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.example.sy0317.recoverflow.CoverFlowLayoutManger;
import com.example.sy0317.recoverflow.RecyclerCoverFlow;

public class JustCoverFlowActivity extends AppCompatActivity {
    private RecyclerCoverFlow mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_cover_flow);
        initList();
    }
    private void initList() {
        mList = findViewById(R.id.list);
        mList.setAdapter(new Adapter(this));
        mList.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                ((TextView)findViewById(R.id.index)).setText((position+1)+"/"+mList.getLayoutManager().getItemCount());
            }
        });

    }

}
