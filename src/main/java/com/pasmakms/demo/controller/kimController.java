package com.pasmakms.demo.controller;


import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.services.BillingEntryNotesService;
import com.pasmakms.demo.services.BillingEntryService;
import com.pasmakms.demo.services.BillingEntryTaggingService;
import com.pasmakms.demo.services.CandidateEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class kimController {

    private BillingEntryService billingEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private BillingEntryTaggingService billingEntryTaggingService;
    private CandidateEntryService candidateEntryService;

    private ListItem listItem;

    public kimController(BillingEntryService billingEntryService, BillingEntryNotesService billingEntryNotesService, BillingEntryTaggingService billingEntryTaggingService, CandidateEntryService candidateEntryService) {
        this.billingEntryService = billingEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
        this.billingEntryTaggingService = billingEntryTaggingService;
        this.candidateEntryService = candidateEntryService;
    }



    @RequestMapping("/viewBillsForAudit")
    public String auditBillingEntryPage(Model model){

        List<BillingEntry> billingEntryList = billingEntryService.listAllForAudit();
        model.addAttribute("billingEntries",billingEntryList);
        return "auditBillingEntry";
    }

    @RequestMapping(path = "/viewAuditBillEntryDetail")
    public String viewBillingDetailPage(@RequestParam(value = "id") int id, Model model) {

        BillingEntry billingEntry = billingEntryService.get(id);
        BillingEntryTagging billingEntryTagging = billingEntryTaggingService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get((id));
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);

        listItem = new ListItem();
        List<String> documentTypeList = listItem.getDocumentTypeList();

        model.addAttribute("billEntry", billingEntry);
        model.addAttribute("billEntryTag",billingEntryTagging);
        model.addAttribute("billNotes",billingEntryNotes);
        model.addAttribute("candidateLists", candidateEntries);
        model.addAttribute("myChecklist", documentTypeList);



        return "viewAuditBillingEntryDetail";

    }

    @RequestMapping("/submitToPrepareChecks")
    public String submitBillToPrepareChecks(Model model, @RequestParam(name = "id") int id){
        BillingEntry billingEntry = billingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("Prepare Checks");
        billingEntry.setBillStatus("Prepare Checks");
        billingEntryService.save(billingEntry);

        return "redirect:/viewBillsForAudit";
    }




}

