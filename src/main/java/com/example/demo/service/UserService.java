package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public User getUserByIdAndRole(Long id, UserRole role) {
        User user = getUserById(id);
        if (user.getRole() != role) {
            throw new BusinessRuleException("User with id " + id + " is not a " + role.name());
        }
        return user;
    }

    public User getInsurer(Long id) {
        return getUserByIdAndRole(id, UserRole.INSURER);
    }

    public User getPartsShop(Long id) {
        return getUserByIdAndRole(id, UserRole.PARTS_SHOP);
    }

    public User getExaminer(Long id) {
        return getUserByIdAndRole(id, UserRole.EXAMINER);
    }

    public List<User> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public User createUser(String username, UserRole role, String companyName) {
        if (userRepository.existsByUsername(username)) {
            throw new BusinessRuleException("Username already exists: " + username);
        }
        User user = new User(username, role, companyName);
        return userRepository.save(user);
    }
}
