package com.pasmakms.demo.controller;


import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.services.BillingEntryNotesService;
import com.pasmakms.demo.services.BillingEntryService;
import com.pasmakms.demo.services.CandidateEntryService;
import com.pasmakms.demo.otherData.ListItem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

        Long candidateID = candidateEntries.get(0).getId();
        String DVNo = candidateEntries.get(0).getCandidateCheckDetails().getCandidateDvno();

        listItem = new ListItem();


        List<String> documentTypeList = listItem.getChecklist(billingEntry.getCategory(),billingEntry.getTypeOfScholar());

        List<String> myChecklist = listItem.getChecklist(billingEntry.getCategory(),billingEntry.getTypeOfScholar());
        model.addAttribute("billEntry", billingEntry);
        model.addAttribute("billNotes",billingEntryNotes);
        model.addAttribute("candidateLists", candidateEntries);
        model.addAttribute("myChecklist", documentTypeList);
        model.addAttribute("candidateID",candidateID);
        model.addAttribute("DVNo",DVNo);



        return "viewAuditBillingEntryDetail";

    }


    @RequestMapping("/forSignature")
    public String billForSignature(@RequestParam(name = "id") int id){

        BillingEntry billingEntry = billingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("For Signature");
        billingEntry.setBillStatus("For Signature");
        billingEntryService.save(billingEntry);


        return "redirect:/viewBillsForAudit";
    }

    @RequestMapping("/addDVNo")
    public String addDVNo(@RequestParam(name = "id") int id, Model model){

        CandidateEntry candidateEntry = candidateEntryService.get(id);

        BillingEntry billingEntry = billingEntryService.get(candidateEntry.getCandidateEnroll());

        model.addAttribute("billEntry",billingEntry);
        model.addAttribute("candidateEntry",candidateEntry);

        return "addDVNo";
    }

    @RequestMapping(value = "/saveDVNo", method = RequestMethod.POST)
    public String saveDVNo(@ModelAttribute CandidateEntry candidateEntry,@RequestParam(name = "id") int id, @RequestParam(name = "billId") int billId){
        CandidateEntry myCandidate = candidateEntryService.getCandidateById(id);
        myCandidate.getCandidateCheckDetails().setCandidateDvno(candidateEntry.getCandidateCheckDetails().getCandidateDvno());

        candidateEntryService.save(myCandidate);

        return "redirect:/viewAuditBillEntryDetail?id=" + billId;
    }



}

