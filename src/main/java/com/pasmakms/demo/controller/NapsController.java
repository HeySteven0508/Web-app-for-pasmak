package com.pasmakms.demo.controller;

import com.pasmakms.demo.domain.*;
import com.pasmakms.demo.otherData.DateToday;
import com.pasmakms.demo.otherData.feedbackHolder;
import com.pasmakms.demo.services.*;
import com.pasmakms.demo.otherData.ListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
public class NapsController {

    private BillingEntryService BillingEntryService;
    private CandidateEntryService candidateEntryService;
    private BillingEntryNotesService billingEntryNotesService;
    private BillingFeedbackService billingFeedbackService;
    private CandidateCheckDetailsService candidateCheckDetailsService;
    private UserAccountService usernameService;
    private ListItem listItem;

    public NapsController(BillingEntryService billingEntryService, CandidateEntryService candidateEntryService, BillingEntryNotesService billingEntryNotesService, BillingFeedbackService billingFeedbackService, CandidateCheckDetailsService candidateCheckDetailsService, UserAccountService usernameService) {
        BillingEntryService = billingEntryService;
        this.candidateEntryService = candidateEntryService;
        this.billingEntryNotesService = billingEntryNotesService;
        this.billingFeedbackService = billingFeedbackService;
        this.candidateCheckDetailsService = candidateCheckDetailsService;
        this.usernameService = usernameService;
    }

    // dashboard
    @RequestMapping("/dashboard")
    public String viewDashboard(Model model, Principal principal){


        List<BillingEntry> newBill = BillingEntryService.listAll();
        List<BillingEntry> revBill = BillingEntryService.listAllForReview();
        List<BillingEntry> verBill = BillingEntryService.listAllForVerify();
        List<BillingEntry> audBill = BillingEntryService.listAllForAudit();
        List<BillingEntry> signBill = BillingEntryService.listAllForSignature();
        List<BillingEntry> prepareCheck = BillingEntryService.listAllForPrepareChecks();
        List<BillingEntry> issueCheck = BillingEntryService.listAllForCheckIssuance();
        List<BillingEntry> completed = BillingEntryService.listAllForCompleted();
        List<BillingEntry> findings = BillingEntryService.listAllReturn();


        //for percentage
        int accomplishPercent;

        //for total number
        int totalRevCount = 0; // Total No. of Reviewed bills
        int totalVerCount = 0;// Total No. of Verified bills
        int totalAudCount = 0;// Total No. of Audited bills
        int totalSigCount = 0;// Total No. of Signed bills
        int totalPreCount = 0;// Total No. of Prepared checks
        int totalIssCount = 0;// Total No. of Issued checks



        // counting the total numbers
        List<BillingEntryNotes> billingEntryNotesList = billingEntryNotesService.listAll();

        for(int i = 0; i<billingEntryNotesList.size(); i++){
            if(billingEntryNotesList.get(i).getBillChecked() != null){
                totalRevCount++;
            }
            if(billingEntryNotesList.get(i).getBillVerified() != null){
                totalVerCount++;
            }
            if(billingEntryNotesList.get(i).getBillAudited() != null){
                totalAudCount++;
            }
            if(billingEntryNotesList.get(i).getBillDocControl() != null){
                totalSigCount++;
            }
            if(billingEntryNotesList.get(i).getBillCheckPrepare() != null){
                totalPreCount++;
            }
            if(billingEntryNotesList.get(i).getBillCheckRelease() != null){
                totalIssCount++;
            }

        }



        // Pending
        int newSize = newBill.size();
        int revSize = revBill.size();
        int verSize = verBill.size();
        int audSize = audBill.size();
        int sigSize = signBill.size();
        int prepSize = prepareCheck.size();
        int issueSize = issueCheck.size();
        int compSize = completed.size();
        int withFindings = findings.size();

        // Total Completed
        accomplishPercent = (int)(((double)compSize/newSize) * 100);


        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);



        model.addAttribute("accountName",user);
        model.addAttribute("newCount",newSize);
        model.addAttribute("revCount",revSize);
        model.addAttribute("verCount",verSize);
        model.addAttribute("audCount",audSize);
        model.addAttribute("sigCount",sigSize);
        model.addAttribute("prepCount", prepSize);
        model.addAttribute("issueCount",issueSize);
        model.addAttribute("compSize",compSize);
        model.addAttribute("findings",withFindings);
        model.addAttribute("accompPercent",accomplishPercent);

        model.addAttribute("totalRevCount",totalRevCount);
        model.addAttribute("totalVerCount",totalVerCount);
        model.addAttribute("totalAudCount",totalAudCount);
        model.addAttribute("totalSigCount",totalSigCount);
        model.addAttribute("totalPreCount",totalPreCount);
        model.addAttribute("totalIssCount",totalIssCount);

        model.addAttribute("accountName",user);




        return "index";
    }

    // display all list of billing Entry
    @RequestMapping("/viewBillingEntry")
    public String viewAllBillingEntryPage(Model model, Principal principal){
        List<BillingEntry> billingEntries = BillingEntryService.listAllNew();
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);



        model.addAttribute("accountName",user);
        model.addAttribute("billingEntries", billingEntries);
        return "viewBillingEntry";
    }

    // view Billing Entry based on the billId
    @RequestMapping(path = "/viewBillEntry", method = RequestMethod.GET)
    public ModelAndView viewBillingDetailPage(@RequestParam(value = "id") int id, Principal principal){
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        List<CandidateEntry> candidateEntries = candidateEntryService.findAllByCandidateEnrolled(id);

        BillingFeedback billingFeedback = new BillingFeedback();

        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        ModelAndView mav = new  ModelAndView("viewBillingEntryDetail");
        mav.addObject("accountName",user);
        mav.addObject("billEntry",billingEntry);
        mav.addObject("candidateLists",candidateEntries);
        mav.addObject("billNotes",billingEntryNotes);
        mav.addObject("billingFeedback",billingFeedback);
        return mav;
    }

    // show Create Billing Entry Page
    @RequestMapping("/addBillingEntry")
    public String viewAddBillingEntryPage(Model model,Principal principal){
        BillingEntry billingEntry = new BillingEntry();

        listItem = new ListItem();
        List<String> centerList = listItem.getCenterList();
        List<String> qualificationList = listItem.getQualificationList();
        List<String> categoryList = listItem.getCategoryList();
        List<String> typeOfScholarshipList = listItem.getTypeOfScholarList();
        List<String> sourceOfFundsList = listItem.getSouceOfFundsList();
        List<String> documentTypeList = listItem.getDocumentTypeList();

        model.addAttribute("documentList", documentTypeList);
        model.addAttribute("centerList",centerList);
        model.addAttribute("qualificationList",qualificationList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("typeOfScholarshipList",typeOfScholarshipList);
        model.addAttribute("soureOfFundList", sourceOfFundsList);
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);



        model.addAttribute("accountName",user);

        model.addAttribute("billingEntry",billingEntry);
        return "addBillingEntry";
    }

    // Successful create of Bill Entry
    @RequestMapping(value = "/addBillEntrySuccessful",method = RequestMethod.POST)
    public String addBillEntry(@ModelAttribute("billingEntry") BillingEntry billingEntry ){
        BillingEntryService.save(billingEntry);
        BillingEntryNotes billingEntryNotes = new BillingEntryNotes("New",billingEntry.getBillId());
        billingEntryNotesService.save(billingEntryNotes);

        return "redirect:/viewBillingEntry";
    }

    // Successful edit of Bill Entry
    @RequestMapping(value = "/editBillEntrySuccessful",method = RequestMethod.POST)
    public String editBillEntry(@ModelAttribute("billingEntry") BillingEntry billingEntry , @RequestParam(name = "id") int id){
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
    public String viewAddCandidateEntryPage(Model model, @RequestParam(value = "id") int id,Principal principal){

        BillingEntry billingEntry = BillingEntryService.get(id);
        CandidateEntry candidateEntry = new CandidateEntry();
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        model.addAttribute("accountName",user);
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
    @RequestMapping(path = "/forReview")
    public String updateBillStatus(@RequestParam(value = "id") int id){
        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingEntryNotes billingEntryNotes = billingEntryNotesService.get(id);
        billingEntryNotes.addBillingNotes("for Review");
        billingEntry.setBillStatus("For Review");
        BillingEntryService.save(billingEntry);

        return "redirect:/viewBillingEntry";
    }

    //delete the selected Billing entry with Billing Notes and Candidates enrolled in
    @RequestMapping("/deleteBillEntry")
    public String deleteBillingEntry(@RequestParam(value = "id") int id){
        BillingEntryService.delete(id);
        billingEntryNotesService.delete(id);
        Long candidateEntries = candidateEntryService.deleteByCandidateEnroll(id);

        return "redirect:/viewBillingEntry";
    }

    @RequestMapping("/editBillingEntry")
    public ModelAndView editBillingEntryPage(@RequestParam(value = "Billid") int id,Principal principal){
        ModelAndView mav = new ModelAndView("editBillingEntry");
        BillingEntry billingEntry = BillingEntryService.get(id);

        listItem = new ListItem();
        List<String> centerList = listItem.getCenterList();
        List<String> qualificationList = listItem.getQualificationList();
        List<String> categoryList = listItem.getCategoryList();
        List<String> typeOfScholarshipList = listItem.getTypeOfScholarList();
        List<String> sourceOfFundsList = listItem.getSouceOfFundsList();
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);


        mav.addObject("accountName",user);
        mav.addObject("centerList",centerList);
        mav.addObject("qualificationList",qualificationList);
        mav.addObject("categoryList",categoryList);
        mav.addObject("typeOfScholarshipList",typeOfScholarshipList);
        mav.addObject("billingEntry", billingEntry);
        mav.addObject("soureOfFundList", sourceOfFundsList);

        return mav;

    }

    @RequestMapping("/returnDocuments")
    public String returnBillingDocumentPage(Model model, @RequestParam(name = "id") int id, Principal principal){

        BillingEntry billingEntry = BillingEntryService.get(id);
        BillingFeedback billingFeedback = new BillingFeedback();
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        model.addAttribute("accountName",user);



        model.addAttribute("billingFeedback",billingFeedback);
        model.addAttribute("billEntry", billingEntry);

        return "returnDocuments";

    }

    @RequestMapping(value = "/returnSuccessfully",method = RequestMethod.POST)
    public String returnBillingSuccessfully(Principal principal,@ModelAttribute("billingFeedback") BillingFeedback billingFeedback, @RequestParam(name = "id") int id ){
        DateToday dateToday = new DateToday();
        try{
            billingFeedback.setBillDate(dateToday.getCurrentDateToday());
            String name = principal.getName();
            UserAccount user = usernameService.getUserAccount(name);
            billingFeedback.setUser(user.getUsername());

        }catch (Exception ex){
            ex.printStackTrace();
        }

        billingFeedback.setBillRemarks("Add Findings");

        billingFeedbackService.save(billingFeedback);
        BillingEntry billingEntry = BillingEntryService.get(id);
        billingEntry.setBillStatus("with Findings");
        BillingEntryService.save(billingEntry);
        return "redirect:/dashboard";

    }

    @RequestMapping("viewAllBillingEntry")
    public String viewAllBillingList(Model model,Principal principal){

        List<BillingEntry> billingEntryList = BillingEntryService.listAll();
        List<BillingEntry> returnedBillingEntryList = BillingEntryService.listAllReturn();
        String name = principal.getName();
        UserAccount user = usernameService.getUserAccount(name);

        model.addAttribute("accountName",user);
        model.addAttribute("returnBillings",returnedBillingEntryList);
        model.addAttribute("allBillings",billingEntryList);
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
    public String viewBillInfo(Model model, @RequestParam(name = "id") int id, Principal principal){

        Date date;
        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
        String name = principal.getName();
        UserAccount myUser = usernameService.getUserAccount(name);

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

        List<feedbackHolder> feedbackHolders = new ArrayList<>(sample.size());
        String feedBackDate,user,feedback,billRemarks;

        for (int i =0;i<sample.size();i++)
        {
            date = sample.get(i).getBillDate();
            feedBackDate = outputFormat.format(sample.get(i).getBillDate());
            user = name;
            feedback = sample.get(i).getBillFeedback();
            billRemarks = sample.get(i).getBillRemarks();

            feedbackHolders.add(new feedbackHolder(feedBackDate,user,feedback,billRemarks));

        }

        model.addAttribute("accountName",myUser);


        model.addAttribute("billEntry", billingEntry);
        model.addAttribute("billNotes",billingEntryNotes);
        model.addAttribute("billFeedback",feedbackHolders);

        return "viewMoreInfoBilling";
    }



    @RequestMapping("/viewMoreBillInfoForFinding")
    public String viewBillInfoForFindings(Model model, @RequestParam(name = "id") int id , Principal principal){
        listItem = new ListItem();
        Date date;
        DateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");

        String name = principal.getName();
        UserAccount myUser = usernameService.getUserAccount(name);

        BillingEntry dummyBillingEntry = new BillingEntry();

        BillingEntry billingEntry = BillingEntryService.get(id);
        List<String> billStatusList =listItem.getBillStatusList();
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

        List<feedbackHolder> feedbackHolders = new ArrayList<>(sample.size());
        String feedBackDate,user,feedback,billRemarks;

        for (int i =0;i<sample.size();i++)
        {
            date = sample.get(i).getBillDate();
            feedBackDate = outputFormat.format(sample.get(i).getBillDate());
            user = myUser.getAccountName();
            feedback = sample.get(i).getBillFeedback();
            billRemarks = sample.get(i).getBillRemarks();

            feedbackHolders.add(new feedbackHolder(feedBackDate,user,feedback,billRemarks));

        }
        model.addAttribute("myBillEntry",dummyBillingEntry);
        model.addAttribute("billStatus",billStatusList);
        model.addAttribute("billEntry", billingEntry);
        model.addAttribute("billNotes",billingEntryNotes);
        model.addAttribute("billFeedback",feedbackHolders);
        model.addAttribute("accountName",myUser);

        return "viewMoreInfoBillingForFindings";
    }

    @RequestMapping(value = "/changeBillStatus",method = RequestMethod.POST)
    public String updateBillStatus(@RequestParam(name = "id") int Billid, @ModelAttribute BillingEntry billingEntry){


        DateToday dateToday = new DateToday();
        List<BillingFeedback> billingFeedback = billingFeedbackService.listAlFeedbackByBillID(Billid);

        BillingFeedback getIDBillingFeedback = billingFeedbackService.get(billingFeedback.get(0).getId());
        BillingFeedback addBillingFeedback = new BillingFeedback(Billid,
                                                        "Removed Findings and set the bill to '" + billingEntry.getBillStatus() +"' ",
                                                        dateToday.getCurrentDateToday(),
                                                        "user",
                                                         "Complied");


        BillingEntry myBillingEntry = BillingEntryService.get(Billid);
        myBillingEntry.setBillStatus(billingEntry.getBillStatus());
        BillingEntryService.save(myBillingEntry);
        billingFeedbackService.save(addBillingFeedback);

        return "redirect:/viewAllBillingEntry";
    }









}
