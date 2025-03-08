package com.frankit.task.config;

import com.frankit.task.entity.User;
import com.frankit.task.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    @Transactional // 트랜잭션 적용
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("example@example.com").isEmpty()) {
                User exampleUser = new User();
                exampleUser.setEmail("example@example.com");
                exampleUser.setPassword(passwordEncoder.encode("example123")); // 비밀번호 암호화
                userRepository.save(exampleUser);
                logger.info("✅ 초기 계정 추가됨: example@example.com / example123");
            }
        };
    }
}
