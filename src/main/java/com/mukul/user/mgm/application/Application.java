package com.mukul.user.mgm.application;

import com.mukul.user.mgm.application.config.H2WebConsoleConfiguration;
import com.mukul.user.mgm.application.config.JpaConfig;
import com.mukul.user.mgm.application.model.User;
import com.mukul.user.mgm.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;


    public static void main(String[] args) {
        SpringApplication.run(new Class<?>[]{Application.class, JpaConfig.class, H2WebConsoleConfiguration.class}, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<User> initialUsers = Arrays.asList(
                new User("user", "user", "user"),
                new User("Jinny", "jinny2016", "user"),
                new User("Jill", "jillisthebest", "user"),
                new User("James", "workharddiejong", "user"),
                new User("admin", "admin", "admin")
        );
        userRepository.save(initialUsers);
    }
}
