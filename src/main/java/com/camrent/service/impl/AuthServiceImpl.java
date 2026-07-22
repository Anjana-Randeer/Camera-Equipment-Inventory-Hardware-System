package com.camrent.service.impl;

import com.camrent.entity.Staff;
import com.camrent.entity.User;
import com.camrent.entity.Role;
import com.camrent.repository.UserRepository;
import com.camrent.repository.StaffRepository;
import com.camrent.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;

    @Override
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    @Override
    public Staff registerStaff(Staff staff) {
        staff.setRole(Role.STAFF);
        return staffRepository.save(staff);
    }
}
