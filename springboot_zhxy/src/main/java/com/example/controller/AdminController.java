package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.pojo.Admin;
import com.example.service.AdminService;
import com.example.util.MD5;
import com.example.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/getAllAdmin/{pageNum}/{pageSize}")
    public Result getAllAdmin(
            @PathVariable Integer pageNum,
            @PathVariable Integer pageSize,
            String adminName
    ){
        Page<Admin> page = new Page<>(pageNum,pageSize);
        Page<Admin> pages = adminService.getAdmins(page, adminName);
        return Result.ok(pages);
    }

    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
        String password = admin.getPassword();
        admin.setPassword(MD5.encrypt(password));
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids){
        adminService.removeByIds(ids);
        return Result.ok();
    }

}
