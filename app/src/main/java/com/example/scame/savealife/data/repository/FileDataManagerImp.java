package com.example.scame.savealife.data.repository;

import android.os.Build;
import android.os.Environment;

import java.io.File;

public class FileDataManagerImp implements IFileDataManager {

    @Override
    public File getMapsFolder() {

        File mapsFolder;
        boolean greaterOrEqKitkat = Build.VERSION.SDK_INT >= 19;

        if (greaterOrEqKitkat) {
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return null;
            }
            mapsFolder = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS), "/graphhopper/maps/");
        } else {
            mapsFolder = new File(Environment.getExternalStorageDirectory(), "/graphhopper/maps/");
        }

        return mapsFolder;
    }

    public static String getFileName(String str ) {
        int index = str.lastIndexOf("/");
        if (index > 0) {
            return str.substring(index + 1);
        }
        return str;
    }
}
