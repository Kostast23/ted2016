package gr.uoa.di.api;

import com.mysema.query.types.expr.BooleanExpression;
import gr.uoa.di.dao.CategoryEntity;
import gr.uoa.di.dao.ItemEntity;
import gr.uoa.di.dao.QItemEntity;
import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.dto.item.SearchRequestDto;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.CategoryRepository;
import gr.uoa.di.repo.ItemRepository;
import gr.uoa.di.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/search")
public class SearchApi {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ItemService itemService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Page<ItemResponseDto> search(@RequestBody SearchRequestDto params) {
        QItemEntity item = QItemEntity.itemEntity;
        List<BooleanExpression> exprs = new LinkedList<>();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "finished"), new Sort.Order(Sort.Direction.DESC, "startDate"));
        Pageable pageable = new PageRequest(params.getPage() - 1, params.getSize(), sort);

        /*
         * add expressions to match items whose names contain all
         * of the space-separated values in the field
         */
        if (params.getName() != null) {
            exprs.add(Arrays.asList(params.getName().split(" ")).stream()
                    .map(s -> item.name.likeIgnoreCase("%" + s + "%"))
                    .reduce((exp1, exp2) -> exp1.and(exp2)).get());
        }

        /* same for descriptions */
        if (params.getDescription() != null) {
            exprs.add(Arrays.asList(params.getDescription().split(" ")).stream()
                    .map(s -> item.description.likeIgnoreCase("%" + s + "%"))
                    .reduce((exp1, exp2) -> exp1.and(exp2)).get());
        }

        /* add the rest of the constraints */
        if (params.getMin() != null) {
            exprs.add(item.currentbid.goe(params.getMin()));
        }

        if (params.getMax() != null) {
            exprs.add(item.currentbid.loe(params.getMax()));
        }

        if (params.getLocation() != null) {
            exprs.add(item.location.like("%" + params.getLocation() + "%"));
        }

        if (params.getFinished() != null && params.getFinished()) {
            exprs.add(item.finished.eq(false));
        }

        /* if the user has selected a category, match that or any of its subcategories */
        if (params.getCategory() != null && params.getCategory() != -1) {
            Queue<CategoryEntity> remainingCategories = new ArrayDeque<>();
            List<CategoryEntity> includedCategories = new LinkedList<>();
            remainingCategories.add(categoryRepository.findOneById(params.getCategory()));

            /* add all subcategories to a list */
            while (!remainingCategories.isEmpty()) {
                CategoryEntity category = remainingCategories.poll();
                includedCategories.add(category);
                remainingCategories.addAll(category.getSubcategories());
            }

            exprs.add(item.category.in(includedCategories));
        }

        Optional<BooleanExpression> finalExpr = exprs.stream().reduce((exp1, exp2) -> exp1.and(exp2));

        if (finalExpr.isPresent()) {
            Page<ItemEntity> page;
            do {
                /* finalize items that need to be finalized before returning the page if necessary */
                page = itemRepository.findAll(finalExpr.get(), pageable);
            } while (!itemService.finalizeFinishedPageItems(page));
            return page.map(itemMapper::mapItemEntityToItemResponseDto);
        }
        return null;
    }
}
