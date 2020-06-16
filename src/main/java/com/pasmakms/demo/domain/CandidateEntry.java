package com.pasmakms.demo.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class CandidateEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String candidateName;
    private String candidatePhone;
    private String candidateSgno;
    private int candidateEnroll;
    private String candidateFeedback;
    private String contactedBy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "candidate_id")
    private CandidateCheckDetails candidateCheckDetails;

}
