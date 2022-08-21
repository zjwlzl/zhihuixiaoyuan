package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.pojo.Admin;
import com.example.pojo.LoginForm;
import com.example.pojo.Student;
import com.example.pojo.Teacher;
import com.example.service.AdminService;
import com.example.service.StudentService;
import com.example.service.TeacherService;
import com.example.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    AdminService adminService;
    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;

    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        String code = new String(CreateVerifiCodeImage.getVerifiCode());
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",code);
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute("verifiCode");
        String verifiCode = loginForm.getVerifiCode();

        if ("".equals(code) || null==code){
            return Result.fail().message("验证码失效，请刷新");
        }
        if (!verifiCode.equalsIgnoreCase(code)){
            return Result.fail().message("验证码错误，请重新输入");
        }
        session.removeAttribute("code");
        Map<String,Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null!=admin){
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null!=student){
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null!=teacher){
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("无此用户");

    }

    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String,Object> map = new LinkedHashMap<>();
        switch (userType) {
            case 1 -> {
                Admin admin = adminService.getById(userId);
                map.put("userType", 1);
                map.put("user", admin);
            }
            case 2 -> {
                Student student = studentService.getById(userId);
                map.put("userType", 2);
                map.put("user", student);
            }
            case 3 -> {
                Teacher teacher = teacherService.getById(userId);
                map.put("userType", 2);
                map.put("user", teacher);
            }
        }
        return Result.ok(map);
    }

    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(@RequestPart("multipartFile")MultipartFile multipartFile){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();

        int i = originalFilename.lastIndexOf(".");

        String newFileName = uuid + originalFilename.substring(i);
        String portraitPath="D:/Coding/javacode/SpringBoot/springboot_zhxy/target/classes/public/upload/".concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String headerImg ="upload/"+newFileName;
        return Result.ok(headerImg);
    }

    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@RequestHeader("token") String token,
                            @PathVariable("oldPwd") String oldPwd,
                            @PathVariable("newPwd") String newPwd){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd=MD5.encrypt(oldPwd);
        newPwd= MD5.encrypt(newPwd);
        switch (userType){
            case 1->{
                QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("id",userId.intValue()).eq("password",oldPwd);
                Admin admin = adminService.getOne(queryWrapper);
                if (null!=admin) {
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else{
                    return Result.fail().message("原密码输入有误！");
                }
            }

            case 2->{
                QueryWrapper<Student> queryWrapper1=new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue()).eq("password",oldPwd);
                Student student = studentService.getOne(queryWrapper1);
                if (null!=student) {
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else{
                    return Result.fail().message("原密码输入有误！");
                }
            }

            case 3->{
                QueryWrapper<Teacher> queryWrapper2=new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue()).eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper2);
                if (null!=teacher) {
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else{
                    return Result.fail().message("原密码输入有误！");
                }
            }
        }
        return Result.ok();
    }
}
