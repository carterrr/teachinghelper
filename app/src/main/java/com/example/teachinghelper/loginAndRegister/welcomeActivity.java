package com.example.teachinghelper.loginAndRegister;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.teachinghelper.MainActivity;
import com.example.teachinghelper.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.teachinghelper.dbbomb.StudentsInfo;
import com.example.teachinghelper.dbbomb.TeacherInfo;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class welcomeActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_choose_teacher;
    private Button btn_choose_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //去除标题
        setContentView(R.layout.activity_welcome);
        Bmob.initialize(this, "b5befe90d65b72ea46dcc4950f7cacb0");
        initView();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("id", null) != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        btn_choose_teacher=(Button)findViewById(R.id.btn_choose_teacher);
        btn_choose_student=(Button)findViewById(R.id.btn_choose_student);
        btn_choose_teacher.setOnClickListener(this);
        btn_choose_student.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_choose_teacher:
                Intent intent = new Intent(this,loginActivity.class);
                intent.putExtra("choosen","teacher");
                startActivity(intent);
               // finish();不能加 否则无法回退
                break;
            case R.id.btn_choose_student:
                Intent intent1 = new Intent(this,loginActivity.class);
                intent1.putExtra("choosen","student");
                startActivity(intent1);
               // finish();不能加 否则无法回退
                break;
        }
    }
}