package com.example.teachinghelper.loginAndRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teachinghelper.MainActivity;
import com.example.teachinghelper.R;
import com.example.teachinghelper.dbbomb.Clazzes;
import com.example.teachinghelper.dbbomb.StudentsInfo;
import com.example.teachinghelper.dbbomb.TeacherInfo;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edit_id;
    private EditText edit_passwd;
    private Button btn_login;
    private Button btn_linkToRegisterScreen;
    private ProgressDialog progressDialog = null;
    String choosen;
    String id;
    String passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //去除标题
        setContentView(R.layout.activity_login);
        Intent intent=getIntent();
        choosen=intent.getStringExtra("choosen");
        initView();

    }


    private void initView() {
        edit_id=(EditText)findViewById(R.id.idInput);
        edit_passwd=(EditText)findViewById(R.id.passwordInput);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_linkToRegisterScreen=(Button)findViewById(R.id.btn_linkToRegisterScreen);
        btn_login.setOnClickListener(this);
        btn_linkToRegisterScreen.setOnClickListener(this);
        if(choosen.equals("teacher")){
            edit_id.setHint("教师编号");
        }else if(choosen.equals("student")) {
            edit_id.setHint("学生编号");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
         case R.id.btn_login:
             id=edit_id.getText().toString();
             passwd=edit_passwd.getText().toString();

             if(!id.isEmpty()&&!passwd.isEmpty()){
                 progressDialog = ProgressDialog.show(loginActivity.this, "请稍等...", "正在登陆中...", true);

                 //String类型不能用==比较，js中可以但是java中String保存的时字符串对象地址值！！！
                 if(choosen.equals("teacher")){
                         BmobQuery<TeacherInfo> query =new BmobQuery<TeacherInfo>();
                         query.addWhereEqualTo("teacherId",id).findObjects(new FindListener<TeacherInfo>() {
                             @Override
                             public void done(List<TeacherInfo> list, BmobException e) {
                                 progressDialog.dismiss();
                                 if(e==null){
                                     Toast.makeText(loginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                     if(list.get(0).getPassword().equals(passwd)){
                                         loginSuccess(id,passwd,list.get(0).getTeacherName(),choosen,list.get(0).getClazzes());
                                     }else{
                                         Toast.makeText(loginActivity.this,"登录失败，请检查编号和密码",Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             }
                         });
                 }else if(choosen.equals("student")) {
                     StudentsInfo studentsInfo=new StudentsInfo();
                     studentsInfo.setUsername(id);
                     studentsInfo.setPassword(passwd);
                     studentsInfo.login(new SaveListener<StudentsInfo>() {
                         @Override
                         public void done(StudentsInfo studentsInfo, BmobException e) {
                             progressDialog.dismiss();
                             if(e==null){
                                 Toast.makeText(loginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                 loginSuccess(id,passwd,studentsInfo.getStudentName(),choosen,studentsInfo.getClazzes());
                             }else{
                                 Toast.makeText(loginActivity.this,"登录失败，请检查编号和密码",Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
          }else {
                 Toast.makeText(loginActivity.this,"请输入编号和密码",Toast.LENGTH_SHORT).show();

             }

            break;
         case R.id.btn_linkToRegisterScreen:
             Intent intent=new Intent(loginActivity.this,registerActivity.class);
             intent.putExtra("choosen",choosen);
             startActivity(intent);
            break;
        }
    }

    private void loginSuccess(String id, String passwd,String name, String choosen, List<Clazzes> clazzes) {
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("id",id);
        editor.putString("passwd",passwd);
        editor.putString("name",name);
        editor.putString("choosen",choosen);
        Gson gson = new Gson();
        String clazzesJson=gson.toJson(clazzes); //将List转换成Json
        editor.putString("clazzesJson", clazzesJson) ; //存入json串
                                         //clazzess班级列表本地持久化
        editor.apply();
        Intent intent=new Intent(loginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
