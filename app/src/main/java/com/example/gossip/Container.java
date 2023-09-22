package com.example.gossip;

import android.graphics.Bitmap;

import com.example.gossip.Status.StatusModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Container {
    public static boolean redirect=false;

    public static HashMap<String,ArrayList<String>> statusMap=new HashMap<>();
    public static String lastUser="";
    private static  Bitmap myImage;
    private static boolean SuggestionOrRequest;
    public static boolean Stop;
    public static String Mobile;
    public static List<String> myTags=new ArrayList<>();
    public static List<StatusModel> statusList;

    public static String myName;
    public static String ChatRoomName;
//    public Container(List<StatusModel> statusList){
//        Container.statusList =statusList;
//    }


    public static boolean isSuggestionOrRequest() {
        return SuggestionOrRequest;
    }

    public static void setSuggestionOrRequest(boolean suggestionOrRequest) {
        SuggestionOrRequest = suggestionOrRequest;
    }

    public static Bitmap getMyImage() {
        return myImage;
    }

    public static String getMyName() {
        return myName;
    }

    public static void setMyName(String myName) {
        Container.myName = myName;
    }

    public static void setMyImage(Bitmap myImage) {
        Container.myImage = myImage;
    }

    public static boolean isStop() {
        return Stop;
    }

    public static void setStop(boolean stop) {
        Stop = stop;
    }

    public static List<String> getMyTags() {
        return myTags;
    }

    public static void setMyTags(List<String> myTags) {
        Container.myTags = myTags;
    }

    public static String getMobile() {
        return Mobile;
    }

    public static void setMobile(String mobile) {
        Mobile = mobile;
    }

    public List<StatusModel> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<StatusModel> statusList) {
        Container.statusList = statusList;
    }

}
