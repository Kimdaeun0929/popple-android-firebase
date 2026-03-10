package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.auth.FirebaseUser;

import android.widget.Toast;

public class login2 extends AppCompatActivity {

    private FirebaseAuth mAuth; //Firebase 인증 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login2); //login2.xml과 연결
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        mAuth = FirebaseAuth.getInstance(); //FirebaseAuth 객체 초기화

        // 비밀번호 입력란에 inputType을 PASSWORD로 설정하여 숨겨진 문자로 표시되도록 함
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        // 비밀번호 2차 확인 입력란도 숨겨진 문자로 설정
        EditText passwordConfirmEditText = findViewById(R.id.editTextPassword1);
        passwordConfirmEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼이 클릭되었을 때 실행되는 코드
                signUp();
            }
        });


    }

    private void signUp(){
        EditText emailEditText = findViewById(R.id.editTextEmail);
        EditText passwordEditText = findViewById(R.id.editTextPassword);
        EditText passwordConfirmEditText = findViewById(R.id.editTextPassword1); // 새로 추가된 비밀번호 2차 확인 필드
        EditText nicknameEditText = findViewById(R.id.editTextNickname);
        EditText birthdayEditText = findViewById(R.id.editTextBirthday);

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordConfirm = passwordConfirmEditText.getText().toString().trim(); // 새로운 비밀번호 2차 확인 값
        String nickname = nicknameEditText.getText().toString().trim();
        String birthday = birthdayEditText.getText().toString().trim();

        // 이메일, 비밀번호, 닉네임, 생년월일이 모두 입력되었는지 확인
        if(email.isEmpty() || password.isEmpty() || nickname.isEmpty() || birthday.isEmpty()){
            Toast.makeText(this, "모든 항목 다 입력하세요.", Toast.LENGTH_SHORT).show();
        }
        // 새로 추가된 비밀번호 2차 확인 값이 비밀번호와 일치하는지 확인
        else if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "비밀번호를 다르게 입력하였습니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                // 회원가입 성공 시 추가 정보를 Firebase 사용자 프로필에 저장
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nickname)
                                        .build();
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // 프로필 업데이트 성공 시 추가 작업 수행
                                                    // 예: 생일 정보를 Firebase 데이터베이스에 저장
                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                    DatabaseReference userRef = database.getReference("users").child(user.getUid());
                                                    userRef.child("birthday").setValue(birthday);

                                                    Toast.makeText(login2.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                                    goToHomeActivity();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(login2.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void goToHomeActivity(){
        Intent intent = new Intent(this, login1.class);
        startActivity(intent);
        finish();
    }
}