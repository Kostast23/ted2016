package gr.uoa.di.service;

import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.RecommendationEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.repo.BidRepository;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.RecommendationRepository;
import gr.uoa.di.repo.UserRepository;
import gr.uoa.di.service.helpers.ItemRecommendations;
import gr.uoa.di.service.helpers.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class SuggestionService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BidRepository bidRepository;
    @Autowired
    RecommendationRepository recommendationRepository;

    private double cosSimilarity(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> common = new HashSet<>(s1);
        common.retainAll(s2);
        int intersect = common.size();
        return intersect / Math.sqrt((double)(s1.size() * s2.size()));
    }

    public void runAutosuggestions() {
        Map<Integer, Set<Integer>> userBidOnItems = new HashMap<>();
        Map<Integer, ItemRecommendations> userItemSuggestions = new HashMap<>();
        Map<Integer, List<UserSimilarity>> userSimilarities = new HashMap<>();

        bidRepository.getUserBidsOnItems().forEach(entry -> {
            Set<Integer> items = userBidOnItems
                    .computeIfAbsent(entry.getUserId(), i -> new HashSet<>());
            items.add(entry.getItemId());
        });

        userBidOnItems.forEach((user, items) -> {
            userBidOnItems.forEach((user2, items2) -> {
                if (user != user2) {
                    double similarity = cosSimilarity(items, items2);
                    if (similarity > 0) {
                        userSimilarities.computeIfAbsent(user, i -> new ArrayList<>())
                                .add(new UserSimilarity(user2, similarity));
                    }
                }
            });
        });

        userSimilarities.values().stream().forEach(similarities -> {
            similarities.sort((o1, o2) -> Double.compare(o2.getSimilarity(), o1.getSimilarity()));
        });

        userSimilarities.forEach((user, similar) -> {
            ItemRecommendations recommended = new ItemRecommendations();
            Set<Integer> currentUserBids = userBidOnItems.get(user);
            similar.forEach(userSimilarity ->
                    userBidOnItems.get(userSimilarity.getUser())
                    .forEach(item -> {
                        if (!currentUserBids.contains(item)) {
                            recommended.addRecommendation(item, userSimilarity.getSimilarity());
                        }
                    }));
            userItemSuggestions.put(user, recommended);
        });

        List<RecommendationEntity> recEnts = new LinkedList<>();
        Map<Integer, UserEntity> allUsers = new HashMap<>();
        Map<Integer, ItemEntity> allItems = new HashMap<>();
        userRepository.findAll().forEach(userEntity -> allUsers.put(userEntity.getId(), userEntity));
        itemRepository.findAll().forEach(itemEntity -> allItems.put(itemEntity.getId(), itemEntity));

        userItemSuggestions.forEach((user, itemRecommendations) -> {
            itemRecommendations.getTop(5).forEach(item -> {
                RecommendationEntity recEnt = new RecommendationEntity();
                recEnt.setUser(allUsers.get(user));
                recEnt.setItem(allItems.get(item.getKey()));
                recEnt.setRecValue(item.getValue());
                recEnts.add(recEnt);
            });
        });
        recommendationRepository.save(recEnts);
    }

}
