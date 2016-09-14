package gr.uoa.di.api;

import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dto.item.ItemEditDto;
import gr.uoa.di.dto.item.ItemResponseDto;
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

    @RequestMapping(method = RequestMethod.POST)
    public void newItem(@RequestBody ItemEditDto item) {
        ItemEntity itemEnt = itemMapper.mapItemEditDtoToItemEntity(item);
        itemEnt.setOwner(userService.getUserEntity((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        itemRepository.save(itemEnt);
    }
}
