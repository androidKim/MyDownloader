package com.midas.mydownloader.structure.core;

/**
 * Created by taejun on 2018. 6. 21..
 */

public class content
{
    /*********************** Define ***********************/
    //다운로드상태..
    public static String TYPE_DOWN_BEFORE = "B";//다운로드전
    public static String TYPE_DOWN_ING = "I";//다운로드중
    public static String TYPE_DOWN_COMPLETE = "C";//다운로드완료

    //저장위치
    public static final String SAVE_TYPE_INTERNAL = "I";//저장 위치 내장
    public static final String SAVE_TYPE_EXTERNAL = "E";//저장 위치 외장
    /*********************** Member ***********************/

    public String title = null;//타이틀
    public String url = null;//컨텐츠 url
    public String save_location = null;
    //----------------------------------------------
    //
    public content(String title, String url)
    {
        this.title = title;
        this.url = url;

    }
}
