package com.example.demo.service;

import com.example.demo.dto.request.QuotationDecisionRequest;
import com.example.demo.dto.response.QuotationResponse;
import com.example.demo.entity.Quotation;
import com.example.demo.entity.User;
import com.example.demo.enums.QuotationStatus;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.repository.QuotationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExaminerService {

    private final QuotationRepository quotationRepository;
    private final UserService userService;
    private final QuotationService quotationService;

    public ExaminerService(QuotationRepository quotationRepository,
                          UserService userService,
                          QuotationService quotationService) {
        this.quotationRepository = quotationRepository;
        this.userService = userService;
        this.quotationService = quotationService;
    }

    @Transactional(readOnly = true)
    public List<QuotationResponse> getPendingQuotations(Long examinerId) {
        userService.getExaminer(examinerId);
        return quotationRepository.findByStatusWithDetails(QuotationStatus.PENDING).stream()
                .map(QuotationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuotationResponse getQuotationDetails(Long examinerId, Long quotationId) {
        userService.getExaminer(examinerId);
        return quotationService.getQuotationById(quotationId);
    }

    public QuotationResponse reviewQuotation(Long examinerId, Long quotationId,
                                             QuotationDecisionRequest decision) {
        User examiner = userService.getExaminer(examinerId);
        Quotation quotation = quotationService.getQuotationEntity(quotationId);

        if (quotation.getStatus() != QuotationStatus.PENDING) {
            throw new BusinessRuleException("Can only review quotations with PENDING status. " +
                    "Current status: " + quotation.getStatus());
        }

        quotation.setStatus(decision.getApproved() ? QuotationStatus.APPROVED : QuotationStatus.DISAPPROVED);
        quotation.setReviewNotes(decision.getReviewNotes());
        quotation.setExaminer(examiner);
        quotation.setReviewedAt(LocalDateTime.now());

        Quotation savedQuotation = quotationRepository.save(quotation);
        return QuotationResponse.fromEntity(savedQuotation);
    }

    @Transactional(readOnly = true)
    public List<QuotationResponse> getReviewedQuotations(Long examinerId) {
        userService.getExaminer(examinerId);
        return quotationRepository.findReviewedByExaminer(examinerId).stream()
                .map(QuotationResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
