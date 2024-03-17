package com.example.api.service;

import com.example.api.domain.LoginLog;
import com.example.api.domain.LoginLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginLogService {
    private final LoginLogRepository repository;

    @Transactional
    public void loginSucceeded(String username, String clientIp) {
        repository.save(
                LoginLog
                        .builder()
                        .username(username)
                        .lastLoginSuccessTime((new Date()).getTime())
                        .loginFailCount(0L)
                        .clientIp(clientIp)
                        .build()
        );
    }

    @Transactional
    public void loginFailed(String username, String clientIp) {
        LoginLog loginLog = getLastLoginLog(username);

        repository.save(
                LoginLog
                        .builder()
                        .username(username)
                        .lastLoginFailTime((new Date()).getTime())
                        .loginFailCount(loginLog.getLoginFailCount() + 1L)
                        .clientIp(clientIp)
                        .build()
        );
    }

    @Transactional
    public boolean isBlocked(String username) {
        LoginLog loginLog = getLastLoginLog(username);

        if (loginLog.getLastLoginFailTime() == null) {
            return false;
        }

        long now = (new Date()).getTime();
        int DELAY_MILLISECOND = 1000;
        return now < (loginLog.getLastLoginFailTime() + (Math.pow(2, loginLog.getLoginFailCount()) * DELAY_MILLISECOND));
    }

    @Transactional
    public LoginLog getLastLoginLog(String username) {
        Optional<LoginLog> lastLoginLog = repository.findTop1ByUsernameOrderByIdDesc(username);
        if (lastLoginLog.isEmpty()) {
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setLoginFailCount(0L);
            return loginLog;
        }

        return lastLoginLog.get();
    }
}
