package org.utl.dsm503.utl;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.utl.dsm503.model.Nomina;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class PdfGenerator {

    public static String createPdf(Nomina nomina) throws FileNotFoundException {
        String filePath = "Nomina_" + System.currentTimeMillis() + ".pdf";
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            // Ajustar la forma de obtener el recurso
            InputStream logoStream = PdfGenerator.class.getResourceAsStream(".../imagenes/logo2.png");
            if (logoStream != null) {
                Image img = new Image(ImageDataFactory.create(logoStream.readAllBytes()));
                img.setWidth(100); // Ajusta el tamaño del logo según sea necesario
                img.setHeight(100);
                document.add(img);
            } else {
                System.err.println("Logo file not found.");
            }

            PdfFont bold = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont regular = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

            document.add(new Paragraph("RECIBO DE NÓMINA")
                    .setFont(bold)
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            headerTable.addCell(new Cell().add(new Paragraph("Empleador").setFont(bold).setFontSize(12).setTextAlignment(TextAlignment.CENTER)));
            headerTable.addCell(new Cell().add(new Paragraph("Empleado").setFont(bold).setFontSize(12).setTextAlignment(TextAlignment.CENTER)));

            headerTable.addCell(new Cell().add(new Paragraph("Empresa: Viña Del Mar Jr\nDirección: Boulevard Vicente Valtierra, C. Padre Tiziano Puppin 4301, San Miguel de Renteria Nte, 37256 León de los Aldama, Gto.").setFont(regular).setFontSize(10)));
            headerTable.addCell(new Cell().add(new Paragraph("Empleado: " + nomina.getNombreCompleto() + "\nCURP: " + nomina.getCurp()).setFont(regular).setFontSize(10)));

            document.add(headerTable);

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            table.addHeaderCell(new Cell().add(new Paragraph("Campo").setFont(bold).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER)));
            table.addHeaderCell(new Cell().add(new Paragraph("Detalle").setFont(bold).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER)));

            table.addCell(new Cell().add(new Paragraph("Sueldo Base").setFont(regular).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph("$" + String.format("%.2f", nomina.getSueldoBase())).setFont(regular).setFontSize(12)));

            table.addCell(new Cell().add(new Paragraph("IMSS").setFont(regular).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph("-$" + String.format("%.2f", nomina.getImss())).setFont(regular).setFontSize(12)));

            table.addCell(new Cell().add(new Paragraph("Retardos").setFont(regular).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph("-$" + String.format("%.2f", nomina.getRetardos())).setFont(regular).setFontSize(12)));

            table.addCell(new Cell().add(new Paragraph("Multas").setFont(regular).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph("-$" + String.format("%.2f", nomina.getMultas())).setFont(regular).setFontSize(12)));

            table.addCell(new Cell().add(new Paragraph("Platos Rotos").setFont(regular).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph("-$" + String.format("%.2f", nomina.getPlatosRotos())).setFont(regular).setFontSize(12)));

            table.addCell(new Cell().add(new Paragraph("Otros Descuentos").setFont(regular).setFontSize(12)));
            table.addCell(new Cell().add(new Paragraph("-$" + String.format("%.2f", nomina.getOtros())).setFont(regular).setFontSize(12)));

            table.addCell(new Cell().add(new Paragraph("Total a Pagar").setFont(bold).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY)));
            table.addCell(new Cell().add(new Paragraph("$" + String.format("%.2f", nomina.getTotalPagar())).setFont(bold).setFontSize(12).setBackgroundColor(ColorConstants.LIGHT_GRAY)));

            document.add(table);
        } catch (Exception e) {
            System.err.println("Error creating PDF: " + e.getMessage());
        }

        document.close();
        return filePath;
    }
}
