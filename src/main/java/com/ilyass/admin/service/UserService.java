package com.ilyass.admin.service;

import com.ilyass.admin.domain.User;
import com.ilyass.admin.exception.domain.UsernameExistException;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    User register(String firstname, String lastname, String username, String email) throws UsernameExistException, MessagingException;

    List<User> getUsers();

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    User addNewUser(String firstName , String lastName , String username, String email , String role , boolean isNonLocked , boolean isActive , MultipartFile profileImage) throws UsernameExistException;

    User updateUser(String currentUsername ,String newFirstName , String newLastName , String newUsername , String newEmail , String role , boolean isNonLocked , boolean isActive , MultipartFile profileImage) throws UsernameExistException;

    void deleteUser(long id);

    void resetPassword(String email);

    User updateProfileImage(String username , MultipartFile profileImage);
}
