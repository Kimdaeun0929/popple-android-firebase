//스플래시 전환 코드
package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // 화면 전환까지의 지연 시간을 정의합니다 (밀리초 단위)
    private static final int DELAY_TIME = 2000; // 2초 후에 자동으로 전환

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handler를 사용하여 지정된 시간이 지난 후에 작업을 수행합니다.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // mypage 화면으로 이동하는 Intent 생성
                Intent intent = new Intent(MainActivity.this, login1.class);
                startActivity(intent); // LoginActivity 시작


            }
        }, DELAY_TIME); // DELAY_TIME 후에 Runnable이 실행됩니다.
    }
}
