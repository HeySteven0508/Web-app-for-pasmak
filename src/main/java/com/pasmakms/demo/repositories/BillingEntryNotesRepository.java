package com.pasmakms.demo.repositories;

import com.pasmakms.demo.domain.BillingEntry;
import com.pasmakms.demo.domain.BillingEntryNotes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillingEntryNotesRepository extends JpaRepository<BillingEntryNotes,Long> {

    BillingEntryNotes findByBillingEntryId(long id);
}
