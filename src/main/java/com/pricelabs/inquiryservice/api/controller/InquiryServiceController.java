package com.pricelabs.inquiryservice.api.controller;


import com.pricelabs.inquiryservice.core.dto.Inquiry;
import com.pricelabs.inquiryservice.core.dto.InquiryResponse;
import com.pricelabs.inquiryservice.core.serp.SERPService;
import com.pricelabs.inquiryservice.core.services.IInquiryServiceImplentation;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class InquiryServiceController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    private final IInquiryServiceImplentation iInquiryServiceImplentation;

    public WritableWorkbook workbook;

    public InquiryServiceController(IInquiryServiceImplentation iInquiryServiceImplentation) {
        this.iInquiryServiceImplentation = iInquiryServiceImplentation;
    }

    //User will give inputs and this api will be called which will further only create an excel file
    @RequestMapping(value = "/inquiry", method = RequestMethod.POST)
    public ModelAndView fillPricePerInquiry(Inquiry inquiry) throws IOException, ParseException, WriteException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("excel_create_page");
        System.out.println(inquiry);
        List<InquiryResponse> inquiryResponses = SERPService.getInquiriesPrice(inquiry.getPageSize(), inquiry.getAddress());
        workbook= iInquiryServiceImplentation.createExcelFile(inquiryResponses);
        System.out.println("Created excel file");
        return modelAndView;
    }

    //User will be redirected after filling form and then this api will be called which will be responsible for
    //downloading/viewing an excel file in downloads/web folder
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadExcelFile(HttpServletResponse response) throws IOException {
        iInquiryServiceImplentation.downloadExcelFileService(workbook,response);
        System.out.println("Downloaded excel file");
    }
}
