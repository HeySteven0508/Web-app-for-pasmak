package com.pasmakms.demo.controller;

import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.otherData.DateToday;
import com.pasmakms.demo.services.BillingEntryNotesService;
import com.pasmakms.demo.services.BillingEntryService;
import com.pasmakms.demo.services.CandidateEntryService;
import com.pasmakms.demo.otherData.ListItem;
import com.pasmakms.demo.services.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class aljonController {

    private BillingEntryService BillingEntryService;
    private CandidateEntryService candidateEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private UserAccountService usernameService;
    private ListItem listItem;

    public aljonController(com.pasmakms.demo.services.BillingEntryService billingEntryService, CandidateEntryService candidateEntryService, BillingEntryNotesService billingEntryNotesService, UserAccountService usernameService) {
        BillingEntryService = billingEntryService;
        this.candidateEntryService = candidateEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
        this.usernameService = usernameService;
    }

    @RequestMapping("/reviewBillingEntry")
    public String viewLoginPage(Model model, Principal principal) {
        List<BillingEntry> billingEntries = BillingEntryService.listAllForReview();
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        model.addAttribute("accountName",user);
        model.addAttribute("billingEntries", billingEntries);
        return "reviewBillingEntry";
    }

    @RequestMapping(path = "/viewReviewBillEntryDetail")
    public ModelAndView viewBillingDetailPage(@RequestParam(value = "id") int id,Principal principal) {
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        listItem = new ListItem();

        List<String> myChecklist;
        String categoryBill;
        String typeOfScholar;

        ModelAndView mav = new ModelAndView("viewReviewBillingEntryDetail");

        if(billingEntry.getCategory() != null){
            categoryBill = billingEntry.getCategory();
            typeOfScholar=billingEntry.getTypeOfScholar();
            myChecklist = listItem.getChecklist(categoryBill,typeOfScholar);


        }
        else{
            myChecklist = Arrays.asList("Category Not Found");
        }

        List<String> category = listItem.getCategoryList();
        List<String> documentType = listItem.getDocumentTypeList();

        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        mav.addObject("accountName",user);



        mav.addObject("billEntry", billingEntry);
        mav.addObject("candidateLists", candidateEntries);
        mav.addObject("billNotes", billingEntryNotes);
        mav.addObject("category",category);
        mav.addObject("documentType",documentType);
        mav.addObject("myChecklist", myChecklist);

        return mav;
    }

    @RequestMapping("updateBillingDocuments")
    public String updateBillingDocuments(@RequestParam(name = "id")int id, @ModelAttribute("billEntry")BillingEntry billingEntry){

        BillingEntry getMyBillingEntry = BillingEntryService.get(id);
        getMyBillingEntry.setDocumentType(billingEntry.getDocumentType());
        getMyBillingEntry.setCategory(billingEntry.getCategory());
        BillingEntryService.save(getMyBillingEntry);

        
        return "redirect:/viewReviewBillEntryDetail?id=" + id;
    }


    @RequestMapping("addBillingTagSuccessful")
    public String saveBillTagging(@RequestParam(name = "id") int id, Model model) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String date = simpleDateFormat.format(new Date());
        String ID = date + "-" + id;

        DateToday dateToday = new DateToday();
        BillingEntry billingEntry = BillingEntryService.get(id);
        billingEntry.setBillingIdNo(ID);
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

    @RequestMapping("resetcategory")
    public String resetCategory(@RequestParam(name = "id") int id ){
        BillingEntry billingEntry = BillingEntryService.get(id);
        billingEntry.setCategory(null);
        billingEntry.setDocumentType(null);
        BillingEntryService.save(billingEntry);

        return "redirect:/viewReviewBillEntryDetail?id=" + id;
    }

    @RequestMapping("/forSignatureBilling")
    public String viewForSignatureBilling(Model model, Principal principal){
        List<BillingEntry> billingEntries = BillingEntryService.listAllForSignature();
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        model.addAttribute("accountName",user);


        model.addAttribute("billingEntries",billingEntries);

        return "viewForSignature";
    }
    @RequestMapping("/viewForSignatureDetail")
    public String viewForSignatureBillingDetail(Model model, @RequestParam(name = "id") int id, Principal principal){
        BillingEntry billingEntry = BillingEntryService.get(id);

        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        model.addAttribute("accountName",user);

        model.addAttribute("billEntry",billingEntry);

        return "viewForSignatureDetail";
    }

    @RequestMapping("/submitToPrepareChecks")
    public String submitBillToPrepareChecks(Model model, @RequestParam(name = "id") int id){
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("Prepare Checks");
        billingEntry.setBillStatus("Prepare Checks");
        BillingEntryService.save(billingEntry);

        return "redirect:/forSignatureBilling";
    }


    
}
