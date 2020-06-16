package com.pasmakms.demo.controller;

import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
public class bethController {


    private BillingEntryService BillingEntryService;
    private CandidateEntryService candidateEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private CandidateCheckDetailsService candidateCheckDetailsService;

    public bethController(com.pasmakms.demo.services.BillingEntryService billingEntryService, CandidateEntryService candidateEntryService, BillingEntryNotesService billingEntryNotesService, CandidateCheckDetailsService candidateCheckDetailsService) {
        BillingEntryService = billingEntryService;
        this.candidateEntryService = candidateEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
        this.candidateCheckDetailsService = candidateCheckDetailsService;
    }

    @RequestMapping("/prepareChecks")
    public String viewAllPrepareChecks(Model model) {
        List<BillingEntry> billingEntries = BillingEntryService.listAllForPrepareChecks();
        model.addAttribute("billingEntries", billingEntries);
        return "viewPrepareChecks";
    }

    @RequestMapping("/viewPrepareChecksDetail")
    public String viewAllPrepareChecksDetail(Model model, @RequestParam(name = "id") int id) {
        BillingEntry billingEntry = BillingEntryService.get(id);
        List<CandidateEntry> candidateEntryList = candidateEntryService.findAllByCandidateEnrolled(id);

        model.addAttribute("candidateEntries",candidateEntryList);
        model.addAttribute("billEntry", billingEntry);


        return "viewPrepareChecksCreate";
    }

    @RequestMapping("/viewPrepareChecksView")
    public String viewAllPrepareChecksView(Model model, @RequestParam(name = "id") int id) {
        BillingEntry billingEntry = BillingEntryService.get(id);
        List<CandidateEntry> candidateEntryList = candidateEntryService.findAllByCandidateEnrolled(id);


        model.addAttribute("candidateEntries",candidateEntryList);
        model.addAttribute("billEntry", billingEntry);



        return "viewPrepareChecksView";
    }


    @RequestMapping("/viewCheckCreation")
    public String viewCreationOfCheckPage(Model model, @RequestParam(name = "candId") int candId, @RequestParam(name = "billId") int billId){
        CandidateCheckDetails candidateCheckDetails = candidateCheckDetailsService.get(candId);
        CandidateEntry candidateEntry = candidateEntryService.get(billId);
        BillingEntry billingEntry = BillingEntryService.get(candidateEntry.getCandidateEnroll());


        model.addAttribute("candidateEntry",candidateEntry);
        model.addAttribute("billingEntry",billingEntry);
        model.addAttribute("candidateCheckDetails",candidateCheckDetails);


        return "addCheck";
    }

    @RequestMapping("/addCheckforCandidate")
    public String viewCreationOfCheckPage(@ModelAttribute("candidateCheckDetail")CandidateCheckDetails candidateCheckDetails, @RequestParam(name = "id") int id){
        candidateCheckDetailsService.save(candidateCheckDetails);


        return "redirect:/viewPrepareChecksDetail?id=" + id;
    }

    @RequestMapping("/issuanceOfCheck")
    public String billStatusForIssuance(@RequestParam(name = "id") int id){
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("Check Issuance");
        billingEntry.setBillStatus("For Issuance");

        BillingEntryService.save(billingEntry);


        return "redirect:/prepareChecks";
    }

    @RequestMapping("releaseChecks")
    public String viewReleaseChecks(Model model){
       List<BillingEntry> billingEntry = BillingEntryService.ListAllForCheckIssuance();
       model.addAttribute("billingEntries",billingEntry);

        return "viewChecksIssuance";
    }

    @RequestMapping("viewIssuanceChecksView")
    public String viewReleaseChecksDetails(Model model,@RequestParam(name = "id") int id){
        BillingEntry billingEntry = BillingEntryService.get(id);
        List<CandidateEntry> candidateEntryList = candidateEntryService.findAllByCandidateEnrolled(id);


        model.addAttribute("candidateEntries",candidateEntryList);
        model.addAttribute("billEntry", billingEntry);

        return "viewIssuanceChecksView";
    }

    @RequestMapping("candidateReleaseCheck")
    public String updateCandidateRelease(Model model, @RequestParam(name = "id") int candidID, @RequestParam(name = "billId") int billId){
        CandidateCheckDetails candidateCheckDetails = candidateCheckDetailsService.get(candidID);

        // Create a date with a custom format and convert it into String
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("MMMM dd, YYYY");
        String dateCreated =  dateTimeFormat.format(localDateTime);

        candidateCheckDetails.setCheckReleased(dateCreated);
        candidateCheckDetailsService.save(candidateCheckDetails);

        return "redirect:/viewIssuanceChecksView?id=" + billId;

    }

    @RequestMapping("billingEntryFinish")
    public String declareBillingEntryFinish( @RequestParam(name = "id")int id){
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);

        billingEntry.setBillStatus("Completed");
        billingEntryNotes.addBillingNotes("Completed");
        BillingEntryService.save(billingEntry);


        return "redirect:/releaseChecks";
    }




}
