package com.eucleia.pdicheck.listener;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public interface WritePDF5Listener {

    /**
     * pdf内容填充.
     * @param writer PdfWriter
     * @param doc 文档.
     * @throws Exception 异常
     */
    void writeTo(PdfWriter writer, Document doc) throws Exception;

    /**
     * pdf开始填充.
     */
    void start();

    /**
     * pdf填充异常.
     * @param throwable 异常
     */
    void error(Throwable throwable);

    /**
     * pdf填充完成.
     */
    void complete();
}
