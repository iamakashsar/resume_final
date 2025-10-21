package com.example.resume_final.controller;



import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.*;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin
public class ResumeController {

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadResume(@RequestBody String resumeContent) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter.getInstance(doc, out);
        doc.open();
        doc.add(new Paragraph(resumeContent));
        doc.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Resume.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }
}
