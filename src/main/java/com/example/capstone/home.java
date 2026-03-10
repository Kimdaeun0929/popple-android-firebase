package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // 이전 액티비티로 돌아가는 기능 추가
        View imageView = findViewById(R.id.imageViewBottom1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 액티비티 종료
                finish();
            }
        });

        // 하단 이미지뷰 클릭 이벤트 처리
        findViewById(R.id.imageViewBottom2).setOnClickListener(v -> startActivity(new Intent(home.this, googlemap.class)));
        findViewById(R.id.imageViewBottom3).setOnClickListener(v -> startActivity(new Intent(home.this, home.class)));
        findViewById(R.id.imageViewBottom4).setOnClickListener(v -> startActivity(new Intent(home.this, chat.class)));
        findViewById(R.id.imageViewBottom5).setOnClickListener(v -> startActivity(new Intent(home.this, mypage.class)));

        // 이미지뷰 클릭 이벤트 처리 메서드 호출
        setImageViewClickListener(R.id.store1, "popup_info", "store1.png");
        setImageViewClickListener(R.id.store2, "popup_info1", "store2.png");
        setImageViewClickListener(R.id.store3, "popup_info2", "store3.png");

        // 리니어 레이아웃 클릭 이벤트 처리 메서드 호출
        setLinearLayoutClickListener(R.id.store4, "popup_info3", "store4.png");
        setLinearLayoutClickListener(R.id.store5, "popup_info4", "store5.png");
        setLinearLayoutClickListener(R.id.store6, "popup_info5", "store6.png");
        setLinearLayoutClickListener(R.id.store7, "popup_info6", "store7.png");
        setLinearLayoutClickListener(R.id.store8, "popup_info7", "store8.png");
        setLinearLayoutClickListener(R.id.store9, "popup_info8", "store9.png");
        setLinearLayoutClickListener(R.id.store10, "popup_info9", "store10.png");
    }

    // 이미지뷰 클릭 이벤트 처리 메서드
    private void setImageViewClickListener(int imageViewId, final String tableName, final String imageFileName) {
        findViewById(imageViewId).setOnClickListener(view -> {
            Intent intent = new Intent(home.this, info.class);
            intent.putExtra("tableName", tableName);
            intent.putExtra("imageFileName", imageFileName);
            startActivity(intent);
        });
    }

    // 리니어 레이아웃 클릭 이벤트 처리 메서드
    private void setLinearLayoutClickListener(int linearLayoutId, final String tableName, final String imageFileName) {
        findViewById(linearLayoutId).setOnClickListener(view -> {
            Intent intent = new Intent(home.this, info.class);
            intent.putExtra("tableName", tableName);
            intent.putExtra("imageFileName", imageFileName);
            startActivity(intent);
        });
    }
}
