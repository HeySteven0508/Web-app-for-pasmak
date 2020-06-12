package com.pasmakms.demo.repositories;

import com.pasmakms.demo.domain.BillingEntryTagging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingEntryTaggingRepository  extends JpaRepository<BillingEntryTagging,Long> {
    BillingEntryTagging findByBillingEntryId (long id);
}
