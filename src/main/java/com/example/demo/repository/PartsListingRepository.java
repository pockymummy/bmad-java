package com.example.demo.repository;

import com.example.demo.entity.PartsListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartsListingRepository extends JpaRepository<PartsListing, Long> {

    List<PartsListing> findByInsurerId(Long insurerId);

    List<PartsListing> findByActiveTrue();

    @Query("SELECT pl FROM PartsListing pl WHERE pl.active = true AND pl.id NOT IN " +
           "(SELECT q.partsListing.id FROM Quotation q WHERE q.partsShop.id = :partsShopId)")
    List<PartsListing> findAvailableListingsForPartsShop(@Param("partsShopId") Long partsShopId);

    @Query("SELECT pl FROM PartsListing pl LEFT JOIN FETCH pl.partItems WHERE pl.id = :id")
    Optional<PartsListing> findByIdWithPartItems(@Param("id") Long id);

    @Query("SELECT DISTINCT pl FROM PartsListing pl " +
           "LEFT JOIN FETCH pl.partItems " +
           "LEFT JOIN FETCH pl.quotations q " +
           "LEFT JOIN FETCH q.partsShop " +
           "WHERE pl.insurer.id = :insurerId")
    List<PartsListing> findByInsurerIdWithQuotations(@Param("insurerId") Long insurerId);
}
