package com.uc.ronrwin.uctopic.http;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/6/12
 * Author      : Ronrwin
 */

public interface LoadServerDataListener<T> {
    void onFailure(String message);
    void onSuccess(T data);
}
