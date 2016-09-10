package gr.uoa.di.mapper;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.ItemPicturesEntity;
import gr.uoa.di.dto.item.ItemListResponseDto;
import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.dto.item.PictureDto;
import gr.uoa.di.jax.BidsJAX;
import gr.uoa.di.jax.ItemJAX;
import gr.uoa.di.jax.LocationJAX;
import gr.uoa.di.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
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
        if (item.getCategory() != null) {
            itemEnt.setCategory(item.getCategory().stream().map(categoryMapper::mapCategoryJAXToCategoryEntity).reduce((cat1, cat2) -> {
                cat2.setParentCategory(cat1);
                return cat2;
            }).get());
        }
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
        List<CategoryEntity> categories = new LinkedList<CategoryEntity>();
        CategoryEntity cat = itemEnt.getCategory();
        while (cat != null) {
            categories.add(0, cat);
            cat = cat.getParentCategory();
        }
        item.getCategory().addAll(categories.stream().map(categoryMapper::mapCategoryEntityToCategoryJAX).collect(Collectors.toList()));
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

    public ItemListResponseDto mapItemEntityToItemListResponseDto(ItemEntity itemEntity) {
        ItemListResponseDto item = new ItemListResponseDto();
        item.setId(itemEntity.getId());
        item.setName(itemEntity.getName());
        item.setCurrentbid(itemEntity.getCurrentbid());
        item.setBuyprice(itemEntity.getBuyprice());
        item.setLocation(itemEntity.getLocation());
        item.setCountry(itemEntity.getCountry());
        item.setEndDate(itemEntity.getEndDate());
        if (!itemEntity.getPictures().isEmpty())
            item.setPicture(mapItemPicturesEntityToPictureDto(itemEntity.getPictures().get(0)));
        item.setSellerUsername(itemEntity.getOwner().getUsername());
        return item;
    }

    public ItemResponseDto mapItemEntityToItemResponseDto(ItemEntity itemEntity) {
        ItemResponseDto item = new ItemResponseDto();
        item.setId(itemEntity.getId());
        item.setName(itemEntity.getName());
        item.setDescription(itemEntity.getDescription());
        item.setBuyprice(itemEntity.getBuyprice());
        item.setCurrentbid(itemEntity.getCurrentbid());
        item.setLocation(itemEntity.getLocation());
        item.setLat(itemEntity.getLat());
        item.setLon(itemEntity.getLon());
        item.setCountry(itemEntity.getCountry());
        item.setStartDate(itemEntity.getStartDate());
        item.setEndDate(itemEntity.getEndDate());
        item.setPictures(itemEntity.getPictures().stream().map(this::mapItemPicturesEntityToPictureDto).collect(Collectors.toList()));
        item.setSellerUsername(itemEntity.getOwner().getUsername());
        item.setCategory(categoryMapper.mapCategoryEntityToCategoryResponseDto(itemEntity.getCategory()));
        return item;
    }

    public PictureDto mapItemPicturesEntityToPictureDto(ItemPicturesEntity itemPicturesEntity) {
        PictureDto picture = new PictureDto();
        picture.setName(itemPicturesEntity.getFilename());
        picture.setContent(itemPicturesEntity.getImage());
        return picture;
    }
}
