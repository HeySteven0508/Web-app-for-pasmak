package com.pasmakms.demo.services;

import com.pasmakms.demo.domain.BillingEntry;
import com.pasmakms.demo.domain.BillingEntryNotes;
import com.pasmakms.demo.repositories.BillingEntryNotesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingEntryNotesService {

   private BillingEntryNotesRepository billingEntryNotesRepository;

    public BillingEntryNotesService(BillingEntryNotesRepository billingEntryNotesRepository) {
        this.billingEntryNotesRepository = billingEntryNotesRepository;
    }

    public List<BillingEntryNotes> listAll(){
        return billingEntryNotesRepository.findAll();
    }

    public void save(BillingEntryNotes billingEntryNotes){
        billingEntryNotesRepository.save(billingEntryNotes);
    }

    public BillingEntryNotes get(long id){
        return billingEntryNotesRepository.findByBillingEntryId(id);
    }

    public void delete(long id){
        billingEntryNotesRepository.deleteById(id);
    }
}
