package gr.uoa.di.service;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.jax.ItemsJAX;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.BidRepository;
import gr.uoa.di.repo.CategoryRepository;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class SuggestionService {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    private double cosSimilarity(Set<Integer> s1, Set<Integer> s2) {
        Set<Integer> common = new HashSet<>(s1);
        common.retainAll(s2);
        int intersect = common.size();
        return intersect / Math.sqrt((double)(s1.size() * s2.size()));
    }

    public void runAutosuggestions() {
        Map<Integer, Set<Integer>> userBidOnItems = new HashMap<>();
        Map<Integer, Set<Integer>> itemBidByUser = new HashMap<>();

        userRepository.findAll().stream().forEach(user -> {
            userBidOnItems.put(user.getId(), user.getBids().stream()
                    .map(bid -> bid.getItem().getId())
                    .collect(Collectors.toSet()));
        });

        itemRepository.findAll().stream().forEach(item -> {
            itemBidByUser.put(item.getId(), item.getBids().stream()
                    .map(bid -> bid.getOwner().getId())
                    .collect(Collectors.toSet()));
        });
    }

}
