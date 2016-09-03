package gr.uoa.di.mapper;

import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.user.UserRegisterDto;
import gr.uoa.di.dto.user.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity mapUserRegisterDtoToUserEntity(UserRegisterDto dto) {
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getTelephone());
        user.setLocation(dto.getLocation());
        user.setCountry(dto.getCountry());
        user.setLat(dto.getLatitude());
        user.setLon(dto.getLongitude());
        user.setAfm(dto.getAfm());
        return user;
    }

    public UserResponseDto mapUserEntityToUserResponseDto(UserEntity user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setTelephone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setLocation(user.getLocation());
        dto.setCountry(user.getCountry());
        dto.setLatitude(user.getLat());
        dto.setLongitude(user.getLon());
        dto.setAfm(user.getAfm());
        dto.setBuyerrating(user.getBuyerrating());
        dto.setSellerrating(user.getSellerrating());
        return dto;
    }

    public Page<UserResponseDto> mapUserEntityPageToUserResponseDtoPage(Page<UserEntity> userPage) {
        return userPage.map(this::mapUserEntityToUserResponseDto);
    }
}
