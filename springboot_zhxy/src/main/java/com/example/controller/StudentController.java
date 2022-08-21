package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.Clazz;
import com.example.pojo.Student;
import com.example.service.StudentService;
import com.example.util.MD5;
import com.example.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/getStudentByOpr/{pageNum}/{pageSize}")
    public Result getStudentByOpr(
            @PathVariable("pageNum") Integer pageNum,
            @PathVariable("pageSize") Integer pageSize,
            String name,
            String clazzName){
        Page<Student> page = new Page<>(pageNum,pageSize);
        Page<Student> studentPage = studentService.getStudentPage(page, clazzName, name);
        return Result.ok(studentPage);
    }

    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student){
        String password = student.getPassword();
        String s = MD5.encrypt(password);
        student.setPassword(s);
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @DeleteMapping("/delStudentById")
    public Result delStudentById(@RequestBody List<Integer> list){
        studentService.removeByIds(list);
        return Result.ok();
    }
}
