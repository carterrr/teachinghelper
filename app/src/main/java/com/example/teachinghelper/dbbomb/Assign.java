package com.example.teachinghelper.dbbomb;

import cn.bmob.v3.BmobObject;

/**
 * Created by 愿初心常在 on 2018/3/26.
 */
//开始新建时需要初始化 用clazzes表中单个clazz的学生列表初始化，计数一开始均为0
public class Assign extends BmobObject{
    private String studentId;
    private Integer assignCounter;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getAssignCounter() {
        return assignCounter;
    }

    public void setAssignCounter(Integer assignCounter) {
        this.assignCounter = assignCounter;
    }
}
