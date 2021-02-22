package com.pasmakms.demo.controller;

import com.pasmakms.demo.domain.BillingEntry;
import com.pasmakms.demo.domain.BillingEntryNotes;
import com.pasmakms.demo.domain.CandidateEntry;
import com.pasmakms.demo.domain.UserAccount;
import com.pasmakms.demo.otherData.CandidateListCreation;
import com.pasmakms.demo.otherData.ListItem;
import com.pasmakms.demo.services.BillingEntryNotesService;
import com.pasmakms.demo.services.BillingEntryService;
import com.pasmakms.demo.services.CandidateEntryService;
import com.pasmakms.demo.services.UserAccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class josieController {

    private BillingEntryService billingEntryService;
    private CandidateEntryService candidateEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private UserAccountService usernameService;

    private ListItem listItem = new ListItem();

    public josieController(BillingEntryService billingEntryService, CandidateEntryService candidateEntryService, BillingEntryNotesService billingEntryNotesService, UserAccountService usernameService) {
        this.billingEntryService = billingEntryService;
        this.candidateEntryService = candidateEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
        this.usernameService = usernameService;
    }

    @RequestMapping("/verifyBillingEntry")
    public String viewVerifyBillingEntryPage(Model model, Principal principal){

        List<BillingEntry> billingEntries = billingEntryService.listAllForVerify();

        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        model.addAttribute("accountName",user);
        model.addAttribute("billingEntries",billingEntries);



        return "verifyBillingEntry";
    }

    @RequestMapping("verifyCandidates")
    public String viewVerifyBillingEntryDetailPage(Model model, @RequestParam(name = "BillId") int id,Principal principal){
        BillingEntry billingEntry = billingEntryService.get(id);
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);

        CandidateListCreation candidateListCreation = new CandidateListCreation(candidateEntries);



        List<String> contactBy = listItem.getContactedBy();
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);


        List<String> choices = Arrays.asList("Text", "Call");


        model.addAttribute("billingEntry",billingEntry);
        model.addAttribute("candidateEntries",candidateEntries);
        model.addAttribute("choiceList",choices);
        model.addAttribute("ContactedBy",contactBy);
        model.addAttribute("form",candidateListCreation);
        model.addAttribute("accountName",user);



        return "viewVerifyBillingEntryDetail";


    }

    @RequestMapping("/forAudit")
    public String updateBillStatus(@RequestParam(name = "id") int id, @ModelAttribute CandidateListCreation candidateListCreation, Model model) {
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);
        String contactedBy;
        String candidateFeedback;
        for(int i = 0; i < candidateEntries.size();i++)
        {
            candidateFeedback = candidateListCreation.getCandidateList().get(i).getCandidateFeedback();
            contactedBy = candidateListCreation.getCandidateList().get(i).getContactedBy();
            candidateEntries.get(i).setContactedBy(contactedBy);
            candidateEntries.get(i).setCandidateFeedback(candidateFeedback);
        }
        candidateEntryService.saveAll(candidateEntries);
       BillingEntry billingEntry = billingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("For Audit");
        billingEntry.setBillStatus("For Audit");
        billingEntryService.save(billingEntry);

        return "redirect:/verifyBillingEntry";
    }

}
