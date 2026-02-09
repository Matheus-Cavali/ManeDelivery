package unoeste.fipp.manedeliveryfx.reports;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import unoeste.fipp.manedeliveryfx.db.entidades.Pedido;

import java.awt.*;
import java.io.File;
import java.util.List;

public class PedidoReports {
    public static void relPedidos(List<Pedido> pedidoList){
        String dest = "pedidos.pdf";
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(dest);


            // Criando o documento  Pdf
            PdfDocument pdf = new PdfDocument(writer);

            // Criando o Document
            Document doc = new Document(pdf);

            //Definindo uma fonte grande
            PdfFont fonteTitulo = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            //Definindo um texto
            Text titulo = new Text("ManéDelivery");
            titulo.setFont(fonteTitulo);
            titulo.setFontSize(48);
            //Definindo um parágrafo
            Paragraph paragraph = new Paragraph(titulo);
            //Adicionando o parágrafo
            doc.add(paragraph);

            // Criando a tabela com 4 colunas
            float[] pointColumnWidths = {150F, 150F, 150F, 150F};
            Table table = new Table(pointColumnWidths);

            // Adicionando celulas na tabela
            table.addCell(new Cell().add("Cod Pedido").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Cliente").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Data").setBackgroundColor(Color.LIGHT_GRAY));
            table.addCell(new Cell().add("Total").setBackgroundColor(Color.LIGHT_GRAY));
            if (pedidoList != null) {
                for (Pedido p : pedidoList) {
                    // Ajuste esses getters de acordo com a sua entidade Pedido
                    table.addCell(new Paragraph(String.valueOf(p.getId())));   // ou getId(), etc.
                    table.addCell(new Paragraph(p.getNomeCliente()));         // se tiver cliente
                    table.addCell(new Paragraph(p.getData().toString()));           // formata depois bonitinho
                    table.addCell(new Paragraph(String.format("R$ %.2f", p.getTotal())));
                }
            }

            // Adicionando a tabela no documento
            doc.add(table);

            // Fechando o documento
            doc.close();
            System.out.println("documento criado..");

            //abrindo e mostrando o PDF
            Desktop.getDesktop().open(new File(dest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

