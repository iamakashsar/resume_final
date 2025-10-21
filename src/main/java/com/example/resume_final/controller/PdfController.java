package com.example.resume_final.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadPdf(@RequestBody Map<String, String> resumeData) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLUE);
            Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font contentFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            // Render Profile Picture
            if(resumeData.containsKey("ProfilePic") && resumeData.get("ProfilePic") != null){
                String base64 = resumeData.get("ProfilePic");
                if(base64.startsWith("data:image")){
                    String[] parts = base64.split(",");
                    byte[] imgBytes = Base64.getDecoder().decode(parts[1]);
                    Image img = Image.getInstance(imgBytes);
                    img.scaleToFit(100,100);
                    img.setAlignment(Element.ALIGN_CENTER);
                    document.add(img);
                }
            }

            // Render Name as title
            if(resumeData.containsKey("Name")){
                Paragraph name = new Paragraph(resumeData.get("Name"), titleFont);
                name.setAlignment(Element.ALIGN_CENTER);
                name.setSpacingAfter(10f);
                document.add(name);
            }

            // Loop through resume sections
            for(Map.Entry<String,String> entry : resumeData.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                if(key.equals("Name") || key.equals("ProfilePic")) continue;

                Paragraph sectionTitle = new Paragraph(key, sectionFont);
                sectionTitle.setSpacingBefore(10f);
                sectionTitle.setSpacingAfter(5f);
                document.add(sectionTitle);

                for(String line : value.split("\\r?\\n")){
                    Paragraph content = new Paragraph(line, contentFont);
                    content.setSpacingAfter(2f);
                    document.add(content);
                }
            }

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "Resume.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(out.toByteArray());

        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
