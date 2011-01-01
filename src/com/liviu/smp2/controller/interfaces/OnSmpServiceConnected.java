package com.liviu.smp2.controller.interfaces;

import com.liviu.smp2.services.SmpService;

import android.content.ComponentName;
import android.os.IBinder;

public interface OnSmpServiceConnected {
	public void onSmpServiceConnected(ComponentName name, IBinder service, SmpService smpService);
}
