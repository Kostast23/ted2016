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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {
    @Autowired
    ItemMapper itemMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BidRepository bidRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public void restoreFile(MultipartFile uploadFile) {
        try {
            /* unmarshall the xml file */
            JAXBContext jaxbContext = JAXBContext.newInstance(ItemsJAX.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ItemsJAX items = (ItemsJAX) jaxbUnmarshaller.unmarshal(uploadFile.getInputStream());

            Map<String, UserEntity> allUsers = userRepository.findAll().stream().collect(Collectors.toMap(UserEntity::getUsername, Function.identity()));
            Map<Pair<String, CategoryEntity>, CategoryEntity> allCategories = categoryRepository.findAll().stream().collect(Collectors.toMap(o -> new ImmutablePair<>(o.getName(), o.getParentCategory()), Function.identity()));

            /* for each item in the file */
            items.getItem().stream().map(itemMapper::mapItemJAXToItemEntity).forEach(item -> {
                /* add the owner to the db if they don't exist */
                UserEntity owner = allUsers.get(item.getOwner().getUsername());
                if (owner != null) {
                    owner.setSellerrating(item.getOwner().getSellerrating());
                    item.setOwner(owner);
                } else {
                    userRepository.save(item.getOwner());
                    allUsers.put(item.getOwner().getUsername(), item.getOwner());
                }

                /* add all bidders */
                Optional.ofNullable(item.getBids()).ifPresent(bids ->
                        bids.stream().forEach(bid -> {
                            UserEntity bidOwner = allUsers.get(bid.getOwner().getUsername());
                            if (bidOwner != null) {
                                bidOwner.setBuyerrating(bid.getOwner().getBuyerrating());
                                bid.setOwner(bidOwner);
                            } else {
                                userRepository.save(bid.getOwner());
                                allUsers.put(bid.getOwner().getUsername(), bid.getOwner());
                            }
                        }));

                List<CategoryEntity> categories = new LinkedList<CategoryEntity>();
                CategoryEntity currentCategory = item.getCategory();
                while (currentCategory != null) {
                    categories.add(0, currentCategory);
                    currentCategory = currentCategory.getParentCategory();
                }

                /* add all categories */
                categories.forEach(category -> {
                    CategoryEntity existingCategory = allCategories.get(new ImmutablePair(category.getName(), category.getParentCategory()));
                    if (existingCategory == null) {
                        categoryRepository.save(category);
                        allCategories.put(new ImmutablePair(category.getName(), category.getParentCategory()), category);
                    } else {
                        category.setId(existingCategory.getId());
                    }
                });

                /* map item and save it */
                ItemEntity savedItem = itemRepository.save(item);
                if (item.getBids() != null)
                    item.getBids().stream().forEach(bidEntity -> {
                        bidEntity.setItem(savedItem);
                        bidRepository.save(bidEntity);
                    });
            });
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] createXMLDump() {
        try {
            /* map items to their JAX objects and create the xml dump */
            ItemsJAX items = new ItemsJAX();
            items.getItem().addAll(itemRepository.findAll().stream().map(itemMapper::mapItemEntityToItemJAX).collect(Collectors.toList()));
            JAXBContext jaxbContext = JAXBContext.newInstance(ItemsJAX.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            jaxbMarshaller.marshal(items, bos);
            return bos.toByteArray();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
