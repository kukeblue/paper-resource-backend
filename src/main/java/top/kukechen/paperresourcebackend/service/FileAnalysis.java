package top.kukechen.paperresourcebackend.service;
import  org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import top.kukechen.paperresourcebackend.model.Grade;
import top.kukechen.paperresourcebackend.model.GradeStep;

import  java.util.HashMap;
import java.util.Map;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;

@Service
public class FileAnalysis {

    HashMap<String, GradeStep> gradeStepCacheMap = new HashMap<>();

    public GradeStep analysisGradeStep(String s) {
        GradeStep gradeStep = null;
        String regex = "[\\u4e00-\\u9fa5]年级";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if(matcher.find()){
            String target = matcher.group(0);
            gradeStep = gradeStepCacheMap.get(target);
            if(gradeStep == null) {
                Map map = new HashMap();
                map.put("name", matcher.group(0));
                gradeStep = (GradeStep) MongoDBUtil.findOneByParam(GradeStep.class, map);
            }
            if(gradeStep != null) {
                gradeStepCacheMap.put(gradeStep.getName(), gradeStep);
            }
        }
        return gradeStep;
    }
}
