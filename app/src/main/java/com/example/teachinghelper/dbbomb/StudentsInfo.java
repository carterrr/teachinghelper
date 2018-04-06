package com.example.teachinghelper.dbbomb;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by 愿初心常在 on 2018/3/24.
 */

public class StudentsInfo extends BmobUser {
    //id用父类的username--id  password--passwd直接继承
    private String studentName;
    private String localmac;
    private List<Clazzes> clazzes;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getLocalmac() {
        return localmac;
    }

    public void setLocalmac(String localmac) {
        this.localmac = localmac;
    }

    public List<Clazzes> getClazzes() {
        return clazzes;
    }

    public void setClazzes(List<Clazzes> clazzes) {
        this.clazzes = clazzes;
    }
}
