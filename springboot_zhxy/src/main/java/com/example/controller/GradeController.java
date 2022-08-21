package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.Grade;
import com.example.service.GradeService;
import com.example.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping("/getGrades/{pageNum}/{size}")
    public Result getGrade(@PathVariable("pageNum") Integer pageNum,@PathVariable("size") Integer size,String gradeName){
        Page<Grade> page = new Page<>(pageNum,size);
        Page<Grade> gradePage = gradeService.getGradePage(page,gradeName);
        return Result.ok(gradePage);
    }

    @GetMapping("/getGrades")
    public Result getGrade(){
        List<Grade> list = gradeService.list();
        return Result.ok(list);
    }

    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@RequestBody Grade grade){
        boolean b = gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@RequestBody List<Integer> list){
        gradeService.removeByIds(list);
        return Result.ok();

    }
}
