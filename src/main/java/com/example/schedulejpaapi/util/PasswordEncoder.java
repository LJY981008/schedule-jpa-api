package com.example.schedulejpaapi.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

/**
 * 비밀번호 암호화 및 비교 유틸리티
 */
@Component
public class PasswordEncoder {

    /**
     * 평문 비밀번호를 BCrypt 해싱을 통해 암호화
     *
     * @param password 암호화할 평문 비밀번호
     * @return BCrypt 해싱된 비밀번호
     */
    public String encode(String password) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
    }

    /**
     * 평문 비밀번호가 BCrypt 해싱된 비밀번호와 일치하는지 비교
     *
     * @param password        평문 비밀번호
     * @param encodedPassword 이전에 해싱한 비밀번호
     * @return 일치하면 {@code true}, 틀리면 {@code false}
     */
    public boolean matches(String password, String encodedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), encodedPassword).verified;
    }
}