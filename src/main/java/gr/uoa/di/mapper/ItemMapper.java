package gr.uoa.di.mapper;

import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.jax.BidsJAX;
import gr.uoa.di.jax.ItemJAX;
import gr.uoa.di.jax.LocationJAX;
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
            itemEnt.setCategories(item.getCategory().stream().map(categoryMapper::mapCategoryJAXToCategoryEntity).collect(Collectors.toList()));
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
                    itemEnt.setLon(Double.valueOf(lon)));
        });
        itemEnt.setCountry(item.getCountry());
        itemEnt.setStartDate(Utils.parseXMLDate(item.getStarted()));
        itemEnt.setEndDate(Utils.parseXMLDate(item.getEnds()));
        itemEnt.setDescription(item.getDescription());
        itemEnt.setOwner(userMapper.mapSellerJAXToUserEntity(item.getSeller()));
        return itemEnt;
    }

    public ItemJAX mapItemEntityToItemJAX(ItemEntity itemEnt) {
        ItemJAX item = new ItemJAX();
        item.setItemID(String.valueOf(itemEnt.getId()));
        item.setName(itemEnt.getName());
        itemEnt.getCategories().forEach(categoryEntity -> item.getCategory().add(categoryMapper.mapCategoryEntityToCategoryJAX(categoryEntity)));
        item.setCurrently(Utils.toUSDollars(itemEnt.getCurrentbid()));
        item.setBuyPrice(Utils.toUSDollars(itemEnt.getBuyprice()));
        item.setFirstBid(Utils.toUSDollars(itemEnt.getFirstbid()));
        item.setBids(new BidsJAX());
        itemEnt.getBids().forEach(bidEntity -> item.getBids().getBid().add(bidMapper.mapBidEntityToBidJAX(bidEntity)));
        item.setNumberOfBids(String.valueOf(item.getBids().getBid().size()));
        LocationJAX loc = new LocationJAX();
        if (itemEnt.getLocation() != null)
            loc.setvalue(itemEnt.getLocation());
        if (itemEnt.getLat() != null)
            loc.setLatitude(String.valueOf(itemEnt.getLat()));
        if (itemEnt.getLon() != null)
            loc.setLongitude(String.valueOf(itemEnt.getLon()));
        item.setLocation(loc);
        item.setCountry(itemEnt.getCountry());
        item.setStarted(Utils.toXMLDate(itemEnt.getStartDate()));
        item.setEnds(Utils.toXMLDate(itemEnt.getEndDate()));
        item.setDescription(itemEnt.getDescription());
        item.setSeller(userMapper.mapUserEntityToSellerJAX(itemEnt.getOwner()));
        return item;
    }
}
