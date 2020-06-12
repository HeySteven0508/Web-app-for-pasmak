package com.pasmakms.demo.controller;

import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.services.BillingEntryNotesService;
import com.pasmakms.demo.services.BillingEntryService;
import com.pasmakms.demo.services.BillingEntryTaggingService;
import com.pasmakms.demo.services.CandidateEntryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class aljonController {

    private BillingEntryService BillingEntryService;
    private CandidateEntryService candidateEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private BillingEntryTaggingService billingEntryTaggingService;
    private ListItem listItem;


    public aljonController(com.pasmakms.demo.services.BillingEntryService billingEntryService, CandidateEntryService candidateEntryService, BillingEntryNotesService billingEntryNotesService, BillingEntryTaggingService billingEntryTaggingService) {
        BillingEntryService = billingEntryService;
        this.candidateEntryService = candidateEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
        this.billingEntryTaggingService = billingEntryTaggingService;
    }

    @RequestMapping("/reviewBillingEntry")
    public String viewLoginPage(Model model) {
        List<BillingEntry> billingEntries = BillingEntryService.listAllForReview();
        model.addAttribute("billingEntries", billingEntries);
        return "reviewBillingEntry";
    }

    @RequestMapping(path = "/viewReviewBillEntryDetail")
    public ModelAndView viewBillingDetailPage(@RequestParam(value = "id") int id) {
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        BillingEntryTagging billingEntryTagging = billingEntryTaggingService.get(id);


        String type = billingEntry.getCategory();
        String Category = billingEntry.getTypeOfScholar();

        listItem = new ListItem();

        List<String> myChecklist = listItem.getChecklist(type, Category);


        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);


        ModelAndView mav = new ModelAndView("viewReviewBillingEntryDetail");
        mav.addObject("billEntry", billingEntry);
        mav.addObject("candidateLists", candidateEntries);
        mav.addObject("billNotes", billingEntryNotes);
        mav.addObject("billEntryTag", billingEntryTagging);
        mav.addObject("myChecklist", myChecklist);
        return mav;
    }

    @RequestMapping("/addTaggingInfo")
    public String viewAddTaggingPage(Model model, @RequestParam(value = "id") int id) {

        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryTagging billingEntryTagging = billingEntryTaggingService.get(id);
        listItem = new ListItem();
        List<String> documentTypeList = listItem.getDocumentTypeList();
        model.addAttribute("billingEntryTag", billingEntryTagging);
        model.addAttribute("billingEntry", billingEntry);
        model.addAttribute("documentList", documentTypeList);
        model.addAttribute("billId", id);

        return "addTaggingInfo";
    }


    @RequestMapping("addBillingTagSuccessful")
    public String saveBillTagging(@RequestParam(name = "id") int id, @ModelAttribute("BillingEntryTagging") BillingEntryTagging billingEntryTagging) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String date = simpleDateFormat.format(new Date());
        String ID = date + "-" + id;
        billingEntryTagging.setBillingIdNo(ID);
        billingEntryTaggingService.save(billingEntryTagging);
        DateToday dateToday = new DateToday();
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.setBillChecked(dateToday.getDateToday());
        billingEntryNotesService.save(billingEntryNotes);

        return "redirect:/viewReviewBillEntryDetail?id=" + id;

    }

    // update the Bill Status from New to For Review
    @RequestMapping(path = "/forVerify")
    public String updateBillStatus(@RequestParam(value = "id") int id) {
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("for Verify");
        billingEntry.setBillStatus("For Verify");
        BillingEntryService.save(billingEntry);

        return "redirect:/reviewBillingEntry";


    }
}
