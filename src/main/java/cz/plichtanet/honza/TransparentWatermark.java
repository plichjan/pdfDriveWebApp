/**
 * This example was written by Bruno Lowagie in answer to a question by a customer.
 */
package cz.plichtanet.honza;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TransparentWatermark {

    private static final int MAX_ROW = 20;

    public static void manipulatePdf(InputStream src, OutputStream dst, String text) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, dst);
        Font f = new Font(FontFamily.HELVETICA, 30, Font.NORMAL, BaseColor.YELLOW);
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.3f);
        String[] split = text.split("\\n");
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            PdfContentByte over = stamper.getOverContent(i);
            over.saveState();
            over.setGState(gs1);
            Rectangle pageSize = reader.getPageSize(i);
            float x = pageSize.getWidth() / 2;
            for (int row = 0; row < MAX_ROW; row++) {
                float y = pageSize.getHeight() / (MAX_ROW + 2) * (row + 1);
                ColumnText.showTextAligned(over, Element.ALIGN_CENTER, new Phrase(split[row % split.length], f), x, y, 5);
            }
            over.restoreState();
        }
        stamper.close();
        reader.close();
    }
}