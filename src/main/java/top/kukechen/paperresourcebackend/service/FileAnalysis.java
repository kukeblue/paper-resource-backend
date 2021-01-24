package top.kukechen.paperresourcebackend.service;
import  org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import top.kukechen.paperresourcebackend.model.Grade;
import top.kukechen.paperresourcebackend.model.GradeStep;
import top.kukechen.paperresourcebackend.model.Paper;
import top.kukechen.paperresourcebackend.model.Subject;

import java.util.*;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;

@Service
public class FileAnalysis {

    HashMap<String, GradeStep> gradeStepCacheMap = new HashMap<>();
    HashMap<String, Subject> subjectCacheMap = new HashMap<>();

    public void analysis(Paper paper) {
        this.analysisGradeStep(paper);
        this.analysisSubject(paper);
        this.analysisTerm(paper);
//        this.analysisResolution(paper);
        this.analysisYear(paper);
    }
    // 分析年级
    public void analysisGradeStep(Paper paper) {
        String s = paper.getName();
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
        if(gradeStep != null) {
            paper.setGradeStepId(gradeStep.getId());
            paper.setGradeId(gradeStep.getGradeId());
        }
    }
    // 分析科目
    public void analysisSubject(Paper paper) {
        String s = paper.getName();
        Subject subject = null;
        String regex = "(语文|数学|英语)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if(matcher.find()){
            String target = matcher.group(0);
            subject = subjectCacheMap.get(target);
            if(subject == null) {
                Map map = new HashMap();
                map.put("name", matcher.group(0));
                subject = (Subject) MongoDBUtil.findOneByParam(Subject.class, map);
            }
            if(subject != null) {
                subjectCacheMap.put(subject.getName(), subject);
            }
        }
        if(subject != null) {
            paper.setSubjectId(subject.getId());
        }
    }
    // 分析学年
    public void analysisTerm(Paper paper) {
        String s = paper.getName();
        Subject subject = null;
        String regex = "(上学期|下学期|上册|下册|上|下)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        if(matcher.find()){
            String target = matcher.group(0);
            if(target.indexOf("上") > -1) {
                paper.setTerm("UP");
            }else {
                paper.setTerm("DOWN");
            }
        }
        if(subject != null) {
            paper.setSubjectId(subject.getId());
        }
    }
    // 分析地域
    public static void analysisResolution(Paper paper){
        String regex="(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇)?(?<village>.*)";
        String address = paper.getName();
        Matcher m=Pattern.compile(regex).matcher(address);
        String province=null,city=null,county=null,town=null,village=null;
        ArrayList region = new ArrayList<String>();
        if(m.find()){
            province=m.group("province");
            if(province != null) {
                region.add(province.trim());
            }
            city=m.group("city");
            if(city != null) {
                region.add(city.trim());
            }
            county=m.group("county");
            if(county != null) {
                region.add(county.trim());
            }
            town=m.group("town");
            if(town != null) {
                region.add(town.trim());
            }
            village=m.group("village");
            if(village != null) {
                region.add(village.trim());
            }
        }
        paper.setRegion(region);
    }

    // 年份分析器
    public static void analysisYear(Paper paper){
        String str = paper.getName();
        String year = "";
        String reg = "\\D[1-4]\\d{3}\\D";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(str);
        while(matcher.find()){
            year = year + matcher.group(0).substring(1,5) + " ";
        }
        paper.setYear(year.trim());
    }
}
