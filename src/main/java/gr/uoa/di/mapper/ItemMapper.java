package gr.uoa.di.mapper;

import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.jax.ItemJAX;
import gr.uoa.di.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    BidMapper bidMapper;

    public ItemEntity mapItemJAXToItemEntity(ItemJAX item) {
        ItemEntity itemEnt = new ItemEntity();
        Optional.ofNullable(item.getItemID()).ifPresent(itemId -> itemEnt.setId(Integer.parseInt(itemId)));
        itemEnt.setName(item.getName());
        if (item.getCategory() != null)
            itemEnt.setCategories(item.getCategory().stream().map(categoryMapper::mapItemJAXToItemEntity).collect(Collectors.toList()));
        itemEnt.setCurrentbid(Utils.parseUSDollars(item.getCurrently()));
        itemEnt.setBuyprice(Utils.parseUSDollars(item.getBuyPrice()));
        itemEnt.setFirstbid(Utils.parseUSDollars(item.getFirstBid()));
        if (item.getBids() != null)
            itemEnt.setBids(item.getBids().getBid().stream().map(bidMapper::mapBidJAXToBidEntity).collect(Collectors.toList()));
        Optional.ofNullable(item.getLocation()).ifPresent(location -> {
            Optional.ofNullable(location.getvalue()).ifPresent(value ->
                    itemEnt.setLocation(value));
            Optional.ofNullable(location.getLatitude()).ifPresent(lat ->
                    itemEnt.setLat(Double.valueOf(lat)));
            Optional.ofNullable(location.getLongitude()).ifPresent(lon ->
                    itemEnt.setLat(Double.valueOf(lon)));
        });
        itemEnt.setCountry(item.getCountry());
        itemEnt.setStartDate(Utils.parseXMLDate(item.getStarted()));
        itemEnt.setEndDate(Utils.parseXMLDate(item.getEnds()));
        itemEnt.setDescription(item.getDescription());
        itemEnt.setOwner(userMapper.mapSellerJAXToUserEntity(item.getSeller()));
        return itemEnt;
    }
}
