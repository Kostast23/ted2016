package gr.uoa.di.service;

import gr.uoa.di.api.BidApi;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.MessageEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ItemService {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MessageRepository messageRepository;

    private void sendMessageTo(UserEntity user, UserEntity from, String subject, String message) {
        MessageEntity msg = new MessageEntity();
        msg.setTo(user);
        msg.setFrom(from);
        msg.setSubject(subject);
        msg.setMessage(message);
        msg.setSentdate(new Date());
        msg.setDeletedsender(true);
        messageRepository.save(msg);
    }

    public boolean finalizeFinishedPageItems(Page<ItemEntity> page) {
        Date curDate = new Date();
        /* make sure we don't return any finished auctions that aren't marked finished */
        return page.getContent().stream().allMatch(item-> {
            if (!item.getFinished() && item.getEndDate().before(curDate)) {
                doFinalize(item);
                itemRepository.save(item);
                return false;
            } else {
                return true;
            }
        });
    }

    public int finalizeAuctions() {
        synchronized (BidApi.class) {
            Date curDate = new Date();
            /* find finished auctions and finalize them */
            List<ItemEntity> toFinish = itemRepository.findByFinishedIsFalseAndEndDateLessThan(curDate);
            toFinish.stream().forEach(itemEntity -> doFinalize(itemEntity));
            itemRepository.save(toFinish);
            return toFinish.size();
        }
    }

    public void doFinalize(ItemEntity item) {
        if (item.getBids().size() > 0) {
            UserEntity winner = item.getBids().stream()
                    .max((o1, o2) -> Integer.compare(o1.getAmount(), o2.getAmount()))
                    .get().getOwner();
            /*
             * create messages for auction owner and winner
             * we parse the message manually here as it will be shown unescaped
             * in order for the links to work
             */
            sendMessageTo(item.getOwner(), winner,
                    "Your auction has ended: " + item.getName(),
                    "The auction " + item.getName() +
                            " has been won by user " + winner.getUsername() +". " +
                            "Click 'Reply' below to message them!");

            sendMessageTo(winner, item.getOwner(),
                    "You won the auction: " + item.getName(),
                    "You won the auction " + item.getName() +
                            " by user " + item.getOwner().getUsername() +". " +
                            "Click 'Reply' below to message them!");
        } else {
            sendMessageTo(item.getOwner(), item.getOwner(),
                    "Your auction has ended: " + item.getName(),
                    "The auction " + item.getName() + " has finished without any bids");
        }
        item.setFinished(true);
    }

}
