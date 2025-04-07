package com.cst323.eventsapp.data;
 
import java.util.List;

import com.cst323.eventsapp.models.UserEntity;

public interface UserRepositoryInterface {
    UserEntity findByLoginName(String loginName);
    UserEntity save(UserEntity user);
}
