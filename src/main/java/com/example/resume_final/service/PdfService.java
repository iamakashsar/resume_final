package com.example.resume_final.service;



import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Simple PDF service using iText 5 to convert structured resume data into a PDF.
 * This keeps PDF generation server-side and returns the PDF bytes to controllers.
 *
 * NOTE: This is a plain-text / paragraph style PDF generator. If you want
 * richer HTML-to-PDF rendering, add an HTML-to-PDF library (wkhtmltopdf, flying-saucer, OpenHTMLtoPDF, etc.)
 */
@Service
public class PdfService {

    /**
     * Generate a simple PDF from a map representing resume sections.
     * Example usage:
     *   Map<String, String> sections = new LinkedHashMap<>();
     *   sections.put("Name", "Alice");
     *   sections.put("Objective", "To ...");
     *
     * @param orderedSections ordered map of section title -> content
     * @return PDF bytes
     * @throws Exception on generation error
     */
    public byte[] generateFromSections(List<Map.Entry<String, String>> orderedSections) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            for (Map.Entry<String, String> entry : orderedSections) {
                String title = entry.getKey();
                String content = entry.getValue();
                if (title != null && !title.isBlank()) {
                    document.add(new Paragraph(title));
                }
                if (content != null) {
                    // Add content as one or more paragraphs (split by newline)
                    for (String line : content.split("\\r?\\n")) {
                        document.add(new Paragraph("  " + line));
                    }
                }
                // add a little spacing between sections
                document.add(new Paragraph(" "));
            }
        } finally {
            if (document.isOpen()) document.close();
        }
        return out.toByteArray();
    }

    /**
     * Convenience method when you only have a single block of text (like previously used by the demo controller).
     */
    public byte[] generateFromPlainText(String plainText) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph(plainText == null ? "" : plainText));
        } finally {
            if (document.isOpen()) document.close();
        }
        return out.toByteArray();
    }
}
