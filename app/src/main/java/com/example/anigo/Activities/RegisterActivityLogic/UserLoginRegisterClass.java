package com.example.anigo.Activities.RegisterActivityLogic;

public class UserLoginRegisterClass {

    public String Name;
    public String Password;

    public String Email;



    public int RoleId;

    public UserLoginRegisterClass(String name, String password, String email, int roleId) {
        Name = name;
        Password = password;
        Email = email;
        RoleId = roleId;
    }
}
