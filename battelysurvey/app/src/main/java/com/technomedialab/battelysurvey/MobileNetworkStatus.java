package com.technomedialab.battelysurvey;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.CellSignalStrength;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

public class MobileNetworkStatus {

    private TelephonyManager tManager;

    @SuppressLint("MissingPermission")
    void cellInfo(StringBuilder logStr, Context context) {

        if (tManager == null) {
            tManager = ( TelephonyManager ) context.getSystemService(Context.TELEPHONY_SERVICE);
            //Log.d(LogUtility.TAG(this), "tManager" + tManager);

        }

        //List<CellInfo> cellInfoList;
        List<CellSignalStrength> signalInfoList;

        try {
            SignalStrength signalStrength = null;
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.P) {
                signalStrength = tManager.getSignalStrength();
                String[] parts = signalStrength.toString().split(" ");

                int CdmaDbm = signalStrength.getCdmaDbm();
                String mGsmSignalStrength = "mGsmSignalStrength" + Const.CSV_BREAK + parts[1];
                String mGsmBitErrorRate = "mGsmBitErrorRate" + Const.CSV_BREAK + parts[2];
                String cdmaDbm = "CdmaDbm" + Const.CSV_BREAK + parts[3];
                String cdmaEcio = "CdmaEcio" + Const.CSV_BREAK + parts[4];
                String evdoDbm = "EvdoDbm" + Const.CSV_BREAK + parts[5];
                String evdoEcio = "EvdoEcio" + Const.CSV_BREAK + parts[6];
                String evdoSnr = "EvdoSnr" + Const.CSV_BREAK + parts[7];
                String lteSignalStrength = "LteSignalStrength" + Const.CSV_BREAK + parts[8];
                String lteRsrp = "LteRsrp" + Const.CSV_BREAK + parts[9];
                String lteRsrq = "LteRsrq" + Const.CSV_BREAK + parts[10];
                String lteRssnr = "LteRssnr" + Const.CSV_BREAK + parts[11];
                String lteCqi = "LteCqi" + Const.CSV_BREAK + parts[12];
                String LteRsrpBoost = "LteRsrpBoost" + Const.CSV_BREAK + parts[13];
                String mTdScdmaRscp = "mTdScdmaRscp" + Const.CSV_BREAK + parts[14];
                String WcdmaSignalStrength = "WcdmaSignalStrength" + Const.CSV_BREAK + parts[15];
                String WcdmaRscpAsu = "WcdmaRscpAsu" + Const.CSV_BREAK + parts[16];
                String WcdmaRscp = "WcdmaRscp" + Const.CSV_BREAK + parts[17];


                Log.d(LogUtility.TAG(this), "3Gの電波強度" + Const.CSV_BREAK + CdmaDbm);
                logStr.append(GetTimestamp.getNowDate() + "3Gの電波強度" + Const.CSV_BREAK + CdmaDbm + "\n");

//                Log.d(LogUtility.TAG(this), mGsmSignalStrength);
//                logStr.append(GetTimestamp.getNowDate() + mGsmSignalStrength + "\n");
//                Log.d(LogUtility.TAG(this), mGsmBitErrorRate);
//                logStr.append(GetTimestamp.getNowDate() + mGsmBitErrorRate + "\n");
//                Log.d(LogUtility.TAG(this), cdmaDbm);
//                logStr.append(GetTimestamp.getNowDate() + cdmaDbm + "\n");
//                Log.d(LogUtility.TAG(this), cdmaEcio);
//                logStr.append(GetTimestamp.getNowDate() + cdmaEcio + "\n");
//                Log.d(LogUtility.TAG(this), evdoDbm);
//                logStr.append(GetTimestamp.getNowDate() + evdoDbm + "\n");
//                Log.d(LogUtility.TAG(this), evdoEcio);
//                logStr.append(GetTimestamp.getNowDate() + evdoEcio + "\n");
//                Log.d(LogUtility.TAG(this), evdoSnr);
//                logStr.append(GetTimestamp.getNowDate() + evdoSnr + "\n");
//                Log.d(LogUtility.TAG(this), evdoEcio);
//                logStr.append(GetTimestamp.getNowDate() + evdoEcio + "\n");
//                Log.d(LogUtility.TAG(this), lteSignalStrength);
//                logStr.append(GetTimestamp.getNowDate() + lteSignalStrength + "\n");
                Log.d(LogUtility.TAG(this), lteRsrp);
                logStr.append(GetTimestamp.getNowDate() + lteRsrp + "\n");
//                Log.d(LogUtility.TAG(this), lteRsrq);
//                logStr.append(GetTimestamp.getNowDate() + lteRsrq + "\n");
//                Log.d(LogUtility.TAG(this), lteRssnr);
//                logStr.append(GetTimestamp.getNowDate() + lteRssnr + "\n");
//                Log.d(LogUtility.TAG(this), lteCqi);
//                logStr.append(GetTimestamp.getNowDate() + lteCqi + "\n");
//                Log.d(LogUtility.TAG(this), LteRsrpBoost);
//                logStr.append(GetTimestamp.getNowDate() + LteRsrpBoost + "\n");
//                Log.d(LogUtility.TAG(this), mTdScdmaRscp);
//                logStr.append(GetTimestamp.getNowDate() + mTdScdmaRscp + "\n");
//                Log.d(LogUtility.TAG(this), WcdmaSignalStrength);
//                logStr.append(GetTimestamp.getNowDate() + WcdmaSignalStrength + "\n");
//                Log.d(LogUtility.TAG(this), WcdmaRscpAsu);
//                logStr.append(GetTimestamp.getNowDate() + WcdmaRscpAsu + "\n");
                Log.d(LogUtility.TAG(this), WcdmaRscp);
                logStr.append(GetTimestamp.getNowDate() + WcdmaRscp + "\n");

                Log.d(LogUtility.TAG(this), "OS9" + Const.CSV_BREAK + signalStrength.toString());
                logStr.append(GetTimestamp.getNowDate() + "OS9" + Const.CSV_BREAK + signalStrength.toString() + "\n");


            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                signalStrength = tManager.getSignalStrength();
                String[] strSignalStrength = signalStrength.toString().split(",");
                String[] mWcdmaData = strSignalStrength[2].split(" ");
                String[] mLteData = strSignalStrength[4].split(" ");
                //5Gの電波強度
                String[] mNrData = strSignalStrength[5].split(" ");

                Log.d(LogUtility.TAG(this),  "3G:" + mWcdmaData[1].replace("=", Const.CSV_BREAK));
                logStr.append(GetTimestamp.getNowDate() + "3G:" + mWcdmaData[1].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this),  "3G:" + mWcdmaData[2].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() + "3G:" + mWcdmaData[2].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this), "3G:" + mWcdmaData[3].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() + "3G:" + mWcdmaData[3].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this), "3G:" + mWcdmaData[4].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() + "3G:" + mWcdmaData[4].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this), "3G:" + mWcdmaData[5].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() + "3G:" + mWcdmaData[5].replace("=",Const.CSV_BREAK) + "\n");

                Log.d(LogUtility.TAG(this), "4G:" + mLteData[1].replace("=",Const.CSV_BREAK));
                logStr.append(GetTimestamp.getNowDate() + "4G:" + mLteData[1].replace("=",Const.CSV_BREAK) + "\n");
                Log.d(LogUtility.TAG(this), "4G:" + mLteData[2].replace("=",Const.CSV_BREAK));
                logStr.append(GetTimestamp.getNowDate() + "4G:" + mLteData[2].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this), "4G:" + mLteData[3].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() + "4G:" + mLteData[3].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this), "4G:" + mLteData[4].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() + "4G:" + mLteData[4].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this),   "4G:" + mLteData[5].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() +  "4G:" + mLteData[5].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this),   "4G:" + mLteData[6].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() +  "4G:" + mLteData[6].replace("=",Const.CSV_BREAK) + "\n");
//                Log.d(LogUtility.TAG(this), "4G:" + mLteData[7].replace("=",Const.CSV_BREAK));
//                logStr.append(GetTimestamp.getNowDate() + "4G:" + mLteData[7].replace("=",Const.CSV_BREAK) + "\n");


                // 5Gの各電波強度
                // アイコンレベルはmCsiRsrpで判定　
                // 範囲[-115 <= -105 <= -95 ]
                Log.d(LogUtility.TAG(this), "5G:" + mNrData[1] + Const.CSV_BREAK + mNrData[3]);
                logStr.append(GetTimestamp.getNowDate() + "5G:" + mNrData[1] + Const.CSV_BREAK + mNrData[3] + "\n");
                Log.d(LogUtility.TAG(this), "5G:" + mNrData[4] + Const.CSV_BREAK + mNrData[6]);
                logStr.append(GetTimestamp.getNowDate() + "5G:" + mNrData[4] + Const.CSV_BREAK + mNrData[6] + "\n");
                Log.d(LogUtility.TAG(this), "5G:" + mNrData[7] + Const.CSV_BREAK + mNrData[9]);
                logStr.append(GetTimestamp.getNowDate() + "5G:" + mNrData[7] + Const.CSV_BREAK + mNrData[9] + "\n");
                Log.d(LogUtility.TAG(this), "5G:" + mNrData[10] + Const.CSV_BREAK + mNrData[12]);
                logStr.append(GetTimestamp.getNowDate() + "5G:" + mNrData[10] + Const.CSV_BREAK + mNrData[12] + "\n");
                Log.d(LogUtility.TAG(this), "5G:" + mNrData[13] + Const.CSV_BREAK + mNrData[15]);
                logStr.append(GetTimestamp.getNowDate() + "5G:" + mNrData[13] + Const.CSV_BREAK + mNrData[15] + "\n");
                Log.d(LogUtility.TAG(this), "5G:" + mNrData[16] + Const.CSV_BREAK + mNrData[18]);
                logStr.append(GetTimestamp.getNowDate() + "5G:" + mNrData[16] + Const.CSV_BREAK + mNrData[18] + "\n");
                Log.d(LogUtility.TAG(this), "5G:" + mNrData[19] + Const.CSV_BREAK + mNrData[21]);
                logStr.append(GetTimestamp.getNowDate() + "5G:" + mNrData[19] + Const.CSV_BREAK + mNrData[21] + "\n");


//                Log.d(LogUtility.TAG(this), "OS10" + Const.CSV_BREAK + signalStrength.toString());
//                logStr.append(GetTimestamp.getNowDate() + "OS10" + Const.CSV_BREAK + signalStrength.toString() + "\n");

                signalInfoList = signalStrength.getCellSignalStrengths();
                for (CellSignalStrength signalInfo : signalInfoList) {
//                    Log.d(LogUtility.TAG(this), "signalInfo:" + signalInfo.toString());
//                    logStr.append(GetTimestamp.getNowDate() + "signalInfo:" + signalInfo.toString() + "\n");
//                    String ssignal = signalInfo.toString();
//                    String[] parts = ssignal.split(" ");
//                    Log.d(LogUtility.TAG(this), "種別:" + parts[0]);
//                    logStr.append(GetTimestamp.getNowDate() + "種別:" + parts[0] + "\n");
//                    Log.d(LogUtility.TAG(this), "電力強度（RSSI）(-109...-53,2147483647):" + parts[1]);
//                    logStr.append(GetTimestamp.getNowDate() + "電力強度（RSSI）(-109...-53,2147483647):" + parts[1] + "\n");
//                    Log.d(LogUtility.TAG(this), "受信電力（RSRP）(-109...-53,2147483647):" + parts[2]);
//                    logStr.append(GetTimestamp.getNowDate() + "受信電力（RSRP）(-109...-53,2147483647):" + parts[2] + "\n");
//                    Log.d(LogUtility.TAG(this), "受信品質（RSRP）(-109...-53,2147483647):" + parts[3]);
//                    logStr.append(GetTimestamp.getNowDate() + "受信品質（RSRP）(-109...-53,2147483647):" + parts[3] + "\n");
                }
            }

        } catch (Exception e) {
            Log.d("SignalStrength", "+++++++++++++++++++++++++++++++ null array spot 3: " + e);
        }
    }
}
