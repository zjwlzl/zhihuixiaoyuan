package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.LoginForm;
import com.example.pojo.Student;

public interface StudentService extends IService<Student> {
    Student login(LoginForm loginForm);

    Page<Student> getStudentPage(Page<Student> page, String clazzName, String name);
}
