package gr.uoa.di.api;

import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dto.item.ItemEditDto;
import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.exception.item.ItemCannotBeEditedException;
import gr.uoa.di.exception.item.ItemDateException;
import gr.uoa.di.exception.item.ItemFieldsException;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public Integer editItem(@PathVariable int itemId, @RequestBody ItemEditDto item) {
        synchronized (BidApi.class) {
            ItemEntity savedItem = itemRepository.findOneById(itemId);
            if (!savedItem.getOwner().getUsername().equals(
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal()) ||
                    !savedItem.getBids().isEmpty()) {
                throw new ItemCannotBeEditedException();
            }
            if (item.getName() == null || item.getName().length() == 0 || item.getEndDate() == null
                    || item.getStartDate() == null || item.getCategory() == null) {
                throw new ItemFieldsException();
            }
            if (item.getEndDate().before(item.getStartDate())) {
                throw new ItemDateException();
            }
            ItemEntity itemEnt = itemMapper.mapItemEditDtoToItemEntity(item);
            itemEnt.setId(itemId);
            itemEnt.setOwner(savedItem.getOwner());
            return itemRepository.save(itemEnt).getId();
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public Integer newItem(@RequestBody ItemEditDto item) {
        if (item.getName() == null || item.getName().length() == 0 || item.getEndDate() == null
        || item.getStartDate() == null || item.getCategory() == null) {
            throw new ItemFieldsException();
        }
        Date curDate = new Date();
        if (item.getStartDate().before(curDate) || item.getEndDate().before(item.getStartDate())) {
            throw new ItemDateException();
        }
        ItemEntity itemEnt = itemMapper.mapItemEditDtoToItemEntity(item);
        itemEnt.setOwner(userService.getUserEntity((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        return itemRepository.save(itemEnt).getId();
    }
}
