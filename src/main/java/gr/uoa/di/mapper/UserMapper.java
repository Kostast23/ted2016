package gr.uoa.di.mapper;

import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.user.SellerResponseDto;
import gr.uoa.di.dto.user.UserRegisterDto;
import gr.uoa.di.dto.user.UserResponseDto;
import gr.uoa.di.jax.BidderJAX;
import gr.uoa.di.jax.LocationJAX;
import gr.uoa.di.jax.SellerJAX;
import gr.uoa.di.service.Utils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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
        user.setSalt("");
        try {
            user.setPassword(Utils.sha1(Utils.sha1(bidder.getUserID())));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        user.setValidated(true);
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
        user.setValidated(true);
        user.setSalt("");
        try {
            user.setPassword(Utils.sha1(Utils.sha1(seller.getUserID())));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        dto.setValidated(user.getValidated());
        return dto;
    }

    public SellerResponseDto mapUserEntityToSellerResponseDto(UserEntity user) {
        SellerResponseDto dto = new SellerResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLocation(user.getLocation());
        dto.setCountry(user.getCountry());
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
        if (owner.getLocation() != null)
            loc.setvalue(owner.getLocation());
        if (owner.getLat() != null)
            loc.setLatitude(String.valueOf(owner.getLat()));
        if (owner.getLon() != null)
            loc.setLongitude(String.valueOf(owner.getLon()));
        bidder.setLocation(loc);
        return bidder;
    }
}
