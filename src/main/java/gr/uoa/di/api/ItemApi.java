package gr.uoa.di.api;

import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dto.item.ItemEditDto;
import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.exception.item.ItemBuyingPriceException;
import gr.uoa.di.exception.item.ItemCannotBeEditedException;
import gr.uoa.di.exception.item.ItemDateException;
import gr.uoa.di.exception.item.ItemFieldsException;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/items")
public class ItemApi {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    UserService userService;

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
        if (item.getBuyprice() < item.getFirstbid()) {
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
}
