package com.example.teachinghelper.dbbomb;

import java.util.List;

import cn.bmob.v3.BmobObject;


/**
 * Created by 愿初心常在 on 2018/3/26.
 */

public class TeacherInfo extends BmobObject{
    private String teacherId;
    private String  password;
    private String teacherName; //不能由username代替，否则学生与老师表合并了
    private List<Clazzes> clazzes;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<Clazzes> getClazzes() {
        return clazzes;
    }

    public void setClazzes(List<Clazzes> clazzes) {
        this.clazzes = clazzes;
    }
}
