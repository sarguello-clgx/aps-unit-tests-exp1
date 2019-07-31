package com.activiti.extension.model;

import java.util.List;

public class ListDataBean {
    List<LabelValueBean> data;

    public ListDataBean(List<LabelValueBean> data) {
        this.data = data;
    }

    public List<LabelValueBean> getData() {
        return data;
    }

    public void setData(List<LabelValueBean> data) {
        this.data = data;
    }
}
