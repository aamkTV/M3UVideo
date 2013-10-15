package com.dkc.m3uvideo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by barbarian on 15.10.13.
 */
public class M3UParser {
    public ArrayList<VideoStream> parse(String path) {
        ArrayList<VideoStream> channels=null;
        boolean m3u=false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while (!m3u && (line = br.readLine()) != null) {
                line = line.trim();
                if(startsWithBom(line)){
                    line=line.substring(1);
                }

                if (line.toLowerCase().startsWith("#extm3u")) {
                    m3u = true;
                    channels= new ArrayList<VideoStream>();
                }
            }
            String detailsLine="";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (m3u) {
                    if (line.toLowerCase().startsWith("#extinf:")&&line.length()>8) {
                        detailsLine=line.substring(8).trim();
                    } else if (!line.startsWith("#")&&line.trim().length()>5) {
                        VideoStream entry = new VideoStream();
                        entry.setPath(line);
                        HashMap<String,String> options = parseOptions(detailsLine);
                        if(options.containsKey("title")){
                            entry.setTitle(options.get("title"));
                        }
                        if(options.containsKey("logo")){
                            entry.setLogoUrl(options.get("logo"));
                        }

                        channels.add(entry);
                        detailsLine = "";
                    }
                }
            }
            br.close();
        }
        catch (Exception ex){
            return null;
        }
        return channels;
    }

    private HashMap<String,String> parseOptions(String line){
        HashMap<String,String> res= new HashMap<String, String>();

        String[] info = line.split("[,]");
        if( info.length >= 2 ){
            String details=info[0].trim();
            res.put("title",info[1].trim());

            StringBuilder sbKey=new StringBuilder();
            StringBuilder sbValue=new StringBuilder();
            boolean isValue=false;
            boolean isQT=false;
            for(int i=0;i<details.length();i++){
                char ch=details.charAt(i);
                if(ch==' '&&!isQT){
                    if(isValue){
                        isValue=false;
                        if(sbKey.length()>0&sbValue.length()>0){
                            res.put(sbKey.toString(),sbValue.toString());
                        }
                    }
                    sbKey= new StringBuilder();
                    sbValue= new StringBuilder();
                }
                else if(ch=='"') {
                    isQT=!isQT;
                }
                else if(ch=='=') {
                    isValue=true;
                }
                else{
                    if(isValue){
                        sbValue.append(ch);
                    }
                    else{
                        sbKey.append(ch);
                    }
                }
            }

            if(isValue){
                if(sbKey.length()>0&sbValue.length()>0){
                    res.put(sbKey.toString(),sbValue.toString());
                }
            }
        }

        return res;
    }

    private boolean startsWithBom(String line) {
        char myChar = line.charAt(0);
        int intValue = (int) myChar;
        // Hexa value of BOM = EF BB BF  => int 65279
        if (intValue == 65279) {
            System.out.println("this file starts with a BOM");
            return true;
        } else {
            System.out.println("file starts with " + intValue);
            return false;
        }
    }
}
