/*
 * Author: Linaro Android Team <linaro-dev@lists.linaro.org>
 *
 * These files are Copyright (C) 2012 Linaro Limited and they
 * are licensed under the Apache License, Version 2.0.
 * You may obtain a copy of this license at
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package org.linaro.benchmarks;

import java.io.File;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.android.uiautomator.core.UiObjectNotFoundException;

public class BenchmarksTestCase extends UiAutomatorTestCase {

    String TAG = "BenchmarksTestCase";

    String png_dir = "/data/local/tmp/";

    public boolean takeScreenshot(String name){
        try{
            //only avaliable for 4.2
            return getUiDevice().takeScreenshot(
                    new File(png_dir + name + ".png"));
        }catch(NoSuchMethodError e){
            return true;
        }
    }

    public void sleep(int second){
        super.sleep(second * 1000);
    }

    public void waitText(String text) throws UiObjectNotFoundException{
        waitText(text, 600);
    }

    public void waitText(String text, int second)
                                throws UiObjectNotFoundException{
        UiSelector selector = new UiSelector();
        UiObject text_obj = new UiObject(selector.text(text)
                                       .className("android.widget.TextView"));
        waitObject(text_obj, second);
    }

    public void waitObject(UiObject obj) throws UiObjectNotFoundException{
        waitObject(obj, 600);
    }

    public void waitObject(UiObject obj, int second)
                                throws UiObjectNotFoundException{
        if (! obj.waitForExists(second * 1000)){
            throw new UiObjectNotFoundException("UiObject is not found: "
                    + obj.getSelector().toString());
        }
    }

    public boolean waitUntilNoObject(UiObject obj, int second){
        return obj.waitUntilGone(second * 1000);
    }

    public void testOrientationNatural() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        //Application specific
        getUiDevice().setOrientationNatural();
        getUiDevice().freezeRotation();

        getUiDevice().unfreezeRotation();
        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testGeekbench() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        //application related
        UiSelector selector = new UiSelector();
        UiObject btn_run = new UiObject(selector.text("Run Benchmarks")
                                       .className("android.widget.Button"));
        btn_run.click();
        try{
            UiObject res_webview = new UiObject(selector.className(
                                                "android.webkit.WebView"));
            waitObject(res_webview);
            takeScreenshot("geekbench-0");
            sleep(2);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN);
            takeScreenshot("geekbench-1");
            sleep(2);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN);
            takeScreenshot("geekbench-2");
            sleep(2);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN);
            takeScreenshot("geekbench-3");
            sleep(2);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN);
            takeScreenshot("geekbench-4");

        }catch(UiObjectNotFoundException e){
            takeScreenshot("geekbench-error");
        }
        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testLinpack() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        UiSelector selector = new UiSelector();
        UiObject btn_st = new UiObject(selector.text("Run Single Thread"));
        btn_st.click();
        sleep(10);
        takeScreenshot("linpack-st");
        UiObject btn_mt = new UiObject(selector.text("Run Multi-Thread"));
        btn_mt.click();
        sleep(10);
        takeScreenshot("linpack-mt");
        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testAntutu() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        UiSelector selector = new UiSelector();
        //
        //click the Test test
        UiObject text_scores = new UiObject(selector.text("Scores")
                                       .className("android.widget.TextView"));
        text_scores.click();
        sleep(2);

        //click the Test test
        UiObject text_detail = new UiObject(selector.text("Detailed Scores")
                                       .className("android.widget.TextView"));
        text_detail.click();
        sleep(2);

        //click the Test test
        UiObject text_test = new UiObject(selector.text("Test")
                                       .className("android.widget.TextView"));
        text_test.click();
        sleep(2);

        Log.v(TAG, "Start the test");
        //begin the test
        UiObject btn_test = new UiObject(selector.text("Start Test")
                                       .className("android.widget.Button"));
        btn_test.click();
        try{
            UiObject submit = new UiObject(selector
                    .text("Submit Scores")
                    .className("android.widget.TextView"));
            UiObject detail = new UiObject(selector
                    .text("Detailed Scores")
                    .className("android.widget.TextView"));
            for(int i = 0; i < 60; i++){
                if (detail.exists() || submit.exists()){
                    break;
                }
                sleep(10);
            }
        }finally{
            takeScreenshot("antutu");
        }
        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testCaffeine() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        //Application specific
        UiSelector selector = new UiSelector();
        UiObject btn_run = new UiObject(selector.text("Run benchmark")
                                       .className("android.widget.Button"));
        btn_run.click();

        try{
            waitText("CaffeineMark results");
            takeScreenshot("caffeine-0");
            UiObject btn_details = new UiObject(selector.text("Details")
                                       .className("android.widget.Button"));
            btn_details.click();
            sleep(2);
            takeScreenshot("caffeine-1");
        }catch(UiObjectNotFoundException e){
            takeScreenshot("caffeine-error");
        }

        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testAndEBench() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        //Application specific
        getUiDevice().setOrientationNatural();
        UiSelector selector = new UiSelector();
        UiObject btn_start = new UiObject(selector
                                    .className("android.widget.ImageButton")
                                    .packageName("com.eembc.coremark"));
        btn_start.click();

        try{
            UiObject running_text = new UiObject(selector
                                    .textContains("Running...")
                                    .className("android.widget.TextView")
                                    .packageName("com.eembc.coremark"));
            waitUntilNoObject(running_text, 600);

            UiObject result_text = new UiObject(selector
                                    .textContains("Results in Iterations/sec:")
                                    .className("android.widget.TextView")
                                    .packageName("com.eembc.coremark"));
            waitObject(result_text);
            sleep(2);
            takeScreenshot("AndEBench-0");
        }catch(UiObjectNotFoundException e){
            takeScreenshot("AndEBench-error");
        }

        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testNBench() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        //Application specific
        getUiDevice().setOrientationNatural();
        UiSelector selector = new UiSelector();
        UiObject btn_start = new UiObject(selector.text("Start the benchmark")
                                    .className("android.widget.Button")
                                    .packageName("com.drolez.nbench"));
        btn_start.click();

        try{

            UiObject running_text = new UiObject(selector
                                    .textContains("Benchmark running.")
                                    .className("android.widget.TextView")
                                    .packageName("com.drolez.nbench"));
            waitUntilNoObject(running_text, 600);

            UiObject result_text = new UiObject(selector
                                    .textContains("Last run:")
                                    .className("android.widget.TextView")
                                    .packageName("com.drolez.nbench"));
            waitObject(result_text);
            sleep(2);
            takeScreenshot("NBench-0");
        }catch(UiObjectNotFoundException e){
            takeScreenshot("NBench-error");
        }

        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testQuadrant() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        //Application specific
        getUiDevice().setOrientationNatural();
        UiSelector selector = new UiSelector();
        try{
            waitText("Information");
            UiObject btn_ok = new UiObject(selector.text("OK")
                     .className("android.widget.Button")
                     .packageName("com.aurorasoftworks.quadrant.ui.standard"));
            if (btn_ok.exists()){
                btn_ok.click();
            }
        }catch(UiObjectNotFoundException e){
            //do nothing
        }


        UiObject text_run = new UiObject(selector.text("Run full benchmark")
                     .className("android.widget.TextView")
                     .packageName("com.aurorasoftworks.quadrant.ui.standard"));
        text_run.click();
        waitText("Benchmark result");
        UiObject btn_yes = new UiObject(selector.text("Yes")
                     .className("android.widget.Button")
                     .packageName("com.aurorasoftworks.quadrant.ui.standard"));
        if (btn_yes.exists()){
            btn_yes.click();
        }
        UiObject text_sending = new UiObject(selector
                                       .text("Sending benchmark results...")
                                       .className("android.widget.TextView"));
        try{
            waitObject(text_sending);
        }catch(UiObjectNotFoundException e){
            takeScreenshot("quadrant-no-sending");
        }

        waitUntilNoObject(text_sending, 600);
        sleep(3);
        takeScreenshot("quadrant");

        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testVellamo() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        //Application specific
        getUiDevice().setOrientationNatural();
        UiSelector selector = new UiSelector();
        try{
            waitText("Vellamo EULA");
            UiObject btn_accept = new UiObject(selector.text("Accept")
                     .className("android.widget.Button")
                     .packageName("com.quicinc.vellamo"));
            if (btn_accept.exists()){
                btn_accept.click();
            }
        }catch(UiObjectNotFoundException e){
            //do nothing
        }

        UiObject btn_no = new UiObject(selector.text("No")
                     .className("android.widget.Button")
                     .packageName("com.quicinc.vellamo"));
        try{
            waitText(
               "Would you like to edit the list of websites Vellamo accesses?");
            if (btn_no.exists()){
                btn_no.click();
            }
        }catch(UiObjectNotFoundException e){
            //do nothing
        }

        UiObject text_start = new UiObject(selector.text("Start")
                     .className("android.widget.TextView")
                     .packageName("com.quicinc.vellamo"));
        text_start.click();

        waitText("Enable Tutorial?");
        btn_no.click();
        try{
            waitText("Vellamo Score");
            btn_no.click();
            sleep(3);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_BACK);
            takeScreenshot("vellamo-0");
            sleep(2);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN);
            takeScreenshot("vellamo-1");
            sleep(2);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_PAGE_DOWN);
            takeScreenshot("vellamo-2");
            sleep(2);
        }catch(UiObjectNotFoundException e){
            takeScreenshot("vellamo-error");
            throw e;
        }

        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }

    public void testGLBenchmark() throws Exception{
        Bundle status = new Bundle();
        status.putString("product", getUiDevice().getProductName());
        Point p = getUiDevice().getDisplaySizeDp();
        status.putInt("dp-width", p.x);
        status.putInt("dp-height", p.y);
        //Application specific
        getUiDevice().setOrientationNatural();
        UiSelector selector = new UiSelector();

        UiObject download_fail = new UiObject(selector.text("Retry Download")
                     .className("android.widget.Button"));
        if (download_fail.exists()){
            takeScreenshot("glbenchmark-retry");
            throw new Exception(
                    "Download failed because the resources could not be found");
        }
        UiObject text_perf_test = new UiObject(selector
                               .text("Performance Tests")
                               .className("android.widget.TextView"));
        text_perf_test.click();
        sleep(3);

        UiObject btn_all = new UiObject(selector.text("All")
                     .className("android.widget.Button"));
        btn_all.click();
        sleep(1);
        //uncheck the crash item
        UiObject crash_item = new UiObject(selector
                               .text("C24Z24MS4")
                               .className("android.widget.TextView"));
        crash_item.click();

        UiObject btn_start= new UiObject(selector.text("Start")
                     .className("android.widget.Button"));
        btn_start.click();
        try{
            UiObject error = new UiObject(selector
                    .text("Error during network communication!")
                    .className("android.widget.TextView"));
            UiObject processing = new UiObject(selector
                    .text("Processing results...")
                    .className("android.widget.TextView"));
            UiObject results = new UiObject(selector
                    .text("Results")
                    .className("android.widget.TextView"));
            for(int i = 0; i < 360; i++){
                if (results.exists()){
                    break;
                }
                if (processing.exists()){
                    continue;
                }

                if (error.exists()){
                    UiObject btn_retry= new UiObject(selector.text("Retry")
                     .className("android.widget.Button"));
                    btn_retry.click();
                }
                sleep(10);
            }
            waitText("Results");
            sleep(2);
            takeScreenshot("glbenchmark-0");
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            takeScreenshot("glbenchmark-1");
            sleep(1);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            sleep(1);
            takeScreenshot("glbenchmark-2");
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            sleep(1);
            takeScreenshot("glbenchmark-3");
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            getUiDevice().pressKeyCode(KeyEvent.KEYCODE_DPAD_DOWN);
            sleep(1);
            takeScreenshot("glbenchmark-4");
        }catch(UiObjectNotFoundException e){
            takeScreenshot("glbenchmark-error");
            throw e;
        }

        getAutomationSupport().sendStatus(Activity.RESULT_OK, status);
    }
}
