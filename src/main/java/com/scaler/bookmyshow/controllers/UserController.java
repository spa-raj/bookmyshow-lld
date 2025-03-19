package com.scaler.bookmyshow.controllers;

import com.scaler.bookmyshow.dtos.SignupRequestDto;
import com.scaler.bookmyshow.dtos.SignupResponseDto;
import com.scaler.bookmyshow.dtos.SignupResponseStatus;
import com.scaler.bookmyshow.models.User;
import com.scaler.bookmyshow.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class UserController {
    private UserService userService;
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        // Logic to handle user signup
        SignupResponseDto response = new SignupResponseDto();
        try {
            User user=userService.signup(
                    signupRequestDto.getEmail(),
                    signupRequestDto.getPassword()
            );
            response.setUserId(user.getId());
            response.setStatus(SignupResponseStatus.SUCCESS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(SignupResponseStatus.FAILED);
        }

        return response;
    }
    public SignupResponseDto login(SignupRequestDto signupRequestDto) {
        // Logic to handle user login
        SignupResponseDto response = new SignupResponseDto();
        try {
            User user=userService.login(
                    signupRequestDto.getEmail(),
                    signupRequestDto.getPassword()
            );
            response.setUserId(user.getId());
            response.setStatus(SignupResponseStatus.SUCCESS);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(SignupResponseStatus.FAILED);
        }

        return response;
    }
}
