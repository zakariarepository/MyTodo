package com.example.mytodo.Database;

import android.app.Activity;

public class SQLExecution {
    private Activity activity;
    public interface SQLWithReturn {
        void execute();
        void executeOnUI();
    }
    public SQLExecution( Activity activity){
        this.activity = activity;
    }
    public void runWithResult(SQLWithReturn exec){
        new Thread(new Runnable() {
            @Override
            public void run() {
                exec.execute();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        exec.executeOnUI();
                    }
                });
            }
        }).start();
    }
}
