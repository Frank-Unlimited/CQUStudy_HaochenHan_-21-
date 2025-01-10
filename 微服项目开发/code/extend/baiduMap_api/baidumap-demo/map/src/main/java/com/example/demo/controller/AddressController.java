package com.example.demo.controller;

import com.example.demo.entity.Address;
import com.example.demo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @PostMapping("/save")
    public String saveAddress(@RequestBody Address address) {
        addressRepository.save(address); // 保存到数据库
        return "地址保存成功！";
    }

    // 查询所有地址的接口
    @GetMapping("/all")
    public List<Address> getAllAddresses() {
        return addressRepository.findAll(); // 返回所有存储的地址
    }
}

