package com.example.teachinghelper.loginAndRegister;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teachinghelper.R;
import com.example.teachinghelper.dbbomb.Assign;
import com.example.teachinghelper.dbbomb.Clazzes;
import com.example.teachinghelper.dbbomb.StudentsInfo;
import com.example.teachinghelper.dbbomb.TeacherInfo;
import com.example.teachinghelper.util.wifiUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class registerActivity extends AppCompatActivity implements View.OnClickListener{
private EditText edit_id;
private EditText edit_passwd;
private EditText edit_name;
private Button btn_clazzesSelect;
private Button btn_register;
private Button btn_linkToLoginScreen;
private String choosen;
private IntentFilter intentFilter;
private WifiStateChangeReceiver wifiStateChangeReceiver;
private WifiManager wifiManager;
private String id;
private String name;
private String passwd;
private String localmac;
private List<Clazzes> clazzes;
private ProgressDialog progressDialog = null;
private AlertDialog dialog;
private Button btn_test_addClazzess;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        intentFilter=new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        clazzes=new ArrayList<>();
        /*wifiStateChangeReceiver=new WifiStateChangeReceiver();
        registerReceiver(wifiStateChangeReceiver,intentFilter);*/
        Intent intent=getIntent();
        choosen=intent.getStringExtra("choosen");
        initView();


    }



    private void initView() {
        edit_id=(EditText)findViewById(R.id.idInput);
        edit_passwd=(EditText)findViewById(R.id.passwordInput);
        edit_name=(EditText)findViewById(R.id.nameInput);
        btn_clazzesSelect=(Button)findViewById(R.id.btn_clazzesSelect);
        btn_register=(Button)findViewById(R.id.btn_register);
        btn_linkToLoginScreen=(Button)findViewById(R.id.btn_linkToLoginScreen);
        btn_clazzesSelect.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_linkToLoginScreen.setOnClickListener(this);
        if(choosen.equals("teacher")){
            edit_id.setHint("教师编号");
            btn_clazzesSelect.setText("授课班级");
        }else if(choosen.equals("student")) {
            edit_id.setHint("学生编号");
        }

        btn_test_addClazzess=(Button)findViewById(R.id.btn_test_addclazzess);
        btn_test_addClazzess.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_clazzesSelect:

                progressDialog = ProgressDialog.show(registerActivity.this, "请稍等...", "正在搜索可选班级...", true);

                //先查询所有clazzes数据并保存
                //在回调函数中 dismiss()并showClazzes();
                BmobQuery<Clazzes> query=new BmobQuery<Clazzes>();
                query.findObjects(new FindListener<Clazzes>() {
                    @Override
                    public void done(List<Clazzes> clazzesList, BmobException e) {
                        if(e==null){
                            progressDialog.dismiss();
                            showClazzes(clazzesList);
                        }else {
                            Log.i("bmob","查询班级失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });


                break;
            case R.id.btn_register:
                id=edit_id.getText().toString();
                passwd=edit_passwd.getText().toString();
                name=edit_name.getText().toString();
                if(!id.isEmpty()&&!passwd.isEmpty()&&!name.isEmpty()){
                    progressDialog = ProgressDialog.show(registerActivity.this, "请稍等...", "正在注册中...", true);
                     if(choosen.equals("teacher")){

                         TeacherInfo teacherInfo=new TeacherInfo();
                         teacherInfo.setTeacherId(id);
                         teacherInfo.setTeacherName(name);
                         teacherInfo.setPassword(passwd);
                         teacherInfo.setClazzes(clazzes);
                         teacherInfo.save(new SaveListener<String>() {
                             @Override
                             public void done(String s, BmobException e) {
                                 if(e==null){
                                     progressDialog.dismiss();
                                     Toast.makeText(registerActivity.this,"教师信息注册成功",Toast.LENGTH_SHORT).show();
                                     finish();
                                 }else{
                                     Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                     Toast.makeText(registerActivity.this,"教师信息注册失败",Toast.LENGTH_SHORT).show();

                                 }
                             }
                         });
                     }else if(choosen.equals("student")){
                                                                    //若刚开机时没开过wifi ，加入打开wifi再关闭然后才能获取正确的mac地址，否则重启后是错误的
                                                                    //若已经开过wifi就不用管
                         if(!wifiManager.isWifiEnabled())
                         {
                             wifiStateChangeReceiver=new WifiStateChangeReceiver();
                             registerReceiver(wifiStateChangeReceiver,intentFilter);
                             wifiManager.setWifiEnabled(true);

                         }else {
                             localmac=wifiUtil.getLocalMac(registerActivity.this);
                             studentSingup(id,passwd,name,localmac,clazzes);
                         }

                     }
                }else{
                    Toast.makeText(registerActivity.this,"请完整填写信息",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_linkToLoginScreen:
                finish();
                break;
           /* case R.id.btn_test_addclazzess:
               final Clazzes clazzes=new Clazzes();
                clazzes.setClazzId("1");
                clazzes.setClazzName("微机原理");
                clazzes.setTeacherId("1");
                clazzes.setTotalCounter(0);
                final Assign assign=new Assign();
                assign.setAssignCounter(0);
                assign.setStudentId("mako");
                assign.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        clazzes.setAssign(assign);
                        clazzes.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                    Toast.makeText(registerActivity.this,"存储课程信息成功",Toast.LENGTH_SHORT).show();

                                }else{
                                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                });

                break;*/
        }
    }

    private void showClazzes(final List<Clazzes> clazzesList) {
             final String clazzesItems[]=new String[clazzesList.size()];
             int i=0;
            for(Clazzes clazzes:clazzesList)
            {
               clazzesItems[i++]= clazzes.getClazzName();
            }

            // 创建一个AlertDialog建造者
            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(registerActivity.this);
            // 设置标题
            if(choosen.equals("teacher")){
               alertDialogBuilder.setTitle("授课班级");
             }else{
               alertDialogBuilder.setTitle("所在班级");
            }

            // 参数介绍
            // 第一个参数：弹出框的信息集合，一般为字符串集合
            // 第二个参数：被默认选中的，一个布尔类型的数组
            // 第三个参数：勾选事件监听
            alertDialogBuilder.setMultiChoiceItems(clazzesItems, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    // dialog：不常使用，弹出框接口
                    // which：勾选或取消的是第几个
                    // isChecked：是否勾选
                    if (isChecked) {
                        // 选中
                        clazzes.add(clazzesList.get(which));
                        //Toast.makeText(registerActivity.this, "选中"+clazzesItems[which], Toast.LENGTH_SHORT).show();

                    }else {
                        // 取消选中
                        clazzes.remove(clazzesList.get(which));
                       // Toast.makeText(registerActivity.this, "取消选中"+clazzesItems[which], Toast.LENGTH_SHORT).show();
                    }

                }
            });
            alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //TODO 业务逻辑代码
                    //Toast.makeText(registerActivity.this, clazzes.get(0).getClazzName(), Toast.LENGTH_SHORT).show();  经验证成功存入clazzes
                    // 关闭提示框
                    dialog.dismiss();
                }
            });
            alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO 业务逻辑代码

                    // 关闭提示框
                    dialog.dismiss();
                }
            });
            dialog = alertDialogBuilder.create();
            dialog.show();

    }

    class WifiStateChangeReceiver extends BroadcastReceiver{//直接就调用了？？？,因为wifi已经打开会直接调用

        @Override
        public void onReceive(Context context, Intent intent) {
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                switch(wifiState) {
                    case WifiManager.WIFI_STATE_ENABLED:         //Log.d("Tag", "--- WIFI状态：已打开WIFI功能 ---");
                        localmac= wifiUtil.getLocalMac(registerActivity.this);
                        studentSingup(id,passwd,name,localmac,clazzes);
                        if(wifiManager.isWifiEnabled())
                        {
                            wifiManager.setWifiEnabled(false);//异步未处理~~~？？？若果开关都是多线程要分开写
                        }
                        break;
                }
            }
        }
    }

    //两层回调调用  ，外层注册内层添加学生对象到班级，最后assign表当中也加入学生
    private void studentSingup(final String id, String passwd, String name, String localmac, final List<Clazzes> clazzes) {
        StudentsInfo studentsInfo=new StudentsInfo();
        studentsInfo.setUsername(id);
        studentsInfo.setPassword(passwd);
        studentsInfo.setStudentName(name);
        studentsInfo.setLocalmac(localmac);
        studentsInfo.setClazzes(clazzes);
        studentsInfo.signUp(new SaveListener<StudentsInfo>() {
            @Override
            public void done(final StudentsInfo studentsInfo, BmobException e) {
                if(e==null){
                    //班级数据库当中相应班级加入学生信息，一并加入到签到表
                    for(Clazzes classes:clazzes){
                        Clazzes clazzes1=new Clazzes();
                        clazzes1.setObjectId(classes.getObjectId());
                        clazzes1.add("students",studentsInfo);
                        clazzes1.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Assign assign=new Assign();
                                    assign.setStudentId(id);
                                    assign.setAssignCounter(0);
                                    assign.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if(e==null){
                                                progressDialog.dismiss();
                                                Toast.makeText(registerActivity.this,"学生信息注册成功",Toast.LENGTH_SHORT).show();
                                                Log.e("assignsave","assign更新成功");
                                                finish();
                                            }else{
                                                Log.e("assignsave","assign更新失败："+e.getMessage());
                                            }
                                        }
                                    });
                                //finish();放在这里会抛出错误，因为progressdialog在dismiss前就把上下文给杀了
                                }else{
                                    Log.e("bmob","更新失败："+e.getMessage());
                                }
                            }
                        });


                    }
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(registerActivity.this,"学生信息注册失败",Toast.LENGTH_SHORT).show();
                    Log.e("studentInfoSave","学生信息注册失败："+e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wifiStateChangeReceiver != null)
        {
            unregisterReceiver(wifiStateChangeReceiver);
        }

    }

}
