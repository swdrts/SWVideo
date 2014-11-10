package com.swdrts.swvideo.srcparse;

public interface VideoSrcCallback {

    void onFinish(String src);
    void onFailed(String reason);
}
