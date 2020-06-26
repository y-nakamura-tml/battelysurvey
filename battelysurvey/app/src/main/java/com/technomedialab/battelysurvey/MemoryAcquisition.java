package com.technomedialab.battelysurvey;

import android.util.Log;

public class MemoryAcquisition {

    void getmemory(StringBuilder logStr){

        // メモリ情報を取得
        Runtime runtime = Runtime.getRuntime();

        if (runtime != null) {
            // トータルメモリ
            Log.d(LogUtility.TAG(this), "全メモリ[KB]" + Const.CSV_BREAK + (int) (runtime.totalMemory() / 1024));
            logStr.append(GetTimestamp.getNowDate() + "全メモリ[KB]" + Const.CSV_BREAK + (int) (runtime.totalMemory() / 1024) + "\n");

            // 空きメモリ
            Log.d(LogUtility.TAG(this), "空きメモリ[KB]" + Const.CSV_BREAK + (int) (runtime.freeMemory() / 1024));
            logStr.append(GetTimestamp.getNowDate() + "空きメモリ[KB]" + Const.CSV_BREAK + (int) (runtime.freeMemory() / 1024) + "\n");

            // 使用メモリ
            Log.d(LogUtility.TAG(this), "使用メモリ[KB]" + Const.CSV_BREAK + (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024));
            logStr.append(GetTimestamp.getNowDate() + "使用メモリ[KB]" + Const.CSV_BREAK + (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024) + "\n");

            // 使用メモリ
            Log.d(LogUtility.TAG(this), "Dalvikで使用できる最大メモリ[KB]" + Const.CSV_BREAK + (int) (runtime.maxMemory() / 1024));
            logStr.append(GetTimestamp.getNowDate() + "Dalvikで使用できる最大メモリ[KB]" + Const.CSV_BREAK + (int) (runtime.maxMemory() / 1024) + "\n");
        }

    }
}
