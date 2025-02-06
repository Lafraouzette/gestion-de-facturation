package dev.kad.invoicemanagement.model.services;

import dev.kad.invoicemanagement.model.entities.Facture;
import dev.kad.invoicemanagement.model.entities.Paiement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PdfService {

    public void generateFacturePdf(Facture facture, String filePath) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Facture Reference: " + facture.getReference());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Date: " + facture.getDate());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Total: MAD" + facture.getTotal());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Status: " + facture.getStatus());

                // Add payment details
                Paiement paiement = facture.getPaiement();
                if (paiement != null) {
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Payment Details:");
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Mode: " + paiement.getMode());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Amount: MAD" + paiement.getMontant());
                    if (paiement.getBanque() != null) {
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.showText("Bank: " + paiement.getBanque());
                    }
                    if (paiement.getChequeRef() != null) {
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.showText("Cheque Reference: " + paiement.getChequeRef());
                    }
                }

                contentStream.endText();
            }

            document.save(filePath);
        }
    }
}