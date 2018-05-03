package com.xyf.common;

import com.xyf.common.annotation.UiThread;
import com.xyf.common.util.RefreshableRxLifeCircle;
import javafx.fxml.Initializable;

public abstract class BaseController extends RefreshableRxLifeCircle implements Initializable {

    @UiThread
    public void onStop() {
        clearDisposable();
    }

}
