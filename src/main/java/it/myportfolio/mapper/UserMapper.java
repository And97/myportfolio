package it.myportfolio.mapper;

import org.springframework.beans.BeanUtils;

import it.myportfolio.dto.UserDTO;
import it.myportfolio.model.User;

public class UserMapper {

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }
}