package com.pasmakms.demo.services;

import com.pasmakms.demo.domain.BillingEntry;
import com.pasmakms.demo.domain.BillingFeedback;
import com.pasmakms.demo.repositories.BillingFeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingFeedbackService {

    private BillingFeedbackRepository billingFeedbackRepository;

    public BillingFeedbackService(BillingFeedbackRepository billingFeedbackRepository) {
        this.billingFeedbackRepository = billingFeedbackRepository;
    }

    public List<BillingFeedback> listAll(){
        return billingFeedbackRepository.findAll();
    }

    public List<BillingFeedback> listAlFeedbackByBillID(int billId){
        return billingFeedbackRepository.findAllByBillingId(billId);
    }


    public void save(BillingFeedback billingFeedback){
        billingFeedbackRepository.save(billingFeedback);
    }

    public BillingFeedback get(long id){
        return billingFeedbackRepository.findById(id).get();
    }

    public void delete(long id){
        billingFeedbackRepository.deleteById(id);
    }
}
