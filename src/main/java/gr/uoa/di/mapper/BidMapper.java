package gr.uoa.di.mapper;

import gr.uoa.di.dao.BidEntity;
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
}
