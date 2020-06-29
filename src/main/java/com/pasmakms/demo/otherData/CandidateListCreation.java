package com.pasmakms.demo.otherData;

import com.pasmakms.demo.domain.CandidateEntry;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class CandidateListCreation {
    private List<CandidateEntry>  candidateList;



    public CandidateListCreation(){

    }

    public CandidateListCreation(List<CandidateEntry> candidateList) {
        this.candidateList = candidateList;
    }

    public void addCandidate(CandidateEntry candidateEntry){
        this.candidateList.add(candidateEntry);
    }

    public List<CandidateEntry> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(List<CandidateEntry> candidateList) {
        this.candidateList = candidateList;
    }

    @Override
    public String toString() {
        return "CandidateListCreation{" +
                "candidateList=" + candidateList +
                '}';
    }
}
