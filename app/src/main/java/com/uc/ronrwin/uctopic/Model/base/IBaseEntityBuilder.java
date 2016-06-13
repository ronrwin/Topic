package com.uc.ronrwin.uctopic.model.base;

import org.json.JSONObject;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/30
 * Author      : Ronrwin
 */
public interface IBaseEntityBuilder<T> {

    public T create(JSONObject json);
}
