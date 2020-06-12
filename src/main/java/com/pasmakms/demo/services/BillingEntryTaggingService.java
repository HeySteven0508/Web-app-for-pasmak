package com.pasmakms.demo.services;

import com.pasmakms.demo.domain.BillingEntry;
import com.pasmakms.demo.domain.BillingEntryTagging;
import com.pasmakms.demo.repositories.BillingEntryRepository;
import com.pasmakms.demo.repositories.BillingEntryTaggingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingEntryTaggingService {

    private final BillingEntryTaggingRepository billingEntryTaggingRepository;

    public BillingEntryTaggingService(BillingEntryTaggingRepository billingEntryTaggingRepository) {
        this.billingEntryTaggingRepository = billingEntryTaggingRepository;
    }

    public List<BillingEntryTagging> listAll(){
        return billingEntryTaggingRepository.findAll();
    }


    public void save(BillingEntryTagging billingEntryTagging){
        billingEntryTaggingRepository.save(billingEntryTagging);
    }

    public BillingEntryTagging get(long id){
        return billingEntryTaggingRepository.findByBillingEntryId(id);
    }

    public void delete(long id){
        billingEntryTaggingRepository.deleteById(id);
    }

}
