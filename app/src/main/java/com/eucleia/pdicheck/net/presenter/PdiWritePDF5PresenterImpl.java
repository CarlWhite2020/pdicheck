package com.eucleia.pdicheck.net.presenter;

import android.text.TextUtils;

import com.eucleia.pdicheck.R;
import com.eucleia.pdicheck.listener.WritePDF5Listener;
import com.eucleia.pdicheck.room.database.AppDatabase;
import com.eucleia.pdicheck.room.entity.PdiReport;
import com.eucleia.tabscanap.bean.diag.CDispPdiCheckBeanEvent;
import com.eucleia.pdicheck.bean.normal.PdiGroup;
import com.eucleia.pdicheck.bean.normal.PdiItem;
import com.eucleia.tabscanap.constant.CharVar;
import com.eucleia.pdicheck.bean.constant.PDF5Constant;
import com.eucleia.pdicheck.bean.constant.SPKEY;
import com.eucleia.tabscanap.util.ArraysUtils;
import com.eucleia.tabscanap.util.DSUtils;
import com.eucleia.tabscanap.util.ELogUtils;
import com.eucleia.tabscanap.util.Pdf5Utils;
import com.eucleia.tabscanap.util.ResUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * PDI PDF 生成工具.
 */
public class PdiWritePDF5PresenterImpl implements WritePDF5Listener {

    private CDispPdiCheckBeanEvent event;
    private PdiReport report;

    public PdiWritePDF5PresenterImpl(final CDispPdiCheckBeanEvent event, final PdiReport report) {
        this.event = event;
        this.report = report;
    }

    @Override
    public void writeTo(final PdfWriter writer, final Document doc) throws Exception {


        doc.add(Pdf5Utils.get().getEmptyParagraph(10));

        //添加标题
        Paragraph headParagraph = new Paragraph(event.getStrCaption(),
                Pdf5Utils.get().getFont(PDF5Constant.HEAD_SIZE, Font.BOLD, PDF5Constant.MAIN_COLOR));
        headParagraph.setAlignment(Element.ALIGN_CENTER);
        doc.add(headParagraph);

        //添加报告编码
        doc.add(Pdf5Utils.get().getEmptyParagraph(25));
        String vin = TextUtils.isEmpty(event.getVin()) ? CharVar.EMPTY_VIN : event.getVin();
        Paragraph codeParagraph = new Paragraph(ResUtils.getString(R.string.report_number)
                + vin + CharVar.MINUS
                + PDF5Constant.FORMAT.format(report.createTime),
                Pdf5Utils.get().getFont(PDF5Constant.HINT_SIZE));
        codeParagraph.setIndentationLeft(PDF5Constant.DEFAULT_SIZE);
        doc.add(codeParagraph);

        //添加报告检测信息
        addCheckInfo(doc);

        //添加车辆信息
        addCarInfo(doc);


        Image resultImage = event.getResult() == 1
                ? Pdf5Utils.get().getImage(PDF5Constant.QUALIFIED_PNG)
                : Pdf5Utils.get().getImage(PDF5Constant.UNQUALIFIED_PNG);
        resultImage.scaleAbsolute(204, 183);
        resultImage.setAbsolutePosition(345, 570);
        PdfContentByte directContent = writer.getDirectContent();
        directContent.addImage(resultImage);


        //添加每个检测组
        addGroup(doc);

    }

    private void addGroup(Document doc) throws DocumentException, IOException {
        List<PdiGroup> groups = event.getGroups();
        for (PdiGroup group : groups) {

            doc.add(Pdf5Utils.get().getEmptyParagraph(20));
            PdfPTable groupTable = new PdfPTable(1);
            groupTable.setWidthPercentage(100);
            //groupTable.setKeepTogether(true);

            //如果有组名且显示时
            if (group.isShowGroup()) {

                PdfPTable groupNameTable = new PdfPTable(2);
                groupNameTable.setWidthPercentage(100);

                //组名称
                Paragraph groupNameParagraph = new Paragraph(group.getGroupName(),
                        Pdf5Utils.get().getFont(PDF5Constant.MAIN_COLOR, Font.BOLD));
                PdfPCell groupNameCell = Pdf5Utils.get().getPCell(groupNameParagraph);
                Pdf5Utils.get().setPadding(groupNameCell, 10, PDF5Constant.DEFAULT_SIZE, 10, PDF5Constant.NOT_SIZE);
                groupNameTable.addCell(groupNameCell);

                //组状态
                Image groupStatusImage = group.getResult() == 3
                        ? Pdf5Utils.get().getImage(PDF5Constant.GOOD_PNG)
                        : Pdf5Utils.get().getImage(PDF5Constant.WARN_PNG);
                groupStatusImage.setBackgroundColor(BaseColor.WHITE);
                groupStatusImage.scaleAbsolute(12, 12);

                PdfPCell groupStatusCell = new PdfPCell(groupStatusImage);
                Pdf5Utils.get().setPadding(groupStatusCell, 12, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE);
                groupStatusCell.setBorderColor(BaseColor.WHITE);
                groupStatusCell.setUseBorderPadding(true);
                groupStatusCell.setBackgroundColor(BaseColor.WHITE);
                groupStatusCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                groupStatusCell.setVerticalAlignment(Element.ALIGN_CENTER);
                groupNameTable.addCell(groupStatusCell);

                PdfPCell groupCell = new PdfPCell(groupNameTable);
                groupCell.setBorderColor(BaseColor.WHITE);
                groupCell.setUseBorderPadding(true);
                groupCell.setBackgroundColor(BaseColor.WHITE);
                groupTable.addCell(groupCell);
            }

            //子项分布
            List<PdiItem> items = group.getItems();
            PdfPTable itemTotalTable = null;
            if (items.size() > 1) {
                itemTotalTable = new PdfPTable(2);
                itemTotalTable.setWidthPercentage(100);
                itemTotalTable.setWidths(new int[]{1, 1});
            }
            for (PdiItem item : items) {

                PdfPCell itemCell;
                PdfPTable itemNameTable = new PdfPTable(2);
                itemNameTable.setWidthPercentage(100);
                itemNameTable.setWidths(new int[]{2, 1});

                //项名称
                Paragraph itemNameParagraph = new Paragraph(item.getItemName(),
                        Pdf5Utils.get().getFont(PDF5Constant.MAIN_COLOR, item.isGroupBold() ? Font.BOLD : Font.NORMAL));
                PdfPCell itemValueCell = Pdf5Utils.get().getPCell(itemNameParagraph);
                itemNameTable.addCell(itemValueCell);

                //项状态
                Paragraph statusParagraph = new Paragraph();
                Image itemStatusImage = null;
                if (item.getResult() <= 4) {
                    itemStatusImage = item.getResult() == 3
                            ? Pdf5Utils.get().getImage(PDF5Constant.GOOD_PNG)
                            : Pdf5Utils.get().getImage(PDF5Constant.ERROR_PNG);
                    itemStatusImage.setBackgroundColor(BaseColor.WHITE);
                    itemStatusImage.scaleAbsolute(12, 12);
                } else {
                    BaseColor color = !ArraysUtils.isEmpty(item.getItemCurDtc()) ? PDF5Constant.RED_COLOR : PDF5Constant.YELLOW_COLOR;
                    statusParagraph.add(new Chunk(String.valueOf(item.getResult() - 4), Pdf5Utils.get().getFont(color)));
                    statusParagraph.add(new Chunk(CharVar.SPACE));
                }
                if (!TextUtils.isEmpty(item.getItemValue())) {
                    statusParagraph.add(new Chunk(item.getItemValue(), Pdf5Utils.get().getFont()));
                    statusParagraph.add(new Chunk(CharVar.SPACE));
                }
                if (itemStatusImage != null) {
                    statusParagraph.add(new Chunk(itemStatusImage, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE, true));
                }

                PdfPCell itemStatusCell = new PdfPCell(statusParagraph);
                itemStatusCell.setBorderColor(BaseColor.WHITE);
                itemStatusCell.setUseBorderPadding(true);
                itemStatusCell.setBackgroundColor(BaseColor.WHITE);
                itemStatusCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itemStatusCell.setVerticalAlignment(Element.ALIGN_CENTER);
                itemNameTable.addCell(itemStatusCell);

                PdfPCell itemNameCell = new PdfPCell();
                itemNameCell.setBorderColor(BaseColor.WHITE);
                itemNameCell.setUseBorderPadding(true);
                itemNameCell.setBackgroundColor(BaseColor.WHITE);
                itemNameCell.addElement(itemNameTable);

                PdfPTable detailTable = new PdfPTable(1);
                detailTable.setWidthPercentage(100);
                if (item.isShowGroup()) {

                    //版本号
                    StringBuilder line1 = new StringBuilder();
                    if (!TextUtils.isEmpty(item.getItemPartNumber())) {
                        line1.append(ResUtils.getString(R.string.part_num));
                        line1.append(item.getItemPartNumber());
                        line1.append(CharVar.SPACE);
                    }
                    if (!TextUtils.isEmpty(item.getItemVersionNumber())) {
                        line1.append(ResUtils.getString(R.string.ver_num));
                        line1.append(item.getItemVersionNumber());
                    }
                    if (!TextUtils.isEmpty(line1)) {
                        PdfPCell verCell = Pdf5Utils.get().getPCell(new Paragraph(line1.toString(),
                                Pdf5Utils.get().getFont(PDF5Constant.MIN_SIZE, PDF5Constant.HINT_COLOR)));
                        detailTable.addCell(verCell);
                    }

                    //当前故障码
                    if (!ArraysUtils.isEmpty(item.getItemCurDtc())) {
                        StringBuilder line2 = new StringBuilder();
                        line2.append(ResUtils.getString(R.string.cur_dtc));
                        line2.append("\n");
                        for (String cur : item.getItemCurDtc()) {
                            line2.append(cur);
                            line2.append(CharVar.COMMA);
                        }
                        line2.deleteCharAt(line2.lastIndexOf(CharVar.COMMA));
                        PdfPCell curCell = Pdf5Utils.get().getPCell(new Paragraph(line2.toString(),
                                Pdf5Utils.get().getFont(PDF5Constant.HINT_SIZE, PDF5Constant.HINT_COLOR)));
                        detailTable.addCell(curCell);

                    }

                    //历史故障码
                    if (!ArraysUtils.isEmpty(item.getItemHisDtc())) {
                        StringBuilder line3 = new StringBuilder();
                        line3.append(ResUtils.getString(R.string.his_dtc));
                        line3.append("\n");
                        for (String his : item.getItemHisDtc()) {
                            line3.append(his);
                            line3.append(CharVar.COMMA);
                        }
                        line3.deleteCharAt(line3.lastIndexOf(CharVar.COMMA));
                        PdfPCell hisCell = Pdf5Utils.get().getPCell(new Paragraph(line3.toString(),
                                Pdf5Utils.get().getFont(PDF5Constant.HINT_SIZE, PDF5Constant.HINT_COLOR)));
                        detailTable.addCell(hisCell);

                    }


                }

                if (itemTotalTable != null) {
                    PdfPCell lineCell = Pdf5Utils.get().getPCell();
                    detailTable.addCell(Pdf5Utils.get().getEmptyCell(4));
                    DottedLineSeparator dottedLineSeparator = new DottedLineSeparator();
                    dottedLineSeparator.setLineColor(PDF5Constant.SPLIT_COLOR);
                    lineCell.addElement(dottedLineSeparator);
                    detailTable.addCell(lineCell);
                }

                //下拉项
                PdfPCell detailCell = new PdfPCell();
                Pdf5Utils.get().setPadding(detailCell, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE);
                detailCell.setBorderColor(BaseColor.WHITE);
                detailCell.setUseBorderPadding(true);
                detailCell.setBackgroundColor(BaseColor.WHITE);
                detailCell.addElement(detailTable);

                PdfPTable itemTable = new PdfPTable(1);
                itemTable.setWidthPercentage(100);
                itemTable.addCell(itemNameCell);
                itemTable.addCell(detailCell);
                itemCell = new PdfPCell();
                itemCell.setBorderColor(BaseColor.WHITE);
                itemCell.setUseBorderPadding(true);
                itemCell.setBackgroundColor(BaseColor.WHITE);
                itemCell.addElement(itemTable);


                Pdf5Utils.get().setPadding(itemCell, PDF5Constant.NOT_SIZE, PDF5Constant.ITEM_SIZE, 5, PDF5Constant.ITEM_SIZE);
                if (itemTotalTable == null) {
                    groupTable.addCell(itemCell);
                } else {
                    itemTotalTable.addCell(itemCell);
                }

            }


            doc.add(groupTable);

            if (itemTotalTable != null) {
                doc.add(itemTotalTable);
            }
        }
    }

    private void addCheckInfo(Document doc) throws DocumentException, IOException {
        doc.add(Pdf5Utils.get().getEmptyParagraph(PDF5Constant.DEFAULT_SIZE));
        PdfPTable infoTable = new PdfPTable(4);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new int[]{2, 3, 2, 3});

        //检测时间
        PdfPCell timeNameCell = Pdf5Utils.get().getPCell(ResUtils.getString(R.string.check_time));
        Pdf5Utils.get().setPadding(timeNameCell, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE);
        infoTable.addCell(timeNameCell);
        PdfPCell timeValueCell = Pdf5Utils.get().getPCell(PDF5Constant.timeValueFormat.format(report.createTime));
        timeValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Pdf5Utils.get().setPadding(timeValueCell, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE);
        infoTable.addCell(timeValueCell);

        //检测机构
        PdfPCell gtsCcisnamecell = Pdf5Utils.get().getPCell(ResUtils.getString(R.string.check_co));
        Pdf5Utils.get().setPadding(gtsCcisnamecell, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE);
        infoTable.addCell(gtsCcisnamecell);
        PdfPCell gtsCcisvaluecell = Pdf5Utils.get().getPCell(report.co);
        gtsCcisvaluecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Pdf5Utils.get().setPadding(gtsCcisvaluecell, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE);
        infoTable.addCell(gtsCcisvaluecell);

        //检测故障总数
        PdfPCell totalDtcNameCell = Pdf5Utils.get().getPCell(ResUtils.getString(R.string.pdi_total_dtc));
        Pdf5Utils.get().setPadding(totalDtcNameCell, 6, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE);
        infoTable.addCell(totalDtcNameCell);
        PdfPCell totalDtcValueCell = Pdf5Utils.get().getPCell(String.valueOf(event.getDtcNum()));
        totalDtcValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Pdf5Utils.get().setPadding(totalDtcValueCell, 6, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE);
        infoTable.addCell(totalDtcValueCell);

        //检测设备
        PdfPCell deviceNameCell = Pdf5Utils.get().getPCell(ResUtils.getString(R.string.check_sn));
        Pdf5Utils.get().setPadding(deviceNameCell, 6, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE);
        infoTable.addCell(deviceNameCell);
        PdfPCell deviceValueCell = Pdf5Utils.get().getPCell(DSUtils.get().getString(SPKEY.SN_CODE));
        deviceValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Pdf5Utils.get().setPadding(deviceValueCell, 6, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE);
        infoTable.addCell(deviceValueCell);
        doc.add(infoTable);
    }

    private void addCarInfo(Document doc) throws DocumentException, IOException {
        doc.add(Pdf5Utils.get().getEmptyParagraph(PDF5Constant.DEFAULT_SIZE));
        PdfPTable carTable = new PdfPTable(4);
        carTable.setWidthPercentage(100);
        carTable.setWidths(new int[]{2, 3, 2, 3});

        //品牌
        PdfPCell brandNameCell = Pdf5Utils.get().getPCell(ResUtils.getString(R.string.brand));
        Pdf5Utils.get().setPadding(brandNameCell, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE);
        carTable.addCell(brandNameCell);
        PdfPCell brandValueCell = Pdf5Utils.get().getPCell(event.getBrand());
        brandValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Pdf5Utils.get().setPadding(brandValueCell, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE);
        carTable.addCell(brandValueCell);

        //年款
        PdfPCell yearNameCell = Pdf5Utils.get().getPCell(ResUtils.getString(R.string.year));
        Pdf5Utils.get().setPadding(yearNameCell, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE);
        carTable.addCell(yearNameCell);
        PdfPCell yearValueCell = Pdf5Utils.get().getPCell(event.getYear());
        yearValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Pdf5Utils.get().setPadding(yearValueCell, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE);
        carTable.addCell(yearValueCell);

        //车型
        PdfPCell modelNameCell = Pdf5Utils.get().getPCell(ResUtils.getString(R.string.carModels));
        Pdf5Utils.get().setPadding(modelNameCell, 6, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE);
        carTable.addCell(modelNameCell);
        PdfPCell modelValueCell = Pdf5Utils.get().getPCell(event.getModel());
        modelValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Pdf5Utils.get().setPadding(modelValueCell, 6, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE);
        carTable.addCell(modelValueCell);

        //vin码
        PdfPCell vinNameCell = Pdf5Utils.get().getPCell(ResUtils.getString(R.string.vin));
        Pdf5Utils.get().setPadding(vinNameCell, 6, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.NOT_SIZE);
        carTable.addCell(vinNameCell);
        PdfPCell vinValueCell = Pdf5Utils.get().getPCell(event.getVin());
        vinValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Pdf5Utils.get().setPadding(vinValueCell, 6, PDF5Constant.NOT_SIZE, PDF5Constant.DEFAULT_SIZE, PDF5Constant.DEFAULT_SIZE);
        carTable.addCell(vinValueCell);
        doc.add(carTable);
    }

    @Override
    public void start() {
        FileUploadPresenter.get().beginCreate();
        ELogUtils.v();
    }

    @Override
    public void error(final Throwable throwable) {
        ELogUtils.e(throwable.getMessage());
        throwable.printStackTrace();
        FileUploadPresenter.get().errorCreate();
    }

    @Override
    public void complete() {
        ELogUtils.d();
        Long id = AppDatabase.get().getReportDao().createReport(this.report);
        report.id = id;
        if (TextUtils.isEmpty(report.vin)) {
            FileUploadPresenter.get().cancelUpload();
        } else {
            FileUploadPresenter.get().beginUpload();
            FileUploadPresenter.get().upload(report);
        }
    }
}
