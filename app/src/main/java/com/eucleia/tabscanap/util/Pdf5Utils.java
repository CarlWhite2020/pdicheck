package com.eucleia.tabscanap.util;

import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.listener.WritePDF5Listener;
import com.eucleia.pdicheck.net.presenter.PdiWritePDF5PresenterImpl;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.tabscanap.bean.diag.CDispPdiCheckBeanEvent;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.tabscanap.constant.FileVar;
import com.eucleia.pdicheck.bean.constant.PDF5Constant;
import com.eucleia.tabscanap.constant.PathVar;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lzy.okgo.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Pdf5Utils {

    private Pdf5Utils() {
    }

    public void savePdf(CDispPdiCheckBeanEvent event) {
        long time = System.currentTimeMillis();
        String vin = event.getVin();
        if (TextUtils.isEmpty(vin)) {
            vin = CharVar.EMPTY_VIN;
        }
        File outPDFPath = getOutPDFPath(vin, time);
        PdiReport report = new PdiReport();
        report.id = null;
        report.createTime = time;
        report.uploadStatus = 0;
        report.path = outPDFPath.getPath();
        report.fileName = outPDFPath.getName();
        report.vin = event.getVin();
        report.model = event.getModel();
        report.result = event.getResult();
        report.year = event.getYear();
        report.brand = event.getBrand();
        report.dtcNum = event.getDtcNum();
        report.co = ResUtils.getString(R.string.wuling_co);

        createPDF(outPDFPath, new PdiWritePDF5PresenterImpl(event, report));

    }

    private static class InnerClass {
        private static final Pdf5Utils INSTANCE = new Pdf5Utils();
    }

    public static Pdf5Utils get() {
        return InnerClass.INSTANCE;
    }

    public File getOutPDFPath(String name, long time) {

        String pathFormat = PDF5Constant.PATH_FORMAT.format(time);
        File file = new File(SDUtils.getLocalFile(), PathVar.PdfPath + pathFormat);
        FileUtils.createOrExistsDir(file);
        String pdfName = name + CharVar.MINUS + PDF5Constant.FORMAT.format(time) + FileVar.PdfSuffix;
        return new File(file, pdfName);
    }

    public void createPDF(File pdfFile, WritePDF5Listener listener) {
        if (listener != null) {
            listener.start();
        }
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            try {
                FileOutputStream out = new FileOutputStream(pdfFile);
                Rectangle rect = new Rectangle(PageSize.A4);
                rect.setBackgroundColor(PDF5Constant.BG_COLOR);
                Document doc = new Document(rect);
                PdfWriter writer = PdfWriter.getInstance(doc, out);
                doc.setMargins(14, 14, 10, 10);

                doc.open();
                writer.open();
                if (listener != null) {
                    listener.writeTo(writer, doc);
                }
                doc.close();
                writer.close();
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onComplete() {
                        if (listener != null) {
                            listener.complete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (listener != null) {
                            listener.error(e);
                        }
                    }
                });
    }


    public Font getFont() throws DocumentException, IOException {
        return getFont(PDF5Constant.MIAN_SIZE);
    }

    public Font getFont(int size) throws DocumentException, IOException {
        return getFont(size, Font.UNDEFINED, PDF5Constant.MAIN_COLOR);
    }

    public Font getFontByStyle(int style) throws DocumentException, IOException {
        return getFont(PDF5Constant.MIAN_SIZE, style, PDF5Constant.MAIN_COLOR);
    }

    public Font getFont(BaseColor color) throws DocumentException, IOException {
        return getFont(PDF5Constant.MIAN_SIZE, Font.UNDEFINED, color);
    }

    public Font getFont(int size, BaseColor color) throws DocumentException, IOException {
        return getFont(size, Font.UNDEFINED, color);
    }

    public Font getFont(BaseColor color, int style) throws DocumentException, IOException {
        return getFont(PDF5Constant.MIAN_SIZE, style, color);
    }

    public Font getFont(int size, int style, BaseColor color) throws DocumentException, IOException {
        BaseFont font = BaseFont.createFont("assets/pdf_resource/pdf_font.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        return new Font(font, size, style, color);
    }

    public void setPadding(PdfPCell cell, int top, int left, int bottom, int right) {
        cell.setPaddingTop(top);
        cell.setPaddingLeft(left);
        cell.setPaddingBottom(bottom);
        cell.setPaddingRight(right);
    }

    public PdfPCell getPCell() {
        PdfPCell pdfPCell = new PdfPCell();
        pdfPCell.setBorderColor(BaseColor.WHITE);
        pdfPCell.setUseBorderPadding(true);
        return pdfPCell;
    }


    public PdfPCell getPCell(Phrase paragraph) {
        PdfPCell pdfPCell = new PdfPCell(paragraph);
        pdfPCell.setBorderColor(BaseColor.WHITE);
        pdfPCell.setUseBorderPadding(true);
        return pdfPCell;
    }


    public PdfPCell getPCell(String str) throws DocumentException, IOException {
        PdfPCell pdfPCell = new PdfPCell(getParagraph(str));
        pdfPCell.setBorderColor(BaseColor.WHITE);
        pdfPCell.setUseBorderPadding(true);
        pdfPCell.setBackgroundColor(BaseColor.WHITE);
        return pdfPCell;
    }

    public PdfPCell getEmptyCell(int size) throws DocumentException, IOException {
        return getPCell(new Paragraph(CharVar.SPACE, getFont(size)));
    }

    public Paragraph getParagraph(String str) throws DocumentException, IOException {
        return new Paragraph(str, getFont());
    }

    public Paragraph getEmptyParagraph(int size) throws DocumentException, IOException {
        return new Paragraph(CharVar.SPACE, getFont(size));
    }

    public PdfPTable getTable() {
        PdfPTable table = new PdfPTable(1);
        return table;
    }

    public Image getImage(String fileName) throws BadElementException, IOException {
        InputStream open = Utils.getApp().getAssets().open(fileName);
        return Image.getInstance(IOUtils.toByteArray(open));
    }


}
