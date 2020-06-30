package com.technomedialab.battelysurvey;

public class ExplorerItem
{
    /**ファイルの名前*/
    public String name;
    /**ファイル絶対パス*/
    public String path;
    /**ファイルの最終更新日時*/
    public String date;

    public ExplorerItem(String name, String path, String date)
    {
        this.name = name;
        this.path = path;
        this.date = date;
    }
}