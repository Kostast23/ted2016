package gr.uoa.di.api;

import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.dto.item.SearchRequestDto;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchApi {

    @Autowired
    EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemMapper itemMapper;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public List<ItemResponseDto> search(@RequestBody SearchRequestDto params) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ItemEntity> query = builder.createQuery(ItemEntity.class);
        EntityType<ItemEntity> itemEntityType = em.getMetamodel().entity(ItemEntity.class);
        Root<ItemEntity> root = query.from(ItemEntity.class);

        List<Predicate> preds = new LinkedList<Predicate>();
        if (params.getName() != null) {
            String[] names = params.getName().split(" ");
            List<Predicate> namePreds = new LinkedList<Predicate>();
            for (String name : names) {
                namePreds.add(builder.like(root.get(itemEntityType.getName()), "%" + name + "%"));
            }
            preds.add(builder.or(namePreds.toArray(new Predicate[namePreds.size()])));
        }
        Predicate allPreds = builder.and(preds.toArray(new Predicate[preds.size()]));
        return itemRepository.findAll(new Specification<ItemEntity>() {
            @Override
            public Predicate toPredicate(Root<ItemEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return null;
            }
        });
    }
}
