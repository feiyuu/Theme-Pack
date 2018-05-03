package com.xyf.common.util;

import com.xyf.common.annotation.UiThread;

public interface Refreshable {

    @UiThread
    void refresh();

}
