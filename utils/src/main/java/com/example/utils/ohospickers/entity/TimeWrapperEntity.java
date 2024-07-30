package com.example.utils.ohospickers.entity;

import java.io.Serializable;

/**
 * 类描述：封装服务器返回的时间
 */
public class TimeWrapperEntity implements Serializable {
  private long slotBegin;//开始时间
  private long slotEnd;//结束时间
  private String startTime;//HH:mm
  private String endTime;//HH:mm
  private String showTime;//用于界面显示的字符串

  public TimeWrapperEntity(long slotBegin, String startTime, long slotEnd, String endTime) {
    this.slotBegin = slotBegin;
    this.slotEnd = slotEnd;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public TimeWrapperEntity(long slotBegin, String startTime, long slotEnd, String endTime, String showTime) {
    this.slotBegin = slotBegin;
    this.slotEnd = slotEnd;
    this.startTime = startTime;
    this.endTime = endTime;
    this.showTime = showTime;
  }

  public String getShowTime() {
    return showTime;
  }

  public void setShowTime(String showTime) {
    this.showTime = showTime;
  }


  public TimeWrapperEntity(String showTime) {
    this.showTime = showTime;
  }

  public long getSlotBegin() {
    return slotBegin;
  }

  public void setSlotBegin(long slotBegin) {
    this.slotBegin = slotBegin;
  }

  public long getSlotEnd() {
    return slotEnd;
  }

  public void setSlotEnd(long slotEnd) {
    this.slotEnd = slotEnd;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

}
