package gr.uoa.di.mapper;

import gr.uoa.di.dao.BidEntity;
import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.ItemPicturesEntity;
import gr.uoa.di.dto.item.ItemEditDto;
import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.dto.item.PictureDto;
import gr.uoa.di.jax.BidsJAX;
import gr.uoa.di.jax.ItemJAX;
import gr.uoa.di.jax.LocationJAX;
import gr.uoa.di.repo.CategoryRepository;
import gr.uoa.di.repo.ItemPicturesRepository;
import gr.uoa.di.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemMapper {
    @Autowired
    UserMapper userMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    BidMapper bidMapper;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private ItemPicturesRepository itemPicturesRepository;

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
        itemEnt.setFinished(itemEnt.getEndDate().before(new Date()) || (itemEnt.getBuyprice() != null && itemEnt.getBuyprice() <= itemEnt.getCurrentbid()));
        itemEnt.setRatedByBuyer(false);
        itemEnt.setRatedBySeller(false);
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

    public ItemResponseDto mapItemEntityToItemResponseDto(ItemEntity itemEntity) {
        ItemResponseDto item = new ItemResponseDto();
        item.setId(itemEntity.getId());
        item.setName(itemEntity.getName());
        item.setDescription(itemEntity.getDescription());
        item.setBuyprice(itemEntity.getBuyprice());
        item.setCurrentbid(itemEntity.getCurrentbid());
        item.setFirstbid(itemEntity.getFirstbid());
        item.setLocation(itemEntity.getLocation());
        item.setLat(itemEntity.getLat());
        item.setLon(itemEntity.getLon());
        item.setCountry(itemEntity.getCountry());
        item.setStartDate(itemEntity.getStartDate());
        item.setEndDate(itemEntity.getEndDate());
        item.setFinished(itemEntity.getFinished());
        item.setImages(itemEntity.getPictures().stream().map(ItemPicturesEntity::getUuid).collect(Collectors.toList()));
        item.setSellerUsername(itemEntity.getOwner().getUsername());
        item.setCategory(categoryMapper.mapCategoryEntityToCategoryResponseDto(itemEntity.getCategory(), true, 0));

        List<BidEntity> bids = itemEntity.getBids();
        if (item.getFinished() && bids.size() > 0) {
            item.setWinnerUsername(bids.get(bids.size() - 1).getOwner().getUsername());
        } else {
            item.setWinnerUsername(null);
        }
        return item;
    }

    public ItemEntity mapItemEditDtoToItemEntity(ItemEditDto item) {
        ItemEntity itemEnt = new ItemEntity();
        itemEnt.setName(item.getName());
        itemEnt.setDescription(item.getDescription());
        if (item.getCategory() != null)
            itemEnt.setCategory(categoryRepository.findOneById(item.getCategory()));
        itemEnt.setBuyprice(item.getBuyprice());
        itemEnt.setFirstbid(item.getFirstbid());
        itemEnt.setCurrentbid(item.getFirstbid());
        itemEnt.setCountry(item.getCountry());
        itemEnt.setLocation(item.getLocation());
        itemEnt.setLat(item.getLat());
        itemEnt.setLon(item.getLon());
        itemEnt.setFinished(false);
        itemEnt.setRatedByBuyer(false);
        itemEnt.setRatedBySeller(false);
        itemEnt.setStartDate(item.getStartDate());
        itemEnt.setEndDate(item.getEndDate());
        if (item.getImages() == null) {
            item.setImages(new ArrayList<>());
        }
        itemEnt.setPictures(item.getImages().stream().map(
                image -> itemPicturesRepository.findOneByUuid(image)).collect(Collectors.toList()));
        return itemEnt;
    }
}
