package com.pasmakms.demo.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
public class CandidateCheckDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private long candidateId;
    private String candidateDvno;
    private String candidateCheckno;
    private String candidateCheckamount;
    private String candidateTaxdeducted;
    private String checkReleased;

}
