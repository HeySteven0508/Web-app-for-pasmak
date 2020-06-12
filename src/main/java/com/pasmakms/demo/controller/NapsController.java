package com.pasmakms.demo.controller;

import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PreUpdate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class NapsController {

    private BillingEntryService BillingEntryService;
    private CandidateEntryService candidateEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private BillingEntryTaggingService billingEntryTaggingService;
    private BillingFeedbackService billingFeedbackService;
    private CandidateCheckDetailsService candidateCheckDetailsService;
    private ListItem listItem;

    public NapsController(com.pasmakms.demo.services.BillingEntryService billingEntryService,
                          CandidateEntryService candidateEntryService, BillingEntryNotesService billingEntryNotesService,
                          BillingEntryTaggingService billingEntryTaggingService, BillingFeedbackService billingFeedbackService,
                          CandidateCheckDetailsService candidateCheckDetailsService) {

        BillingEntryService = billingEntryService;
        this.candidateEntryService = candidateEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
        this.billingEntryTaggingService = billingEntryTaggingService;
        this.billingFeedbackService = billingFeedbackService;
        this.candidateCheckDetailsService = candidateCheckDetailsService;
    }

    // dashboard
    @RequestMapping("/dashboard")
    public String viewLoginPage(Model model){
        List<BillingEntry> newBill = BillingEntryService.listAll();
        List<BillingEntry> revBill = BillingEntryService.listAllForReview();
        List<BillingEntry> verBill = BillingEntryService.listAllForVerify();
        List<BillingEntry> audBill = BillingEntryService.listAllForAudit();

        int newSize = newBill.size();
        int revSize = revBill.size();
        int verSize = verBill.size();
        int audSize = audBill.size();

        model.addAttribute("newCount",newSize);
        model.addAttribute("revCount",revSize);
        model.addAttribute("verCount",verSize);
        model.addAttribute("audCount",audSize);


        return "index";
    }

    // display all list of billing Entry
    @RequestMapping("/viewBillingEntry")
    public String viewAllBillingEntryPage(Model model){
        List<BillingEntry> billingEntries = BillingEntryService.listAllNew();
        model.addAttribute("billingEntries", billingEntries);
        return "viewBillingEntry";
    }

    // view Billing Entry based on the billId
    @RequestMapping(path = "/viewBillEntry", method = RequestMethod.GET)
    public ModelAndView viewBillingDetailPage(@RequestParam(value = "id") int id){
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);

        BillingFeedback billingFeedback = new BillingFeedback();
        ModelAndView mav = new  ModelAndView("viewBillingEntryDetail");
        mav.addObject("billEntry",billingEntry);
        mav.addObject("candidateLists",candidateEntries);
        mav.addObject("billNotes",billingEntryNotes);
        mav.addObject("billingFeedback",billingFeedback);
        return mav;
    }

    // show Create Billing Entry Page
    @RequestMapping("/addBillingEntry")
    public String viewAddBillingEntryPage(Model model){
        BillingEntry billingEntry = new BillingEntry();

        listItem = new ListItem();
        List<String> centerList = listItem.getCenterList();
        List<String> qualificationList = listItem.getQualificationList();
        List<String> categoryList = listItem.getCategoryList();
        List<String> typeOfScholarshipList = listItem.getTypeOfScholarList();
        List<String> sourceOfFundsList = listItem.getSouceOfFundsList();

        model.addAttribute("centerList",centerList);
        model.addAttribute("qualificationList",qualificationList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("typeOfScholarshipList",typeOfScholarshipList);
        model.addAttribute("soureOfFundList", sourceOfFundsList);

        model.addAttribute("billingEntry",billingEntry);
        return "addBillingEntry";
    }

    // Successful create of Bill Entry
    @RequestMapping(value = "/addBillEntrySuccessful",method = RequestMethod.POST)
    public String addBillEntry(@ModelAttribute("billingEntry") BillingEntry billingEntry ){
        BillingEntryService.save(billingEntry);
        BillingEntryNotes billingEntryNotes = new BillingEntryNotes("New",billingEntry.getBillId());
        billingEntryNotesService.save(billingEntryNotes);
        BillingEntryTagging billingEntryTagging = new BillingEntryTagging(billingEntry.getBillId());
        billingEntryTaggingService.save(billingEntryTagging);


        return "redirect:/viewBillingEntry";
    }

    // Successful edit of Bill Entry
    @RequestMapping(value = "/editBillEntrySuccessful",method = RequestMethod.POST)
    public String editBillEntry(@ModelAttribute("billingEntry") BillingEntry billingEntry , @RequestParam(name = "id") int id){
       log.info(billingEntry.toString());
        BillingEntryService.save(billingEntry);
        return "redirect:/viewBillEntry?id=" + id;
    }


    // Successful create of Candidate Entry
    @RequestMapping(value = "/editCandidateEntrySuccessful",method = RequestMethod.POST)
    public String editCandidateEntry(@ModelAttribute("candidateEntry") CandidateEntry candidateEntry, @RequestParam(value="Billid") int id){

        candidateEntryService.save(candidateEntry);

        return "redirect:/viewBillEntry?id=" + id;
    }

    // Show Adding Candidate Page
    @RequestMapping("/addCandidateEntry")
    public String viewAddCandidateEntryPage(Model model, @RequestParam(value = "id") int id){

        BillingEntry billingEntry = BillingEntryService.get(id);
        CandidateEntry candidateEntry = new CandidateEntry();

        model.addAttribute("candidateEntry",candidateEntry);
        model.addAttribute("billingEntry",billingEntry);
        model.addAttribute("billId",id);
        return "addCandidateEntry";
    }


    // Successful create of Candidate Entry
    @RequestMapping(value = "/addCandidateEntrySuccessful",method = RequestMethod.POST)
    public String addCandidateEntry(@ModelAttribute("candidateEntry") CandidateEntry candidateEntry, @RequestParam(value="id") int id){

        candidateEntry.setCandidateCheckDetails(new CandidateCheckDetails());
        candidateEntryService.save(candidateEntry);


        return "redirect:/viewBillEntry?id=" + id;
    }


    // update the Bill Status from New to For Review
    @RequestMapping(path = "/forReview",method = RequestMethod.POST)
    public String updateBillStatus(@RequestParam(value = "id") int id,@ModelAttribute("billingFeedback") BillingFeedback billingFeedback){
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("for Review");
        billingEntry.setBillStatus("For Review");
        BillingEntryService.save(billingEntry);
        billingFeedback.setBillingId(id);
        billingFeedbackService.save(billingFeedback);

        return "redirect:/viewBillingEntry";
    }

    //delete the selected Billing entry with Billing Notes and Candidates enrolled in
    @RequestMapping("/deleteBillEntry")
    public String deleteBillingEntry(@RequestParam(value = "id") int id){
        BillingEntryService.delete(id);
        billingEntryNotesService.delete(id);
        Long candidateEntries = candidateEntryService.deleteByCandidateEnroll(id);
        billingEntryTaggingService.delete(id);

        return "redirect:/viewBillingEntry";
    }

    @RequestMapping("/editBillingEntry")
    public ModelAndView editBillingEntryPage(@RequestParam(value = "Billid") int id){
        ModelAndView mav = new ModelAndView("editBillingEntry");
        BillingEntry billingEntry = BillingEntryService.get(id);

        listItem = new ListItem();
        List<String> centerList = listItem.getCenterList();
        List<String> qualificationList = listItem.getQualificationList();
        List<String> categoryList = listItem.getCategoryList();
        List<String> typeOfScholarshipList = listItem.getTypeOfScholarList();
        List<String> sourceOfFundsList = listItem.getSouceOfFundsList();

        mav.addObject("centerList",centerList);
        mav.addObject("qualificationList",qualificationList);
        mav.addObject("categoryList",categoryList);
        mav.addObject("typeOfScholarshipList",typeOfScholarshipList);
        mav.addObject("billingEntry", billingEntry);
        mav.addObject("soureOfFundList", sourceOfFundsList);

        return mav;

    }

    @RequestMapping("/returnDocuments")
    public String returnBillingDocumentPage(Model model, @RequestParam(name = "id") int id){

        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingFeedback billingFeedback = new BillingFeedback();

        model.addAttribute("billingFeedback",billingFeedback);
        model.addAttribute("billEntry", billingEntry);

        return "returnDocuments";

    }

    @RequestMapping(value = "/returnSuccessfully",method = RequestMethod.POST)
    public String returnBillingSuccessfully(@ModelAttribute("billingFeedback") BillingFeedback billingFeedback, @RequestParam(name = "id") int id ){
        billingFeedbackService.save(billingFeedback);
        BillingEntry billingEntry = BillingEntryService.get(id);
        billingEntry.setBillStatus("Returned Documents");
        BillingEntryService.save(billingEntry);
        return "redirect:/dashboard";

    }

    @RequestMapping("viewAllBillingEntry")
    public String viewAllBillingList(Model model){

        List<BillingEntry> billingEntryList = BillingEntryService.listAll();
        List<BillingEntry> returnedBillingEntryList = BillingEntryService.listAllReturn();
        List<BillingEntryTagging> billingEntryTaggings = billingEntryTaggingService.listAll();

        model.addAttribute("returnBillings",returnedBillingEntryList);
        model.addAttribute("allBillings",billingEntryList);
        model.addAttribute("findByTag",billingEntryTaggings);
        return "viewAllBillingEntry";
    }

    @RequestMapping("editCandidate")
    public String editCandidate(Model model, @RequestParam(name = "id") int candId, @RequestParam(name="billId") int billId){
        BillingEntry billingEntry = BillingEntryService.get(billId);
        CandidateEntry candidateEntry = candidateEntryService.get(candId);

        model.addAttribute("billingEntry",billingEntry);
        model.addAttribute("candidateEntry",candidateEntry);

        return "editCandidateEntry";

    }

    @RequestMapping("deleteCandidate")
    public String deleteCandidate(Model model,@RequestParam("canId") int id, @RequestParam("billId") int billId){
        candidateEntryService.delete(id);
        return "redirect:/viewBillEntry?id=" + billId;
    }


    @RequestMapping("/viewMoreBillInfo")
    public String viewBillInfo(Model model, @RequestParam(name = "id") int id){

        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        List<BillingFeedback> billingFeedbacks = billingFeedbackService.listAlFeedbackByBillID(id);
        List<BillingFeedback> sample = new ArrayList<>();
        for(BillingFeedback feedback:billingFeedbacks){
            if(!feedback.getBillFeedback().equalsIgnoreCase("")){
                sample.add(feedback);
            }
            else{

            }
        }
        System.out.println(sample.toString());

        log.info(billingFeedbacks.toString());

        model.addAttribute("billEntry", billingEntry);
        model.addAttribute("billNotes",billingEntryNotes);
        model.addAttribute("billFeedback",billingFeedbacks);

        return "viewMoreInfoBilling";
    }









}
