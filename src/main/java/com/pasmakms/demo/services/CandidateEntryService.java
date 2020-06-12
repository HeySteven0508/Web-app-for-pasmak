package com.pasmakms.demo.services;

import com.pasmakms.demo.domain.BillingEntry;
import com.pasmakms.demo.domain.CandidateEntry;
import com.pasmakms.demo.repositories.CandidateEntryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CandidateEntryService {
    CandidateEntryRepository candidateEntryRepository;

    public CandidateEntryService(CandidateEntryRepository candidateEntryRepository) {
        this.candidateEntryRepository = candidateEntryRepository;
    }

    public CandidateEntry getCandidateById(long id){
        return candidateEntryRepository.findById(id).get();
    }

    public List<CandidateEntry> listAll(){
        return candidateEntryRepository.findAll();
    }

    public List<CandidateEntry> findAllByCandidateEnrolled(int id){
        return candidateEntryRepository.findAllByCandidateEnroll(id);
    }

    public CandidateEntry findByCandidateName(String username){
      return candidateEntryRepository.findByCandidateName(username);
    }


    public void save(CandidateEntry candidateEntry){
        candidateEntryRepository.save(candidateEntry);
    }

    public CandidateEntry get(long id){
        return candidateEntryRepository.findById(id).get();
    }

    public void delete(long id){
        candidateEntryRepository.deleteById(id);
    }

    @Transactional
    public Long deleteByCandidateEnroll(int id){
        return  candidateEntryRepository.deleteAllByCandidateEnroll(id);

    }
}

