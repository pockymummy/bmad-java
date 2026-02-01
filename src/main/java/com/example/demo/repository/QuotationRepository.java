package com.example.demo.repository;

import com.example.demo.entity.Quotation;
import com.example.demo.enums.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    List<Quotation> findByPartsShopId(Long partsShopId);

    List<Quotation> findByStatus(QuotationStatus status);

    List<Quotation> findByExaminerId(Long examinerId);

    boolean existsByPartsListingIdAndPartsShopId(Long partsListingId, Long partsShopId);

    @Query("SELECT q FROM Quotation q " +
           "LEFT JOIN FETCH q.quotationItems " +
           "LEFT JOIN FETCH q.partsListing pl " +
           "LEFT JOIN FETCH q.partsShop " +
           "WHERE q.id = :id")
    Optional<Quotation> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT q FROM Quotation q " +
           "LEFT JOIN FETCH q.partsListing " +
           "LEFT JOIN FETCH q.partsShop " +
           "WHERE q.status = :status")
    List<Quotation> findByStatusWithDetails(@Param("status") QuotationStatus status);

    @Query("SELECT q FROM Quotation q " +
           "LEFT JOIN FETCH q.partsListing " +
           "LEFT JOIN FETCH q.partsShop " +
           "WHERE q.examiner.id = :examinerId AND q.status != 'PENDING'")
    List<Quotation> findReviewedByExaminer(@Param("examinerId") Long examinerId);
}
