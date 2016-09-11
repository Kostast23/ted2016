package gr.uoa.di.mapper;

import gr.uoa.di.dao.BidEntity;
import gr.uoa.di.dto.bid.BidResponseDto;
import gr.uoa.di.jax.BidJAX;
import gr.uoa.di.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BidMapper {
    @Autowired
    UserMapper userMapper;

    public BidEntity mapBidJAXToBidEntity(BidJAX bid) {
        BidEntity bidEnt = new BidEntity();
        bidEnt.setAmount(Utils.parseUSDollars(bid.getAmount()));
        Optional.ofNullable(bid.getBidder()).ifPresent(bidderJAX ->
                bidEnt.setOwner(userMapper.mapBidderJAXToUserEntity(bidderJAX)));
        bidEnt.setTime(Utils.parseXMLDate(bid.getTime()));
        return bidEnt;
    }

    public BidJAX mapBidEntityToBidJAX(BidEntity bidEntity) {
        BidJAX bid = new BidJAX();
        bid.setAmount(Utils.toUSDollars(bidEntity.getAmount()));
        bid.setTime(Utils.toXMLDate(bidEntity.getTime()));
        bid.setBidder(userMapper.mapUserEntityToBidderJAX(bidEntity.getOwner()));
        return bid;
    }

    public BidResponseDto mapBidEntityToBidResponseDto(BidEntity bidEntity) {
        BidResponseDto bid = new BidResponseDto();
        bid.setId(bidEntity.getId());
        bid.setAmount(bidEntity.getAmount());
        bid.setTime(bidEntity.getTime());
        bid.setOwner(bidEntity.getOwner().getUsername());
        return bid;
    }
}
