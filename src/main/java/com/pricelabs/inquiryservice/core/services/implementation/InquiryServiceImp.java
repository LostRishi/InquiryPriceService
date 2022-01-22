package com.pricelabs.inquiryservice.core.services.implementation;

import com.pricelabs.inquiryservice.core.dto.InquiryResponse;
import com.pricelabs.inquiryservice.core.services.IInquiryServiceImplentation;
import jxl.Workbook;
import jxl.write.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Service
public class InquiryServiceImp implements IInquiryServiceImplentation {

    final String fileBasePath = "src/main/java/com/pricelabs/inquiryservice/api/response/";

    @Override
    public WritableWorkbook createExcelFile(List<InquiryResponse> inquiryResponses){
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileBasePath + "First.xls"));
            WritableSheet sheet = workbook.createSheet("Sheet 1", 0);

            //Add core lojik
            WritableCell cell = new Label(0,0,"InquiryId");
            WritableCell cell1 = new Label(1,0,"Inquiry");
            WritableCell cell2 = new Label(2,0,"Price");

            sheet.addCell(cell);
            sheet.addCell(cell1);
            sheet.addCell(cell2);

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
    public void downloadExcelFileService(WritableWorkbook workbook) throws IOException {
        try {
            File file = new File(fileBasePath + "First.xls");
            HttpHeaders header = new HttpHeaders();
            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=First.xls");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
//            header.add("Expires", "0");

            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

            ResponseEntity.ok()
                    .headers(header)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }catch (IOException e){
            System.out.println("Downloading failed with an error message: " + e);
        }
    }

}
