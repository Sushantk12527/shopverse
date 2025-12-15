package com.shopverse.inventory_service.repository;

import com.shopverse.inventory_service.model.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {

    InventoryEntity findByProductId(Long productId);
}
