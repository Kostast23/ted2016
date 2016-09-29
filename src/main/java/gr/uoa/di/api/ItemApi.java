package gr.uoa.di.api;

import gr.uoa.di.dao.BidEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.RecommendationEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.item.ItemEditDto;
import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.exception.item.ItemBuyingPriceException;
import gr.uoa.di.exception.item.ItemCannotBeEditedException;
import gr.uoa.di.exception.item.ItemDateException;
import gr.uoa.di.exception.item.ItemFieldsException;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.RecommendationRepository;
import gr.uoa.di.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemApi {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    UserService userService;
    @Autowired
    RecommendationRepository recommendationRepository;

    @RequestMapping(value = "/{itemId}")
    public ItemResponseDto getItem(@PathVariable int itemId) {
        ItemEntity item = itemRepository.findOneById(itemId);
        if (!item.getFinished() && item.getEndDate().before(new Date())) {
            item.setFinished(true);
            itemRepository.save(item);
        }
        return itemMapper.mapItemEntityToItemResponseDto(item);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{itemId}")
    public Integer editItem(@PathVariable int itemId, @RequestBody @Valid ItemEditDto item, BindingResult bindingResult) {
        synchronized (BidApi.class) {
            ItemEntity savedItem = itemRepository.findOneById(itemId);
            if (!savedItem.getOwner().getUsername().equals(
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal()) ||
                    !savedItem.getBids().isEmpty()) {
                throw new ItemCannotBeEditedException();
            }
            if (bindingResult.hasErrors()) {
                throw new ItemFieldsException();
            }
            if (item.getBuyprice() != null && item.getBuyprice() < item.getFirstbid()) {
                throw new ItemBuyingPriceException();
            }
            if (item.getEndDate().before(item.getStartDate())) {
                throw new ItemDateException();
            }
            ItemEntity itemEnt = itemMapper.mapItemEditDtoToItemEntity(item);
            itemEnt.setId(itemId);
            itemEnt.setOwner(savedItem.getOwner());
            itemEnt.getPictures().forEach(itemPicturesEntity -> itemPicturesEntity.setItem(itemEnt));
            return itemRepository.save(itemEnt).getId();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{itemId}")
    public void deleteItem(@PathVariable int itemId) {
        synchronized (BidApi.class) {
            ItemEntity savedItem = itemRepository.findOneById(itemId);
            if (!savedItem.getOwner().getUsername().equals(
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal()) ||
                    !savedItem.getBids().isEmpty()) {
                throw new ItemCannotBeEditedException();
            }
            itemRepository.delete(savedItem);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Integer newItem(@RequestBody @Valid ItemEditDto item, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ItemFieldsException();
        }
        if (item.getBuyprice() != null && item.getBuyprice() < item.getFirstbid()) {
            throw new ItemBuyingPriceException();
        }
        if (item.getEndDate().before(item.getStartDate())) {
            throw new ItemDateException();
        }
        ItemEntity itemEnt = itemMapper.mapItemEditDtoToItemEntity(item);
        itemEnt.setOwner(userService.getUserEntity((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        itemEnt.getPictures().forEach(itemPicturesEntity -> itemPicturesEntity.setItem(itemEnt));
        return itemRepository.save(itemEnt).getId();
    }

    @RequestMapping(value = "/active/{username}")
    public Page<ItemResponseDto> getActive(@PathVariable String username, Pageable pageable) {
        List<ItemEntity> items = itemRepository.findByOwner_Username(username);
        List<ItemEntity> active = new ArrayList<>();
        for (ItemEntity item: items) {
            if (!item.getFinished() && item.getEndDate().before(new Date())) {
                item.setFinished(true);
                itemRepository.save(item);
            } else if (!item.getFinished()) {
                active.add(item);
            }
        }
        return new PageImpl<>(active.stream().map(itemMapper::mapItemEntityToItemResponseDto).
                collect(Collectors.toList()), pageable, active.size());
    }

    @RequestMapping(value = "/finished/{username}")
    public Page<ItemResponseDto> getFinished(@PathVariable String username, Pageable pageable) {
        List<ItemEntity> items = itemRepository.findByOwner_Username(username);
        List<ItemEntity> finished = new ArrayList<>();
        for (ItemEntity item: items) {
            if (!item.getFinished() && item.getEndDate().before(new Date())) {
                item.setFinished(true);
                itemRepository.save(item);
                finished.add(item);
            } else if (item.getFinished()) {
                finished.add(item);
            }
        }
        return new PageImpl<>(finished.stream().map(itemMapper::mapItemEntityToItemResponseDto).
                collect(Collectors.toList()), pageable, finished.size());
    }

    @RequestMapping(value = "/participating/{username}")
    public Page<ItemResponseDto> getParticipating(@PathVariable String username, Pageable pageable) {
        if (!username.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new ItemBuyingPriceException();
        }

        UserEntity user = userService.getUserEntity(username);
        List<BidEntity> bids = user.getBids();
        Set<ItemEntity> participating = new HashSet<>();
        for (BidEntity bid: bids) {
            if (!bid.getItem().getFinished()) {
                participating.add(bid.getItem());
            }
        }
        return new PageImpl<>(participating.stream().map(itemMapper::mapItemEntityToItemResponseDto).
                collect(Collectors.toList()), pageable, participating.size());
    }

    @RequestMapping(value = "/bought/{username}")
    public Page<ItemResponseDto> getBought(@PathVariable String username, Pageable pageable) {
        if (!username.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new ItemBuyingPriceException();
        }

        UserEntity user = userService.getUserEntity(username);
        List<BidEntity> bids = user.getBids();
        Set<ItemEntity> bought = new HashSet<>();
        for (BidEntity bid: bids) {
            ItemEntity item = bid.getItem();
            List<BidEntity> itemBids = item.getBids();
            if (item.getFinished() && itemBids.get(itemBids.size() - 1).getOwner().getUsername().equals(username)) {
                bought.add(item);
            }
        }
        return new PageImpl<>(bought.stream().map(itemMapper::mapItemEntityToItemResponseDto).
                collect(Collectors.toList()), pageable, bought.size());
    }

    @RequestMapping(value = "/suggestions")
    public List<ItemResponseDto> getSuggestions() {
        String currentUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity currentUser = userService.getUserEntity(currentUsername);
        List<RecommendationEntity> suggestions = recommendationRepository.findByUser(currentUser);
        return suggestions.stream()
                .sorted((rec1, rec2) -> Double.compare(rec2.getRecValue(), rec1.getRecValue()))
                .map(RecommendationEntity::getItem)
                .map(itemMapper::mapItemEntityToItemResponseDto)
                .collect(Collectors.toList());
    }
}
