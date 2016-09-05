package gr.uoa.di.mapper;

import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.user.UserRegisterDto;
import gr.uoa.di.dto.user.UserResponseDto;
import gr.uoa.di.jax.BidderJAX;
import gr.uoa.di.jax.LocationJAX;
import gr.uoa.di.jax.SellerJAX;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    public UserEntity mapBidderJAXToUserEntity(BidderJAX bidder) {
        UserEntity user = new UserEntity();
        user.setUsername(bidder.getUserID());
        if (bidder.getRating() != null)
                user.setBuyerrating(Integer.valueOf(bidder.getRating()));
        user.setCountry(bidder.getCountry());
        Optional.ofNullable(bidder.getLocation()).ifPresent(location -> {
            if (location.getvalue() != null)
                    user.setLocation(location.getvalue());
            if (location.getLatitude() != null)
                    user.setLat(Double.valueOf(location.getLatitude()));
            if (location.getLongitude() != null)
                user.setLon(Double.valueOf(location.getLongitude()));
        });
        return user;
    }

    public UserEntity mapSellerJAXToUserEntity(SellerJAX seller) {
        UserEntity user = new UserEntity();
        user.setUsername(seller.getUserID());
        if (seller.getRating() != null)
            user.setSellerrating(Integer.valueOf(seller.getRating()));
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

    public SellerJAX mapUserEntityToSellerJAX(UserEntity owner) {
        SellerJAX seller = new SellerJAX();
        seller.setUserID(owner.getUsername());
        if (owner.getSellerrating() != null)
            seller.setRating(String.valueOf(owner.getSellerrating()));
        return seller;
    }

    public BidderJAX mapUserEntityToBidderJAX(UserEntity owner) {
        BidderJAX bidder = new BidderJAX();
        bidder.setUserID(owner.getUsername());
        if (owner.getBuyerrating() != null)
            bidder.setRating(String.valueOf(owner.getBuyerrating()));
        bidder.setCountry(owner.getCountry());
        LocationJAX loc = new LocationJAX();
        loc.setvalue(owner.getLocation());
        loc.setLatitude(String.valueOf(owner.getLat()));
        loc.setLongitude(String.valueOf(owner.getLon()));
        bidder.setLocation(loc);
        return bidder;
    }
}
