package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.StudentMapper;
import com.example.pojo.Admin;
import com.example.pojo.Clazz;
import com.example.pojo.LoginForm;
import com.example.pojo.Student;
import com.example.service.StudentService;
import com.example.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Autowired
    StudentMapper studentMapper;
    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        Student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Page<Student> getStudentPage(Page<Student> page, String clazzName, String name) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(clazzName)){
            queryWrapper.like("clazz_name",clazzName);
        }
        if (!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);
        }
        Page<Student> studentPage = studentMapper.selectPage(page, queryWrapper);

        return studentPage;
    }
}
