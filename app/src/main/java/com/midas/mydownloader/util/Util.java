package com.midas.mydownloader.util;

import android.os.Environment;

public class Util
{
    //----------------------------------------------------------------------------------------------------
    //
    public static String getExternalStorageDir()
    {
        String path = null;
        String ext = Environment.getExternalStorageState();

        if(ext.equals(Environment.MEDIA_MOUNTED))
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        else
            path = Environment.MEDIA_UNMOUNTED;

        return path;
    }
}
