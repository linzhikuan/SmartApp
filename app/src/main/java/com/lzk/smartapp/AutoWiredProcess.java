package com.lzk.smartapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;

public class AutoWiredProcess {

    public static void initBindView(Object currentClass, View sourceView) {
        Field[] fields = currentClass.getClass().getDeclaredFields();
        if( fields.length > 0) {
            int var5 = fields.length;
            for(int var4 = 0; var4 < var5; ++var4) {
                Field field = fields[var4];
                BindView bindView = field.getAnnotation(BindView.class);
                if(bindView != null) {
                    int viewId = bindView.id();
                    boolean clickLis = bindView.click();

                    try {
                        field.setAccessible(true);
                        if(clickLis) {
                            sourceView.findViewById(viewId).setOnClickListener((View.OnClickListener)currentClass);
                        }

                        field.set(currentClass, sourceView.findViewById(viewId));
                    } catch (Exception var11) {
                        var11.printStackTrace();
                    }
                }
            }
        }

    }

    public static void initBindView(Activity aty) {
        initBindView(aty, aty.getWindow().getDecorView());
    }

    public static void initBindView(View view) {
        Context cxt = view.getContext();
        if(cxt instanceof Activity) {
            initBindView((Activity)cxt);
        } else {
            Log.d("AnnotateUtil.java", "the view don\'t have root view");
        }
    }

    @TargetApi(11)
    public static void initBindView(Fragment frag) {
        initBindView(frag, frag.getActivity().getWindow().getDecorView());
    }

}