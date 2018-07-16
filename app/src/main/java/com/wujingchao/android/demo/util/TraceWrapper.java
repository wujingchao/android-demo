package com.wujingchao.android.demo.util;

import android.os.Build;
import android.os.Trace;

public final class TraceWrapper {

    public static void beginSection(String sectionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.beginSection(sectionName);
        }
    }

    public static void endSection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }
    }

}
