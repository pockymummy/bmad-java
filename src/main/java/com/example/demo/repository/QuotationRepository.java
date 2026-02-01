package com.example.demo.repository;

import com.example.demo.entity.Quotation;
import com.example.demo.entity.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    List<Quotation> findByListingId(Long listingId);

    List<Quotation> findByStatus(QuotationStatus status);
}
