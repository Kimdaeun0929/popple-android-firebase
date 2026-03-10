package com.example.capstone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mypage extends Activity {

    TextView greetingTextView;
    TextView loginTextView;
    boolean isLoggedIn = false;
    private static final int LOGIN_REQUEST_CODE = 1;
    private static final String PREF_NAME = "login_pref";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_USER_NAME = "user_name"; // 사용자 이름을 저장할 키

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        // XML에서 정의한 뷰와 변수를 연결
        greetingTextView = findViewById(R.id.login1);
        loginTextView = findViewById(R.id.login2);

        // 로그인 텍스트뷰 클릭 시 로그인 화면으로 이동
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mypage.this, login1.class);
                startActivityForResult(intent, LOGIN_REQUEST_CODE);
            }
        });

        // 이전 액티비티로 돌아가는 기능 추가
        View imageView = findViewById(R.id.imageViewBottom1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 하단 이미지뷰 클릭 이벤트 처리
        findViewById(R.id.imageViewBottom2).setOnClickListener(v -> startActivity(new Intent(mypage.this, googlemap.class)));
        findViewById(R.id.imageViewBottom3).setOnClickListener(v -> startActivity(new Intent(mypage.this, home.class)));
        findViewById(R.id.imageViewBottom4).setOnClickListener(v -> startActivity(new Intent(mypage.this, chat.class)));
        findViewById(R.id.imageViewBottom5).setOnClickListener(v -> startActivity(new Intent(mypage.this, mypage.class)));

        // 위시리스트 레이아웃 클릭 이벤트 처리
        LinearLayout wishlistLayout = findViewById(R.id.wishlist);
        wishlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mypage.this, wishlist.class);
                startActivity(intent);
            }
        });

        // 앱 시작 시 로그인 상태 확인 및 환영 메시지 업데이트
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false);
        updateGreeting();
    }

    // 로그인 액티비티로부터 결과를 받는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            // 로그인 성공 시 사용자 이름을 받아와 환영 메시지를 업데이트
            updateGreeting();

            // 로그인 상태를 저장
            SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
            editor.putBoolean(KEY_LOGGED_IN, true);
            editor.apply();
        }
    }

    // 환영 메시지 업데이트 메서드
    private void updateGreeting() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                greetingTextView.setText(displayName + "님, 안녕하세요");

                // 사용자 이름을 SharedPreferences에 저장
                SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
                editor.putString(KEY_USER_NAME, displayName);
                editor.apply();

                isLoggedIn = true;
                loginTextView.setText("로그아웃"); // 로그아웃 텍스트로 설정
            }
        } else {
            // 사용자가 로그인하지 않은 경우
            greetingTextView.setText("비회원님, 안녕하세요");
            isLoggedIn = false;
            loginTextView.setText("로그인 하려면 클릭하세요"); // 로그인 텍스트로 설정
        }
    }

    // 앱이 재개될 때 로그인 상태 확인
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false);
        if (!isLoggedIn) {
            updateGreeting();
        }
    }
}
