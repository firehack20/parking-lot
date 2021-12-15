package com.example.parkinglotapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parkinglotapp.animations.ZoomOutPageTransformer;
import com.example.parkinglotapp.firebase.FirebaseIDService;
import com.example.parkinglotapp.fragments.HistoryFragment;
import com.example.parkinglotapp.login.LoginActivity;
import com.example.parkinglotapp.model.Person;
import com.example.parkinglotapp.singleton.SingleTon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final int REQUEST_CODE=142;
    private FragmentManager manager=getSupportFragmentManager();
    private PagerAdapter pagerAdapter=null;
    private FirebaseFirestore firebaseFirestore;
    View decorView;
    final Intent intent=new Intent(MainActivity.this, FirebaseIDService.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        decorView=getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        init();
        pagerAdapter=new com.example.parkinglotapp.adapters.PagerAdapter(manager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true,new ZoomOutPageTransformer());
        tabLayout.setupWithViewPager(viewPager);
        setIconTabLayout();
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        if(mUser==null){
            goToLogin();
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "have", Toast.LENGTH_SHORT).show();
            firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Users").document(mAuth.getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot documentSnapshot=task.getResult();
                                if(documentSnapshot!=null){
                                    Person person=documentSnapshot.toObject(Person.class);
                                    if(person.isHaveNotification()){
                                        Intent intent1=new Intent(MainActivity.this,NotificationActivity.class);
                                        intent1.putExtra("test","somethings");
                                        startActivity(intent1);
//                                        startActivityForResult(intent1,1);
                                    }
                                }
                            }
                            else{

                            }
                        }
                    });

//        Toast.makeText(this, "token : ", Toast.LENGTH_SHORT).show();
            startService(intent);
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            String token=task.getResult().getToken();
                            Log.d("AAA1","token : "+token);
                        }
                    });
        }
    }
    private void goToLogin(){
        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        finish();
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        decorView.setSystemUiVisibility(hideSystemBars());
    }

    private void SignOut(){
        firebaseFirestore.collection("Users").document(mAuth.getUid()).update("token",null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
//                    Toast.makeText(MainActivity.this, "update : null", Toast.LENGTH_SHORT).show();
                    Log.d("AAA1","update : null");
                    SingleTon.getInstance().clear();
                    mAuth.signOut();
                    goToLogin();
                }
                else{
//                    Toast.makeText(MainActivity.this, "error (update null) : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("AAA1","error (update null) : "+task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.act_logout:
                SignOut();
                stopService(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void init(){
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
    }
    public void setIconTabLayout(){
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_history);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_info);
    }

}