package com.pasmakms.demo.controller;

import com.pasmakms.demo.domain.BillingEntry;
import com.pasmakms.demo.domain.BillingEntryNotes;
import com.pasmakms.demo.domain.BillingEntryTagging;
import com.pasmakms.demo.domain.CandidateEntry;
import com.pasmakms.demo.services.BillingEntryNotesService;
import com.pasmakms.demo.services.BillingEntryService;
import com.pasmakms.demo.services.BillingEntryTaggingService;
import com.pasmakms.demo.services.CandidateEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class josieController {

    private BillingEntryService billingEntryService;
    private CandidateEntryService candidateEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private BillingEntryTaggingService billingEntryTaggingService;

    public josieController(com.pasmakms.demo.services.BillingEntryService billingEntryService, CandidateEntryService candidateEntryService, BillingEntryNotesService billingEntryNotesService, BillingEntryTaggingService billingEntryTaggingService) {
        this.billingEntryService = billingEntryService;
        this.candidateEntryService = candidateEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
        this.billingEntryTaggingService = billingEntryTaggingService;
    }

    @RequestMapping("/verifyBillingEntry")
    public String viewVerifyBillingEntryPage(Model model){

        List<BillingEntry> billingEntries = billingEntryService.listAllForVerify();
        model.addAttribute("billingEntries",billingEntries);


        return "verifyBillingEntry";
    }

    @RequestMapping("verifyCandidates")
    public String viewVerifyBillingEntryDetailPage(Model model, @RequestParam(name = "BillId") int id){
        BillingEntry billingEntry = billingEntryService.get(id);
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);
        BillingEntryTagging billingEntryTaggings = billingEntryTaggingService.get(id);

        List<String> choices = Arrays.asList("Text", "Call");
        model.addAttribute("billingEntry",billingEntry);
        model.addAttribute("candidateEntries",candidateEntries);
        model.addAttribute("billEntryTag",billingEntryTaggings);
        model.addAttribute("choiceList",choices);


        return "viewVerifyBillingEntryDetail";


    }

    @RequestMapping("/forAudit")
    public String updateBillStatus(@RequestParam(name = "id") int id){
        BillingEntry billingEntry = billingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("For Audit");
        billingEntry.setBillStatus("For Audit");
        billingEntryService.save(billingEntry);

        return "redirect:/verifyBillingEntry";
    }

}
