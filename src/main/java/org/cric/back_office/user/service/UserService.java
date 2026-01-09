package org.cric.back_office.user.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Setter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void saveUser(UserRegistDto userRegistDto) {
        //user repository에 저장
    }

    @Transactional
    public void editUser(Long id,UserEditDto userEditDto) {
        //user repository에서 id로 user의 정보 가져오기

        //가져온 정보를 UserEditDto를 통해 정보 update
        
    }

}