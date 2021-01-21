package top.kukechen.paperresourcebackend.service;
import  org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import  java.util.HashMap;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;

@Service
public class FileAnalysis {

    HashMap<String, String> GradeAMap = new HashMap<>();

    public void analysis(String name) {
        String s = "江西省三年级";
        String regex = "[\\u4e00-\\u9fa5]年级$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if(matcher.find()){
            System.out.println(matcher.group(0));
        }
    }
}
