package com.camrent.service;

import com.camrent.entity.User;
import com.camrent.entity.Staff;

public interface AuthService {
    User authenticate(String email, String password);
    Staff registerStaff(Staff staff);
}
