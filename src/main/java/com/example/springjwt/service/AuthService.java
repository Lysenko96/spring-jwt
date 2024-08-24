package com.example.springjwt.service;

import com.example.springjwt.dto.JwtRequest;
import com.example.springjwt.dto.JwtResponse;
import com.example.springjwt.dto.RegistrationUserDto;
import com.example.springjwt.exception.AppError;
import com.example.springjwt.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<?> createAuthToken(JwtRequest jwtRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        return ResponseEntity.ok(new JwtResponse(jwtTokenUtil.generateToken(userDetails)));
    }
    
    public ResponseEntity<?> createNewUser(RegistrationUserDto userDto) {
        if(!userDto.getPassword().equals(userDto.getConfirmPassword())) return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Password not confirmed"), HttpStatus.BAD_REQUEST);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(userService.findByUsername(userDto.getUsername()) != null) return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "User is created"), HttpStatus.BAD_REQUEST);
        userService.createNewUser(userDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
