package gr.uoa.di.api;

import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/items")
public class ItemApi {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemMapper itemMapper;

    @RequestMapping(value = "/{itemId}")
    public ItemResponseDto getItem(@PathVariable int itemId) {
        return itemMapper.mapItemEntityToItemResponseDto(itemRepository.findOneById(itemId));
    }
}
