package com.eucleia.pdicheck.net;

import com.eucleia.pdicheck.BuildConfig;
import com.eucleia.tabscanap.constant.CharVar;

/**
 * author admin
 */
public interface NetAPI {


    static String getAPI(String api) {
        if (api.startsWith(CharVar.HTTP)) return api;
        StringBuilder sb = new StringBuilder();
        switch (BuildConfig.Flavor) {
            case DEV:
                sb.append(NetConfig.Live);
                break;
            case UAT:
                sb.append(NetConfig.Uat);
                break;
            case LIVE:
                sb.append(NetConfig.Live);
                break;
        }
        sb.append(api);
        return sb.toString();
    }

    static String getPdfUpload(){
        if (BuildConfig.DEBUG){
            return PDF_UPLOAD_DEBUG;
        }return PDF_UPLOAD_RELEASE;
    }

    /**
     * 登录
     */
    String LOGIN = "/api/dbox/user/login";

    /**
     * 修改密码
     */
    String PW_CHANGE = "/api/dbox/user/change";

    /**
     * 更新
     */
    String APP_UPDATE = "/api/dbox/app/version";

    /**
     * 上传PDF
     *
     * @value vin VIN
     * @value fileName 文件名称
     * @value file 文件(base64)
     */
    String PDF_UPLOAD_DEBUG = "https://smps-test-xzzx.sgmwsales.com/api/pdi/checkPdf/upload";

    String PDF_UPLOAD_RELEASE = "https://quality-oa.sgmwsales.com/api/pdi/checkPdf/upload";



    String GUIDE_URL = "https://oss.eucleia.net/otherdata/PDI/PDI-Check-guide.html";

}
