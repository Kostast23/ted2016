package gr.uoa.di.api;

import com.mysema.query.types.expr.BooleanExpression;
import gr.uoa.di.dao.QItemEntity;
import gr.uoa.di.dto.item.ItemResponseDto;
import gr.uoa.di.dto.item.SearchRequestDto;
import gr.uoa.di.mapper.ItemMapper;
import gr.uoa.di.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/search")
public class SearchApi {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemMapper itemMapper;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Page<ItemResponseDto> search(@RequestBody SearchRequestDto params) {
        QItemEntity item = QItemEntity.itemEntity;
        List<BooleanExpression> exprs = new LinkedList<>();
        Pageable pageable = new PageRequest(params.getPage() - 1, params.getSize());

        if (params.getName() != null) {
            exprs.add(Arrays.asList(params.getName().split(" ")).stream()
                    .map(s -> item.name.likeIgnoreCase("%" + s + "%"))
                    .reduce((exp1, exp2) -> exp1.and(exp2)).get());
        }

        if (params.getDescription() != null) {
            exprs.add(Arrays.asList(params.getDescription().split(" ")).stream()
                    .map(s -> item.description.likeIgnoreCase("%" + s + "%"))
                    .reduce((exp1, exp2) -> exp1.and(exp2)).get());
        }

        Optional<BooleanExpression> finalExpr = exprs.stream().reduce((exp1, exp2) -> exp1.and(exp2));

        if (finalExpr.isPresent()) {
            Page<ItemResponseDto> results = itemRepository.findAll(finalExpr.get(), pageable).map(itemMapper::mapItemEntityToItemResponseDto);
            return results;
        }
        return null;
    }
}
