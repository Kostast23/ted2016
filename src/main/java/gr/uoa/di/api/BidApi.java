package gr.uoa.di.api;

import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dto.bid.BidResponseDto;
import gr.uoa.di.mapper.BidMapper;
import gr.uoa.di.repo.BidRepository;
import gr.uoa.di.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bids")
public class BidApi {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    BidMapper bidMapper;

    @RequestMapping(value = "/{itemId}")
    public List<BidResponseDto> getBids(@PathVariable Integer itemId) {
        ItemEntity item = itemRepository.findOneById(itemId);
        if (item == null)
            return null;
        return bidRepository.findByItem(item).stream().map(bidMapper::mapBidEntityToBidResponseDto).collect(Collectors.toList());
    }


    @RequestMapping(value = "/{itemId}", method = RequestMethod.POST)
    public void makeBid(@PathVariable Integer itemId, @RequestBody String bid) {
        // check current bid
        // make it synchronized
        Object a = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(a);
    }
}
