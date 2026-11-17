package com.digitalwallet.digitalwallet.service;

import com.digitalwallet.digitalwallet.model.User;
import com.digitalwallet.digitalwallet.model.Wallet;
import com.digitalwallet.digitalwallet.repository.UserRepository;
import com.digitalwallet.digitalwallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public User registerUser(User user){

        if(userRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("Email already registered");
        }

        if(userRepository.existsByMobileNumber(user.getMobileNumber())){
            throw new IllegalArgumentException("Mobile number already registered");
        }
        //Hash the password before saving
        user.setPasswordHash(
                passwordEncoder.encode(user.getPasswordHash())
        );

        User saveUser=userRepository.save(user);
        //Automatically create a wallet for the registered user
        Wallet wallet=new Wallet(saveUser);
        walletRepository.save(wallet);

        return saveUser;
    }
}

