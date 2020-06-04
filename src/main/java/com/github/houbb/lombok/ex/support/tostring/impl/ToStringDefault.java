package com.github.houbb.lombok.ex.support.tostring.impl;

import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.lombok.ex.support.tostring.IToString;

/**
 * @author binbin.hou
 * @since 0.0.6
 */
@ThreadSafe
public class ToStringDefault implements IToString {

    @Override
    public String toString(Object instance) {
        return StringUtil.EMPTY;
    }

}
