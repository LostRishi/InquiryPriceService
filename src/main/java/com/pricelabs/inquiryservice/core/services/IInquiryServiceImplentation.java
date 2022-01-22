package com.pricelabs.inquiryservice.core.services;

import com.pricelabs.inquiryservice.core.dto.InquiryResponse;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.List;

public interface IInquiryServiceImplentation {
    WritableWorkbook createExcelFile(List<InquiryResponse> inquiryResponses) throws IOException, WriteException;
    void downloadExcelFileService(WritableWorkbook workbook) throws IOException;
}
