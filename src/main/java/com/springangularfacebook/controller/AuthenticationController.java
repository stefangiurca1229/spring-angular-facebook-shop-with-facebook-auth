package com.springangularfacebook.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
 @GetMapping("/uuid")
    public void getUuid(
            @RequestParam String uuid
 ){
  System.out.println("uuid= "+uuid);
 }
 @GetMapping("/callback")
 public void test(
         @RequestParam String code,
         @RequestParam String state
 ){
System.out.println("code: "  + code);
System.out.println("state: " + state);
 }

 @GetMapping("test")
    public String test(){
     return "test";
 }

}