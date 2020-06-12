package com.pasmakms.demo.repositories;

import com.pasmakms.demo.domain.CandidateEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;

public interface CandidateEntryRepository extends JpaRepository<CandidateEntry,Long> {

    List<CandidateEntry> findAllByCandidateEnroll(int id);
    Long deleteAllByCandidateEnroll(int id);
    CandidateEntry findByCandidateName(String candidateName);
}
