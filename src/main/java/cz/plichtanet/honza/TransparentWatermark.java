/**
 * This example was written by Bruno Lowagie in answer to a question by a customer.
 */
package cz.plichtanet.honza;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.*;

import java.io.*;

public class TransparentWatermark {

    public static void manipulatePdf(InputStream src, OutputStream dst, String text) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, dst);
        Font f = new Font(FontFamily.HELVETICA, 15);
        Phrase p = new Phrase(text, f);
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.5f);
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfContentByte over = stamper.getOverContent(i);
            over.saveState();
            over.setGState(gs1);
            ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 297, 450, 0);
            over.restoreState();
        }
        stamper.close();
        reader.close();
    }
}