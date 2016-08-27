package gr.uoa.di.mapper;

import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.user.UserRegisterDto;

import java.util.Random;

public class UserMapper {
    public UserEntity mapUserRegisterDtoToUserEntity(UserRegisterDto dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getTelephone());
        user.setLocation(dto.getAddress());
        user.setCountry(dto.getCountry());
        user.setLat(dto.getLatitude());
        user.setLon(dto.getLongitude());
        user.setAfm(dto.getAfm());
        return user;
    }
}
