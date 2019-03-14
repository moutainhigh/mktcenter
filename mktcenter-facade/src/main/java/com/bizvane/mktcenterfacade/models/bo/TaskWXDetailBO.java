package com.bizvane.mktcenterfacade.models.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: lijunwei
 * @Time: 2018/8/15 11:50
 */
public class TaskWXDetailBO {
    private Long mktTaskId;
    private String taskName ;
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            locale = "zh",
            timezone = "GMT+8"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date startTime;
  @JsonFormat(
            pattern = "yyyy-MM-dd",
            locale = "zh",
            timezone = "GMT+8"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date   endTime;
    private  String  taskInfo;
    private Integer points;
    List<TaskCouponWXBO>  taskCouponWXBOs;

    public Long getMktTaskId() {
        return mktTaskId;
    }

    public void setMktTaskId(Long mktTaskId) {
        this.mktTaskId = mktTaskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<TaskCouponWXBO> getTaskCouponWXBOs() {
        return taskCouponWXBOs;
    }

    public void setTaskCouponWXBOs(List<TaskCouponWXBO> taskCouponWXBOs) {
        this.taskCouponWXBOs = taskCouponWXBOs;
    }
}