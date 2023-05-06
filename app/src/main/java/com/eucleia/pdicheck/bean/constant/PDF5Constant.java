package com.eucleia.pdicheck.bean.constant;


import com.itextpdf.text.BaseColor;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * pdf用到的常量.
 */
public interface PDF5Constant {

    /**
     * 编号时间格式.
     */
    SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmss",
            Locale.getDefault());

    /**
     * 路径时间格式.
     */
    SimpleDateFormat PATH_FORMAT = new SimpleDateFormat("yyyy年MM月dd日",
            Locale.getDefault());

    /**
     * 检测时间格式.
     */
    SimpleDateFormat timeValueFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss",
            Locale.getDefault());

    /**
     * 背景色.
     */
    BaseColor BG_COLOR = new BaseColor(0xEE, 0xEE, 0xEE);

    /**
     * 主文字色.
     */
    BaseColor MAIN_COLOR = new BaseColor(0x33, 0x33, 0x33);

    /**
     * 提示文字色.
     */
    BaseColor HINT_COLOR = new BaseColor(0x99, 0x99, 0x99);

    /**
     * 红色（Error）.
     */
    BaseColor RED_COLOR = new BaseColor(0xFC, 0x53, 0x5D);

    /**
     * 黄色（Warn）.
     */
    BaseColor YELLOW_COLOR = new BaseColor(0xF8, 0xAC, 0x2F);

    /**
     * 分割色.
     */
    BaseColor SPLIT_COLOR = new BaseColor(0xC1, 0xD5, 0xD6);

    /**
     * 标题颜色.
     */
    int HEAD_SIZE = 20;

    /**
     * 主体文字.
     */
    int MIAN_SIZE = 12;

    /**
     * 提示文字.
     */
    int HINT_SIZE = 10;

    /**
     * 备注信息文字.
     */
    int MIN_SIZE = 8;

    /**
     * 合格.
     */
    String QUALIFIED_PNG = "pdf_resource/ic_qualified.png";

    /**
     * 不合格.
     */
    String UNQUALIFIED_PNG = "pdf_resource/ic_unqualified.png";

    /**
     * √.
     */
    String GOOD_PNG = "pdf_resource/ic_good.png";

    /**
     * ×.
     */
    String ERROR_PNG = "pdf_resource/ic_error.png";

    /**
     * !.
     */
    String WARN_PNG = "pdf_resource/ic_warn.png";

    /**
     * 无边距.
     */
    int NOT_SIZE = 0;

    /**
     * 默认边距.
     */
    int DEFAULT_SIZE = 15;

    /**
     * 子项内边距.
     */
    int ITEM_SIZE = 10;

}
