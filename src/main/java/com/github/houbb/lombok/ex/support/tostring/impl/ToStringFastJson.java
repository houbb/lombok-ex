package com.github.houbb.lombok.ex.support.tostring.impl;

import com.alibaba.fastjson.JSON;
import com.github.houbb.lombok.ex.support.tostring.IToString;

/**
 * @author binbin.hou
 * @since 0.0.4
 */
public class ToStringFastJson implements IToString {

    @Override
    public String toString(Object instance) {
        return JSON.toJSONString(instance);
    }

}
