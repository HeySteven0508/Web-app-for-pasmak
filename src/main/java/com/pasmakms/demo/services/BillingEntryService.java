package com.pasmakms.demo.services;

import com.pasmakms.demo.domain.BillingEntry;
import com.pasmakms.demo.repositories.BillingEntryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingEntryService {
    private final BillingEntryRepository billingEntryRepository;

    public BillingEntryService(BillingEntryRepository billingEntryRepository) {
        this.billingEntryRepository = billingEntryRepository;
    }

    public List<BillingEntry> listAll(){
        return billingEntryRepository.findAll();
    }

    public List<BillingEntry>listAllNew(){
        return billingEntryRepository.findAllByBillStatus("New");
    }
    public List<BillingEntry>listAllForReview(){
        return billingEntryRepository.findAllByBillStatus("For Review");
    }
    public List<BillingEntry>listAllForAudit(){return billingEntryRepository.findAllByBillStatus("For Audit");}
    public List<BillingEntry>listAllForVerify(){
        return billingEntryRepository.findAllByBillStatus("For Verify");
    }

    public List<BillingEntry>listAllForPrepareChecks(){
        return billingEntryRepository.findAllByBillStatus("Prepare Checks");
    }
    public List<BillingEntry>ListAllForCheckIssuance(){
        return billingEntryRepository.findAllByBillStatus("For Issuance");
    }

    public List<BillingEntry>listAllReturn(){
        return billingEntryRepository.findAllByBillStatus("Returned Documents");
    }

    public void save(BillingEntry billingEntry){
        billingEntryRepository.save(billingEntry);
    }

    public BillingEntry get(long id){
        return billingEntryRepository.findById(id).get();
    }

    public void delete(long id){
        billingEntryRepository.deleteById(id);
    }

}
