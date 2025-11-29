package com.pressing.repository;

import com.pressing.model.Category;
import com.pressing.model.Item;
import com.pressing.model.Merchant;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {

  Item findByName(@Param("name") String name);

  Collection<Item> findByCategory(@Param("category") Category category);

  Page<Item> findByCategory(@Param("category") Category category, Pageable pageable);

  Page<Item> findByMerchant(Merchant merchant, Pageable pageable);
}
