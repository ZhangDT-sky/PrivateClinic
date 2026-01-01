package org.code.privateclinic.service;

import org.code.privateclinic.bean.LoginRequestDTO;

public interface AuthService {
    String login(LoginRequestDTO loginRequestDTO);
}
