package com.example.easyappointment.data.Models;

import android.content.Context;

import io.objectbox.BoxStore;

public class ObjectBox {
    private static BoxStore boxStore = null;

    public static void init (Context context) {
         boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();
    }

    public static BoxStore get(){
        return boxStore;
    }
}
