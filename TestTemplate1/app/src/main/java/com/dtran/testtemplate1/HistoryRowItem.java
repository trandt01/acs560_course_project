package com.dtran.testtemplate1;

public class HistoryRowItem {
    private long id;
    private String title;
    private String desc;
    private long selectDate;

    public HistoryRowItem(long id,String title, String desc,long selectDate) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.selectDate = selectDate;
    }

    public long getSelectDate() {
        return selectDate;
    }
    public void setSelectDate(long selectDate) {
        this.selectDate = selectDate;
    }

    public long getID() {
        return id;
    }
    public void setID(long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}
