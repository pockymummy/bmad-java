package com.example.demo.repository;

import com.example.demo.entity.PartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartItemRepository extends JpaRepository<PartItem, Long> {

    List<PartItem> findByPartsListingId(Long partsListingId);
}
