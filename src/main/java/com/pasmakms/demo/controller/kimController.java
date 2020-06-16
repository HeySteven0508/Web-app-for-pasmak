package com.pasmakms.demo.controller;


import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.services.BillingEntryNotesService;
import com.pasmakms.demo.services.BillingEntryService;
import com.pasmakms.demo.services.CandidateEntryService;
import com.pasmakms.demo.otherData.ListItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class kimController {

    private BillingEntryService billingEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private CandidateEntryService candidateEntryService;

    private ListItem listItem;

    public kimController(BillingEntryService billingEntryService, BillingEntryNotesService billingEntryNotesService, CandidateEntryService candidateEntryService) {
        this.billingEntryService = billingEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
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
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get((id));
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);

        listItem = new ListItem();
        List<String> documentTypeList = listItem.getDocumentTypeList();

        model.addAttribute("billEntry", billingEntry);
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
