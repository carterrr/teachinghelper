package com.example.teachinghelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teachinghelper.dbbomb.Clazzes;
import com.example.teachinghelper.dbbomb.StudentsInfo;
import com.example.teachinghelper.dbbomb.TeacherInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

private NavigationView navigationView;
private DrawerLayout mDrawerLayout;
private String id;
private String name;
private String choosen;
private List<Clazzes> clazzes;
private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

    }

    private void initData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        id=prefs.getString("id", null);
        name=prefs.getString("name",null);
        choosen=prefs.getString("choosen", null);
        String clazzesJson = prefs.getString("clazzesJson","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if(clazzesJson!="")  //防空判断
        {
            Toast.makeText(this,clazzesJson,Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            clazzes = gson.fromJson(clazzesJson, new TypeToken<List<Clazzes>>() {}.getType()); //将json字符串转换成List集合
        }
    }

    private void initView() {

        navigationView=(NavigationView)findViewById(R.id.nav_view);
        menu=navigationView.getMenu();

        if (choosen.equals("teacher")){
            menu.findItem(R.id.nav_call).setVisible(false);
            menu.findItem(R.id.nav_friends).setVisible(false);
            menu.findItem(R.id.nav_location).setVisible(false);
            menu.findItem(R.id.nav_mail).setVisible(false);
            menu.findItem(R.id.nav_task).setVisible(false);

        }else {
            menu.findItem(R.id.nav_call1).setVisible(false);
            menu.findItem(R.id.nav_friends1).setVisible(false);
            menu.findItem(R.id.nav_location1).setVisible(false);
            menu.findItem(R.id.nav_mail1).setVisible(false);
            menu.findItem(R.id.nav_task1).setVisible(false);

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
