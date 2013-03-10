/*
 * Author: Linaro Android Team <linaro-dev@lists.linaro.org>
 *
 * These files are Copyright (C) 2012 Linaro Limited and they
 * are licensed under the Apache License, Version 2.0.
 * You may obtain a copy of this license at
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.linaro;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.android.uiautomator.core.UiObjectNotFoundException;

public class SettingsTestCase extends UiAutomatorTestCase {

    public void clickText(String text)throws UiObjectNotFoundException{
        UiSelector selector = new UiSelector();
        UiObject obj = new UiObject(selector.text(text)
                                       .className("android.widget.TextView"));
        obj.click();
    }
    public void openSettingsApp() throws UiObjectNotFoundException{
        getUiDevice().pressHome();
        getUiDevice().pressMenu();
        UiSelector selector = new UiSelector();
        UiObject btn_setting = new UiObject(selector.text("System settings")
                                       .className("android.widget.TextView")
                                       .packageName("com.android.launcher"));
        btn_setting.click();

    }

    public void testSetSleep30Minutes() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        //application related
        openSettingsApp();
        UiScrollable settingsItem = new UiScrollable(new UiSelector()
                   .className("android.widget.ListView"));
        UiObject item = settingsItem.getChildByText(new UiSelector()
                   .className("android.widget.LinearLayout"), "Display");
        item.click();

        clickText("Sleep");
        UiSelector selector = new UiSelector();
        UiObject checkText = new UiObject(selector.text("30 minutes")
                                  .className("android.widget.CheckedTextView"));
        checkText.click();
    }

    public void testSetScreenLockNone() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        //application related
        openSettingsApp();
        UiScrollable settingsItem = new UiScrollable(new UiSelector()
                   .className("android.widget.ListView"));
        UiObject item = settingsItem.getChildByText(new UiSelector()
                   .className("android.widget.LinearLayout"), "Security");
        item.click();

        clickText("Screen lock");
        clickText("None");
    }

    public void testSetStayAwake() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        //application related
        openSettingsApp();
        UiScrollable settingsItem = new UiScrollable(new UiSelector()
                   .className("android.widget.ListView"));
        UiObject item = settingsItem.getChildByText(new UiSelector()
                .className("android.widget.LinearLayout"), "Developer options");
        item.click();

        UiScrollable item_items = new UiScrollable(new UiSelector()
                   .className("android.widget.ListView").instance(1));
        UiObject stayawake_item = item_items.getChildByText(new UiSelector()
                .className("android.widget.RelativeLayout"), "Stay awake");

        UiObject check_box = stayawake_item.getFromParent(new UiSelector()
                .className("android.widget.CheckBox"));
        if (! check_box.isChecked()){
            check_box.click();
        }
    }
}
