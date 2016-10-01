package gr.uoa.di.api;

import gr.uoa.di.dao.BidEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.dto.bid.BidResponseDto;
import gr.uoa.di.exception.bid.AuctionFinishedException;
import gr.uoa.di.exception.bid.AuctionNotStartedException;
import gr.uoa.di.exception.bid.BidLessThanCurrentException;
import gr.uoa.di.exception.bid.BidOnOwnItemException;
import gr.uoa.di.mapper.BidMapper;
import gr.uoa.di.repo.BidRepository;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.UserRepository;
import gr.uoa.di.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    UserRepository userRepository;

    @Autowired
    BidMapper bidMapper;

    @Autowired
    ItemService itemService;

    @RequestMapping(value = "/{itemId}")
    public List<BidResponseDto> getBids(@PathVariable Integer itemId) {
        ItemEntity item = itemRepository.findOneById(itemId);
        if (item == null)
            return null;
        /* map to response and return */
        return bidRepository.findByItem(item).stream().map(bidMapper::mapBidEntityToBidResponseDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.POST)
    public void makeBid(@PathVariable Integer itemId, @RequestBody String bid) {
        /* avoid a race condition */
        synchronized (BidApi.class) {
            Date curDate = new Date();
            UserEntity user = userRepository.findOneByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            ItemEntity item = itemRepository.findOneById(itemId);
            int bidAmount = Integer.parseInt(bid);
            /* security checks */
            if (curDate.before(item.getStartDate())) {
                throw new AuctionNotStartedException();
            }
            if (curDate.after(item.getEndDate())) {
                itemService.doFinalize(item);
                itemRepository.save(item);
            }
            if (item.getFinished()) {
                throw new AuctionFinishedException();
            }
            if (item.getOwner().getId() == user.getId()) {
                throw new BidOnOwnItemException();
            }
            if (bidAmount <= 0 || item.getCurrentbid() >= bidAmount) {
                throw new BidLessThanCurrentException();
            }
            BidEntity bidEnt = new BidEntity();
            bidEnt.setAmount(bidAmount);
            bidEnt.setItem(item);
            bidEnt.setOwner(user);
            bidEnt.setTime(new Date());
            bidRepository.save(bidEnt);

            /* create successful bid */
            item.setCurrentbid(bidAmount);
            if (item.getBuyprice() != null && item.getBuyprice() <= bidAmount) {
                itemService.doFinalize(item);
                item.setEndDate(new Date());
            }
            itemRepository.save(item);
        }
    }
}
