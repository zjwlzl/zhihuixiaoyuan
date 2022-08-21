package com.example.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.Admin;
import com.example.pojo.LoginForm;


public interface AdminService extends IService<Admin> {
    Admin login(LoginForm loginForm);

    Page<Admin> getAdmins(Page<Admin> page, String adminName);

}
