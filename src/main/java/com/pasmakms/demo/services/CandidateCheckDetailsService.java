package com.pasmakms.demo.services;

import com.pasmakms.demo.domain.CandidateCheckDetails;
import com.pasmakms.demo.repositories.CandidateCheckDetailsRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CandidateCheckDetailsService {

    CandidateCheckDetailsRepository candidateCheckDetailsRepository;

    public CandidateCheckDetailsService(CandidateCheckDetailsRepository candidateCheckDetailsRepository) {
        this.candidateCheckDetailsRepository = candidateCheckDetailsRepository;
    }
    public List<CandidateCheckDetails> listAll(){
        return candidateCheckDetailsRepository.findAll();
    }

    public void save(CandidateCheckDetails candidateCheckDetails){
        candidateCheckDetailsRepository.save(candidateCheckDetails);
    }

    public CandidateCheckDetails get(long id){
        return candidateCheckDetailsRepository.findById(id).get();
    }

    public void delete(long id){
        candidateCheckDetailsRepository.deleteById(id);
    }
}
