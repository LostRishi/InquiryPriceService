package com.pricelabs.inquiryservice.core.services.implementation;

import com.pricelabs.inquiryservice.core.dto.InquiryResponse;
import com.pricelabs.inquiryservice.core.services.IInquiryServiceImplentation;
import jxl.Workbook;
import jxl.write.*;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


@Service
public class InquiryServiceImp implements IInquiryServiceImplentation {

    final String fileBasePath = "src/main/java/com/pricelabs/inquiryservice/api/response/";
    final String fileName = "Inquiry.xls";
    LocalDate todaysDate = LocalDate.now();

    @Override
    public WritableWorkbook createExcelFile(List<InquiryResponse> inquiryResponses){
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileBasePath + fileName));
            WritableSheet sheet = workbook.createSheet("Sheet 1", 0);

            //TODO: Add core logic
            WritableCell cell = new Label(0,0,"InquiryId");
            sheet.addCell(cell);

            WritableCell cell1 = new Label(1,0,"Inquiry");
            sheet.addCell(cell1);

            for(int day=0;day<365;day++){
                int currDay = day+1;
                LocalDate date = todaysDate.plusDays(day);
                String nextDay = date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear();
                WritableCell cell2 = new Label(2+day,0,nextDay);
                sheet.addCell(cell2);
            }
            workbook.write();
            workbook.close();
            return  workbook;
        }catch (IOException | WriteException e) {
            System.out.println("Creating excel file failed with an error message: " + e);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void downloadExcelFileService(WritableWorkbook workbook, HttpServletResponse response) throws IOException {
        File file = new File(fileBasePath + fileName);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" +fileName);
        response.setHeader("Content-Transfer-Encoding", "binary");

        try {
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(fileBasePath+fileName);
            int len;
            byte[] buf = new byte[1024];
            while((len = fis.read(buf)) > 0) {
                bos.write(buf,0,len);
            }
            bos.close();
            response.flushBuffer();
        }
        catch(IOException e) {
            System.out.println("Downloading failed with an error message: " + e);
            e.printStackTrace();
        }
    }

}
