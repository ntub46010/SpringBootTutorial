package com.vincent.demo.util;

import java.beans.PropertyEditorSupport;

public class ToSearchTextEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        var t = CommonUtil.toSearchText(text);
        super.setValue(t);
    }
}
