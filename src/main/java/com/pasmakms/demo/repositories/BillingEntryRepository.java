package com.pasmakms.demo.repositories;

import com.pasmakms.demo.domain.BillingEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingEntryRepository extends JpaRepository<BillingEntry,Long> {
    List<BillingEntry> findAllByBillStatus (String status);
}
