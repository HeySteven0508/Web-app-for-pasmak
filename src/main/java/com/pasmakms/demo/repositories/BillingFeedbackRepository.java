package com.pasmakms.demo.repositories;

import com.pasmakms.demo.domain.BillingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingFeedbackRepository extends JpaRepository<BillingFeedback,Long> {

    List<BillingFeedback> findAllByBillingId(int billId);
}
