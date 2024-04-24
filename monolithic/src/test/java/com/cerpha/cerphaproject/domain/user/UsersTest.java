package com.cerpha.cerphaproject.domain.user;

import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UsersTest {

    @DisplayName("사용자의 주소, 전화번호를 변경한다.")
    @Test
    public void updateProfile() {
        // given
        Users user = createUser();

        // when
        String updateAddress = "서울시";
        String updatePhone = "01077779999";
        user.updateProfile(updateAddress, updatePhone);

        // then
        Assertions.assertThat(user.getAddress()).isEqualTo(updateAddress);
        Assertions.assertThat(user.getPhone()).isEqualTo(updatePhone);
    }

    @DisplayName("사용자의 비밀번호를 변경한다.")
    @Test
    public void changePassword() {
        // given
        Users user = createUser();

        // when
        String newPassword = "newpassword";
        user.changePassword(newPassword);

        // then
        Assertions.assertThat(user.getPassword()).isEqualTo(newPassword);
    }

    private static Users createUser() {
        Users user = Users.builder()
                .email("ghdb132@naver.com")
                .password("132465aa")
                .name("hobin")
                .nickname("ghgh")
                .phone("01012345678")
                .address("인천시")
                .role(UserRole.USER)
                .build();
        return user;
    }


}
