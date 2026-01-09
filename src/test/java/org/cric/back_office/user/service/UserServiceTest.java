package org.cric.back_office.user.service;

import org.cric.back_office.user.dto.UserEditDto;
import org.cric.back_office.user.dto.UserRegistDto;
import org.cric.back_office.user.entity.User;
import org.cric.back_office.user.enums.UserStatus;
import org.cric.back_office.user.repository.UserJpaRepository;
import org.cric.back_office.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private UserService userService;

    private UserRegistDto userRegistDto;
    private UserEditDto userEditDto;
    private User testUser;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        testUserId = 1L;

        // UserRegistDto 설정
        userRegistDto = new UserRegistDto();
        userRegistDto.setEmail("test@example.com");
        userRegistDto.setPassword("password123");
        userRegistDto.setName("테스트 사용자");
        userRegistDto.setAffiliation("테스트 조직");
        userRegistDto.setPosition("테스트 직책");
        userRegistDto.setPhoneNumber("010-1234-5678");
        userRegistDto.setBirthday(LocalDate.of(1990, 1, 1));

        // UserEditDto 설정
        userEditDto = new UserEditDto();
        userEditDto.setName("수정된 이름");
        userEditDto.setAffiliation("수정된 조직");
        userEditDto.setPosition("수정된 직책");
        userEditDto.setPhoneNumber("010-9999-9999");
        userEditDto.setBirthday(LocalDate.of(1995, 5, 5));

        // 테스트용 User 엔티티 설정
        testUser = User.createUser(userRegistDto);
    }

    @Test
    @DisplayName("saveUser - 정상 케이스: User를 성공적으로 저장한다")
    void saveUser_Success() {
        // given
        when(userJpaRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            return savedUser;
        });

        // when & then
        assertThatCode(() -> userService.saveUser(userRegistDto))
                .doesNotThrowAnyException();
        verify(userJpaRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("editUser - 정상 케이스: User 정보를 성공적으로 수정한다")
    void editUser_Success() {
        // given
        when(userJpaRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userJpaRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when & then
        assertThatCode(() -> userService.editUser(testUserId, userEditDto))
                .doesNotThrowAnyException();
        verify(userJpaRepository, times(1)).findById(testUserId);
        verify(userJpaRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("editUser - 예외 케이스: 존재하지 않는 User ID로 수정 시 IllegalArgumentException을 발생시킨다")
    void editUser_UserNotFound_ThrowsIllegalArgumentException() {
        // given
        Long nonExistentUserId = 999L;
        when(userJpaRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.editUser(nonExistentUserId, userEditDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with id: " + nonExistentUserId);
        verify(userJpaRepository, times(1)).findById(nonExistentUserId);
        verify(userJpaRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("User (삭제) - 정상 케이스: User 상태를 DELETED로 성공적으로 변경한다")
    void deleteUser_Success() {
        // given
        when(userJpaRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userJpaRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when & then
        assertThatCode(() -> userService.User(testUserId))
                .doesNotThrowAnyException();
        verify(userJpaRepository, times(1)).findById(testUserId);
        verify(userJpaRepository, times(1)).save(any(User.class));
        assertThat(testUser.getUserStatus()).isEqualTo(UserStatus.DELETED);
    }

    @Test
    @DisplayName("User (삭제) - 예외 케이스: 존재하지 않는 User ID로 삭제 시 IllegalArgumentException을 발생시킨다")
    void deleteUser_UserNotFound_ThrowsIllegalArgumentException() {
        // given
        Long nonExistentUserId = 999L;
        when(userJpaRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.User(nonExistentUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found with id: " + nonExistentUserId);
        verify(userJpaRepository, times(1)).findById(nonExistentUserId);
        verify(userJpaRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("editUser - 수정된 정보가 정확히 반영되는지 확인")
    void editUser_VerifyUpdatedFields() {
        // given
        when(userJpaRepository.findById(testUserId)).thenReturn(Optional.of(testUser));
        when(userJpaRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        userService.editUser(testUserId, userEditDto);

        // then
        assertThat(testUser.getName()).isEqualTo(userEditDto.getName());
        assertThat(testUser.getAffiliation()).isEqualTo(userEditDto.getAffiliation());
        assertThat(testUser.getPosition()).isEqualTo(userEditDto.getPosition());
        assertThat(testUser.getPhoneNumber()).isEqualTo(userEditDto.getPhoneNumber());
        assertThat(testUser.getBirthday()).isEqualTo(userEditDto.getBirthday());
    }
}
