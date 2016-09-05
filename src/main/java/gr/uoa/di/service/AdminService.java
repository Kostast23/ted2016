package gr.uoa.di.service;

import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dao.UserEntity;
import gr.uoa.di.jax.ItemsJAX;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.CategoryRepository;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.repo.UserRepository;
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
    ItemRepository itemRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public void restoreFile(MultipartFile uploadFile) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ItemsJAX.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ItemsJAX items = (ItemsJAX) jaxbUnmarshaller.unmarshal(uploadFile.getInputStream());

            Map<String, UserEntity> allUsers = userRepository.findAll().stream().collect(Collectors.toMap(UserEntity::getUsername, Function.identity()));
            Map<String, CategoryEntity> allCategories = categoryRepository.findAll().stream().collect(Collectors.toMap(CategoryEntity::getName, Function.identity()));

            items.getItem().stream().map(itemMapper::mapItemJAXToItemEntity).forEach(item -> {
                UserEntity owner = allUsers.get(item.getOwner().getUsername());
                if (owner != null) {
                    owner.setSellerrating(item.getOwner().getSellerrating());
                    item.setOwner(owner);
                } else {
                    userRepository.save(item.getOwner());
                    allUsers.put(item.getOwner().getUsername(), item.getOwner());
                }

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

                Optional.ofNullable(item.getCategories()).ifPresent(categories ->
                        item.setCategories(categories.stream().map(category -> {
                            CategoryEntity existingCategory = allCategories.get(category.getName());
                            if (existingCategory == null) {
                                categoryRepository.save(category);
                                allCategories.put(category.getName(), category);
                                return category;
                            }
                            return existingCategory;
                        }).collect(Collectors.toList())));

                itemRepository.save(item);
            });
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] createXMLDump() {
        try {
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
