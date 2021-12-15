package com.example.parkinglotapp.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parkinglotapp.MainActivity;
import com.example.parkinglotapp.R;
import com.example.parkinglotapp.blur.BlurBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtEmail,edtPass,edtPass1;
    private Button btn_next;
    private FirebaseAuth mAuth;
    private ConstraintLayout mContainerView;
    public static final String EMAIL="EMAIL";
    public static final String PASS="PASS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContainerView=findViewById(R.id.container);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_login);
        Bitmap blurredBitmap = BlurBuilder.blur( this, originalBitmap );
        mContainerView.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        mAuth=FirebaseAuth.getInstance();
        edtEmail=findViewById(R.id.edt_email);
        edtPass=findViewById(R.id.edt_password);
        edtPass1=findViewById(R.id.edt_password_confirm);
        btn_next=findViewById(R.id.btn_next);
//        btn_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email=edtEmail.getText().toString().trim();
//                String pass=edtPass.getText().toString().trim();
//                String pass1=edtPass1.getText().toString().trim();
//                boolean isEmpty=false;
//                for(String x: new String[]{email, pass, pass1}){
//                    if(x.isEmpty()){
//                        isEmpty=true;
//                        break;
//                    }
//                }
//                if(isEmpty){
//                    Toast.makeText(RegisterActivity.this, "Dien day du cac truong", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    if(!pass.equals(pass1)){
//                        Toast.makeText(RegisterActivity.this, "password cua ban khong trung", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        mAuth.createUserWithEmailAndPassword(email,pass)
//                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if(task.isSuccessful()){
//                                            Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                                            goToMain();
//                                        }
//                                        else{
//                                            Toast.makeText(RegisterActivity.this, "error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
//                }
//            }
//        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edtEmail.getText().toString().trim();
                String pass=edtPass.getText().toString().trim();
                String pass1=edtPass1.getText().toString().trim();
                boolean isEmpty=false;
                for(String x: new String[]{email, pass, pass1}){
                    if(x.isEmpty()){
                        isEmpty=true;
                        break;
                    }
                }
                if(isEmpty){
                    Toast.makeText(RegisterActivity.this, "Dien day du cac truong", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!pass.equals(pass1)){
                        Toast.makeText(RegisterActivity.this, "password cua ban khong trung", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent intent=new Intent(RegisterActivity.this,InfoSetupActivity.class);
                        intent.putExtra(EMAIL,email);
                        intent.putExtra(PASS,pass);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    private void goToMain(){
        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}