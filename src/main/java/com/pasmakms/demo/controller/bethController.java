package com.pasmakms.demo.controller;

import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        CandidateCheckDetails candidateCheckDetails = new CandidateCheckDetails();

        model.addAttribute("candidateEntries",candidateEntryList);
        model.addAttribute("billEntry", billingEntry);
        model.addAttribute("candidateCheckDetail",candidateCheckDetails);


        return "viewPrepareChecksCreate";
    }

    @RequestMapping("/viewPrepareChecksView")
    public String viewAllPrepareChecksView(Model model, @RequestParam(name = "id") int id) {
        BillingEntry billingEntry = BillingEntryService.get(id);
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);


        model.addAttribute("candidateEntries",candidateEntries);
        model.addAttribute("candidateEntry",candidateEntries.get(0));
        model.addAttribute("billEntry", billingEntry);



        return "viewPrepareChecksView";
    }

    @RequestMapping(value = "/createBillCheck",method = RequestMethod.POST)
    public String createBill(@ModelAttribute CandidateCheckDetails candidateCheckDetails, @RequestParam(name = "id") int id){
        CandidateCheckDetails myCandidateCheckDetails = candidateCheckDetails;
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);

        for(int i = 0; i < candidateEntries.size(); i++){
            candidateEntries.get(i).getCandidateCheckDetails().setCandidateCheckno(myCandidateCheckDetails.getCandidateCheckno());
            candidateEntries.get(i).getCandidateCheckDetails().setCandidateCheckamount(myCandidateCheckDetails.getCandidateCheckamount());
            candidateEntries.get(i).getCandidateCheckDetails().setCandidateTaxdeducted(myCandidateCheckDetails.getCandidateTaxdeducted());

        }
        candidateEntryService.saveAll(candidateEntries);

        return "redirect:/prepareChecks";
    }


    @RequestMapping("/viewCheckCreation")
    public String viewCreationOfCheckPage(Model model, @RequestParam(name = "candId") int candId, @RequestParam(name = "billId") int billId){
        CandidateEntry candidateEntry = candidateEntryService.get(billId);
        CandidateCheckDetails candidateCheckDetails = candidateCheckDetailsService.get(candId);
        BillingEntry billingEntry = BillingEntryService.get(candidateEntry.getCandidateEnroll());

        System.out.println(candidateCheckDetails);
        System.out.println(candidateEntry.getCandidateCheckDetails().getCandidateDvno());


        model.addAttribute("candidateEntry",candidateEntry);
        model.addAttribute("billingEntry",billingEntry);
        model.addAttribute("candidateCheckDetails",candidateCheckDetails);


        return "addCheck";
    }

    @RequestMapping("/addCheckforCandidate")
    public String viewCreationOfCheckPage(@ModelAttribute("candidateCheckDetail")CandidateCheckDetails candidateCheckDetails, @RequestParam(name = "id") int id, @RequestParam(name="candId") int candId){

        CandidateCheckDetails myCandidateCheckDetails = candidateCheckDetailsService.get(candId);
        myCandidateCheckDetails.setCandidateCheckno(candidateCheckDetails.getCandidateCheckno());
        myCandidateCheckDetails.setCandidateCheckamount(candidateCheckDetails.getCandidateCheckamount());

        candidateCheckDetailsService.save(myCandidateCheckDetails);


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
       List<BillingEntry> billingEntry = BillingEntryService.listAllForCheckIssuance();
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
