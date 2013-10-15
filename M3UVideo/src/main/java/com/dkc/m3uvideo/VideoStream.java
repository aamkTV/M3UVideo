package com.dkc.m3uvideo;

import java.util.HashMap;

/**
 * Created by barbarian on 15.10.13.
 */
public class VideoStream {
    private String title="";
    private String path = "";
    private String logoUrl = "";
    private String mime_type="video/*";
    private HashMap<String,String> options= new HashMap<String, String>();

    public VideoStream(){

    }

    private HashMap<String,String> details;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
    }

    public HashMap<String, String> getDetails() {
        return details;
    }

    public void setDetails(HashMap<String, String> details) {
        this.details = details;
    }
}
