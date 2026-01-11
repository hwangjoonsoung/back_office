package org.cric.back_office.user.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.cric.back_office.user.dto.UserEditDto;
import org.cric.back_office.user.dto.UserRegistDto;
import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.enums.UserStatus;
import org.cric.back_office.user.repository.UserJpaRepository;
import org.cric.back_office.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public Integer saveUser(UserRegistDto userRegistDto) {
        User user = User.createUser(userRegistDto);

        User save = userJpaRepository.save(user);
        return save.getId();
    }

    @Transactional
    public void editUser(Long id, UserEditDto userEditDto) {
        //ai todo: user repository에서 id로 user의 정보 가져오기 
        User user = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        //ai todo: 가져온 정보를 UserEditDto를 통해 정보 update (user entity에 함수 있음)
        user.settingEditUser(user, userEditDto);
        userJpaRepository.save(user);
    }

    @Transactional
    public void User(Long id) {
        //ai todo: user repository에서 id로 user의 정보 가져오기
        User user = userJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        //ai todo: 가져온 정보에서 userstatus를 DELETED로 변경
        user.setUserStatus(UserStatus.DELETED);
        userJpaRepository.save(user);
    }

}