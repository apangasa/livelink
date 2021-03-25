package com.nitinr.livelink;

public class SttService {

    public static SttService SttService_instance = null;

    private SttService (){

    };

    public static SttService getInstance() {
        return SttService_instance;
    }

    
}
