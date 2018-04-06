package com.example.teachinghelper.dbbomb;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 愿初心常在 on 2018/3/26.
 */

public class Clazzes  extends BmobObject {
    private String  clazzId;
    private String  clazzName;
    private String  teacherId;
    private Assign assign;
    private Integer totalCounter;//统计总的签到次数
    private List<StudentsInfo> students;


    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getClazzId() {
        return clazzId;
    }

    public void setClazzId(String clazzId) {
        this.clazzId = clazzId;
    }

    public Assign getAssign() {
        return assign;
    }

    public void setAssign(Assign assign) {
        this.assign = assign;
    }

    public Integer getTotalCounter() {
        return totalCounter;
    }

    public void setTotalCounter(Integer totalCounter) {
        this.totalCounter = totalCounter;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public List<StudentsInfo> getStudents() {
        return students;
    }

    public void setStudents(List<StudentsInfo> students) {
        this.students = students;
    }
}
