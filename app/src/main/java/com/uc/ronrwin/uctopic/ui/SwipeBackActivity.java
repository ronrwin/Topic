package com.uc.ronrwin.uctopic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.widget.SwipeBackLayout;


/**
 * 想要实现向右滑动删除Activity效果只需要继承SwipeBackActivity即可，如果当前页面含有ViewPager
 * 只需要调用SwipeBackLayout的setViewPager()方法即可
 * 
 * @author xiaanming
 *
 */
public class SwipeBackActivity extends Activity {
	protected SwipeBackLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
				R.layout.base, null);
		layout.attachToActivity(this);
	}

	protected void enterAnimation() {
		overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
	}

	protected void exitAnimation() {
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}

	@Override
	public void finish() {
		super.finish();
		exitAnimation();
	}
}
