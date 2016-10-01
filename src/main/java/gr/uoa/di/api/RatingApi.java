package gr.uoa.di.api;

import gr.uoa.di.dao.BidEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.exception.item.ItemRatingException;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.method.P;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ratings/{itemId}")
public class RatingApi {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${rating_change}")
    private Integer ratingChange;

    @RequestMapping(value = "/seller/happy")
    public void sellerHappy(@PathVariable int itemId) {
        setSellerHappy(itemId, true);
    }

    @RequestMapping(value = "/seller/unhappy")
    public void sellerUnHappy(@PathVariable int itemId) {
        setSellerHappy(itemId, false);
    }

    private void setSellerHappy(int itemId, boolean happy) {
        ItemEntity item = itemRepository.findOneById(itemId);

        if (item.getSellerHappy() != null) {
            throw new ItemRatingException();
        }

        if (item.getOwner().getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            String buyerUsername = getBuyerUsername(item);
            if (buyerUsername != null) {
                item.setSellerHappy(happy);
                itemRepository.save(item);

                /* update buyer's rating */
                UserEntity buyer = userRepository.findOneByUsername(buyerUsername);
                int change = happy ? ratingChange : -ratingChange;
                buyer.setBuyerrating(buyer.getBuyerrating() + change);
                userRepository.save(buyer);
            } else {
                throw new ItemRatingException();
            }
        } else {
            throw new ItemRatingException();
        }
    }

    @RequestMapping(value = "/buyer/happy")
    public void buyerHappy(@PathVariable int itemId) {
        setBuyerHappy(itemId, true);
    }

    @RequestMapping(value = "/buyer/unhappy")
    public void buyerUnHappy(@PathVariable int itemId) {
        setBuyerHappy(itemId, false);
    }

    private void setBuyerHappy(int itemId, boolean happy) {
        ItemEntity item = itemRepository.findOneById(itemId);
        String buyerUsername = getBuyerUsername(item);

        if (item.getBuyerHappy() != null) {
            throw new ItemRatingException();
        }

        if (buyerUsername != null) {
            if (buyerUsername.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
                item.setBuyerHappy(happy);
                itemRepository.save(item);

                /* update seller's rating */
                UserEntity seller = userRepository.findOneByUsername(item.getOwner().getUsername());
                int change = happy ? ratingChange : -ratingChange;
                seller.setSellerrating(seller.getSellerrating() + change);
                userRepository.save(seller);
            } else {
                throw new ItemRatingException();
            }
        } else {
            throw new ItemRatingException();
        }
    }

    private String getBuyerUsername(ItemEntity item) {
        List<BidEntity> bids = item.getBids();
        if (item.getFinished() && bids.size() > 0) {
            return bids.get(bids.size() - 1).getOwner().getUsername();
        }
        return null;
    }
}
