package com.eucleia.tabscanap.jni.diagnostic;

import com.eucleia.tabscanap.bean.diag.child.FaultsBean;
import com.eucleia.tabscanap.bean.diag.child.Information;
import com.eucleia.tabscanap.bean.diag.child.InspectionObdresultsBean;
import com.eucleia.tabscanap.bean.diag.child.Iupr;
import com.eucleia.tabscanap.bean.diag.child.Livedata;
import com.eucleia.tabscanap.bean.diag.child.Readiness;
import com.eucleia.tabscanap.bean.diag.child.VehicleSystem;
import com.eucleia.tabscanap.constant.DiagnoseEvent;
import com.eucleia.tabscanap.bean.diag.CDispOBDReportBeanEvent;
import com.eucleia.tabscanap.constant.CDispConstant;
import com.eucleia.tabscanap.constant.PageType;
import com.eucleia.tabscanap.util.EDiagUtils;
import com.eucleia.tabscanap.util.ELogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class CDispOBDReport {

    public final static int SHOW = 1010;
    public final static int HIDE = 1011;

    /**
     * 全局对象引用管理者
     */
    public static CDispOBDReportBeanEvent sCDispOBDReportBeanEvent = new CDispOBDReportBeanEvent();

    /**
     * 判断是否调用过show()一次,为setColor,SetTitle等更新界面做判断使用;
     */
    static boolean IsExcuteShow;

    /**
     * 初始化所有数据
     * 添加对象到Map
     * [JNI 层引用]
     */
    public static void setData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport setData 方法:" + objKey);
        InspectionObdresultsBean inspectionObdresultsBean = new InspectionObdresultsBean();
        inspectionObdresultsBean.setSystem(new ArrayList<VehicleSystem>());
        inspectionObdresultsBean.setIupr(new ArrayList<Iupr>());
        inspectionObdresultsBean.setLivedata(new ArrayList<Livedata>());
        inspectionObdresultsBean.setReadiness(new ArrayList<Readiness>());
        sCDispOBDReportBeanEvent.setInspectionObdresultsBean(inspectionObdresultsBean);
    }

    /**
     * 释放所有数据
     * 析构方法 释放资源 移除对象出Map
     * [JNI 层引用]
     */
    public static void resetData(int objKey) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport resetData 方法:" + objKey);
        sCDispOBDReportBeanEvent = new CDispOBDReportBeanEvent();
    }


    /*-----------------------------------------------------------------------------
    功能：初始化诊断报告
    参数说明：strCaption标题文本
              nDispType 界面显示左右  =DISP_LEFT
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void InitData(int objKey, String strCaption, int nDispType) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport InitData 方法:" + objKey);
        InspectionObdresultsBean inspectionObdresultsBean = new InspectionObdresultsBean();
        inspectionObdresultsBean.setSystem(new ArrayList<VehicleSystem>());
        inspectionObdresultsBean.setIupr(new ArrayList<Iupr>());
        inspectionObdresultsBean.setLivedata(new ArrayList<Livedata>());
        inspectionObdresultsBean.setReadiness(new ArrayList<Readiness>());
        inspectionObdresultsBean.setStrCaption(strCaption);
        sCDispOBDReportBeanEvent.setDispType(nDispType);
        sCDispOBDReportBeanEvent.setInspectionObdresultsBean(inspectionObdresultsBean);
    }

    /*-----------------------------------------------------------------------------
    功能：设置通讯协议
    参数说明：strProtocol 通讯协议
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void SetProtocol(int objKey, String strProtocol) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport SetProtocol 方法:" + objKey);
        sCDispOBDReportBeanEvent.getInspectionObdresultsBean().setProtocol(strProtocol);
    }

    /*-----------------------------------------------------------------------------
    功能：设置检测结果
    参数说明：iQualify 0 - 不合格， 1 - 合格
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void SetQualify(int objKey, int iQualify) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport SetQualify 方法:" + objKey);
        sCDispOBDReportBeanEvent.getInspectionObdresultsBean().setQualify(iQualify);
    }

    /*-----------------------------------------------------------------------------
    功能：设置钥匙打到ON，故障灯MIL状态
    参数说明：iMILStatus MIL状态， 0为熄灭，1为点亮
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void SetMILStatusByKeyOn(int objKey, int iMILStatus) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport SetMILStatusByKeyOn 方法:" + objKey);
        sCDispOBDReportBeanEvent.getInspectionObdresultsBean().setMil_status_key_on_int(iMILStatus);
    }

    /*-----------------------------------------------------------------------------
    功能：设置发动机启动，故障灯MIL状态
    参数说明：iMILStatus MIL状态， 0为熄灭，1为点亮
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void SetMILStatusByEngineStart(int objKey, int iMILStatus) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport SetMILStatusByEngineStart 方法:" + objKey);
        sCDispOBDReportBeanEvent.getInspectionObdresultsBean().setMil_status_engine_start_int(iMILStatus);
    }

    /*-----------------------------------------------------------------------------
    功能：设置故障灯MIL状态
    参数说明：iMILStatus MIL状态， 0为熄灭，1为点亮
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void SetMILStatus(int objKey, int iMILStatus) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport SetMILStatus 方法:" + objKey);
        sCDispOBDReportBeanEvent.getInspectionObdresultsBean().setMil_status_int(iMILStatus);
    }

    /*-----------------------------------------------------------------------------
    功能：设置故障灯MIL状态
    参数说明：strMILStatus MIL状态
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void SetMILStatus(int objKey, String strMILStatus) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport SetMILStatus 方法:" + objKey);
        sCDispOBDReportBeanEvent.getInspectionObdresultsBean().setMil_status(strMILStatus);
    }

    /*-----------------------------------------------------------------------------
    功能：设置故障灯亮后行驶里程数（KM）
    参数说明：strMILOnMileage 故障灯亮后的行驶里程
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void SetMILOnMileage(int objKey, String strMILOnMileage) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport SetMILOnMileage 方法:" + objKey);
        sCDispOBDReportBeanEvent.getInspectionObdresultsBean().setMil_on_mileage(strMILOnMileage);
    }

    /*-----------------------------------------------------------------------------
    功能：设置故障总数
    参数说明：iTotal 故障总数
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void SetDTCTotal(int objKey, int iTotal) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport SetDTCTotal 方法:" + objKey);
        sCDispOBDReportBeanEvent.getInspectionObdresultsBean().setFaults_total(iTotal);
    }


    /*-----------------------------------------------------------------------------
    功能：添加历史故障代码
    参数说明：strCode 故障代号
              strSystem 系统
              strDesc 故障描述
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void AddHistoryDtcItems(int objKey, String strCode, String strSystem, String strDesc) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport AddHistoryDtcItems 方法:" + objKey);
        FaultsBean historyBean = new FaultsBean();
        historyBean.setFault_code(strCode);
        historyBean.setFault_description(strDesc);
        List<VehicleSystem> systems = sCDispOBDReportBeanEvent.getInspectionObdresultsBean().getSystem();
        List<FaultsBean> faults_historyBeans;
        boolean hasSys = false;
        if (systems.size() > 0) {
            for (VehicleSystem sys : systems) {
                if (strSystem.equals(sys.getSystem_description())) {
                    hasSys = true;
                    faults_historyBeans = sys.getFaults_historyBean();
                    if (faults_historyBeans == null) {
                        faults_historyBeans = new ArrayList<>();
                    }
                    faults_historyBeans.add(historyBean);
                    sys.setFaults_historyBean(faults_historyBeans);
                    break;
                }
            }
            if (!hasSys) {
                faults_historyBeans = new ArrayList<>();
                faults_historyBeans.add(historyBean);
                VehicleSystem sys = new VehicleSystem();
                sys.setSystem_description(strSystem);
                sys.setFaults_historyBean(faults_historyBeans);
                systems.add(sys);
            }
        } else {
            faults_historyBeans = new ArrayList<>();
            faults_historyBeans.add(historyBean);
            VehicleSystem sys = new VehicleSystem();
            sys.setSystem_description(strSystem);
            sys.setFaults_historyBean(faults_historyBeans);
            systems.add(sys);
        }
    }

    /*-----------------------------------------------------------------------------
    功能：添加当前故障代码
    参数说明：strCode 故障代号
              strSystem 系统
              strDesc 故障描述
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void AddCurrentDtcItems(int objKey, String strCode, String strSystem, String strDesc) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport AddCurrentDtcItems 方法:" + objKey);
        FaultsBean currentBean = new FaultsBean();
        currentBean.setFault_code(strCode);
        currentBean.setFault_description(strDesc);
        List<VehicleSystem> systems = sCDispOBDReportBeanEvent.getInspectionObdresultsBean().getSystem();
        List<FaultsBean> faultsCurrentBeans;
        boolean hasSys = false;
        if (systems.size() > 0) {
            for (VehicleSystem sys : systems) {
                if (strSystem.equals(sys.getSystem_description())) {
                    hasSys = true;
                    faultsCurrentBeans = sys.getFaults_current();
                    if (faultsCurrentBeans == null) {
                        faultsCurrentBeans = new ArrayList<>();
                    }
                    faultsCurrentBeans.add(currentBean);
                    sys.setFaults_current(faultsCurrentBeans);
                    break;
                }
            }
            if (!hasSys) {
                faultsCurrentBeans = new ArrayList<>();
                faultsCurrentBeans.add(currentBean);
                VehicleSystem sys = new VehicleSystem();
                sys.setSystem_description(strSystem);
                sys.setFaults_current(faultsCurrentBeans);
                systems.add(sys);
            }
        } else {
            faultsCurrentBeans = new ArrayList<>();
            faultsCurrentBeans.add(currentBean);
            VehicleSystem sys = new VehicleSystem();
            sys.setSystem_description(strSystem);
            sys.setFaults_current(faultsCurrentBeans);
            systems.add(sys);
        }
    }

    /*-----------------------------------------------------------------------------
    功能：添加控制单元状态
    参数说明：strSys系统名称
              strName名称
              strStatus状态
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void AddECUInforItems(int objKey, String strSys, String strName, String strStatus) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport AddECUInforItems 方法:" + objKey);
        Information information = new Information();
        information.setInformation_name(strName);
        information.setInformation_value(strStatus);
        List<VehicleSystem> systems = sCDispOBDReportBeanEvent.getInspectionObdresultsBean().getSystem();
        List<Information> informations;
        boolean hasSys = false;
        if (systems.size() > 0) {
            for (VehicleSystem sys : systems) {
                if (strSys.equals(sys.getSystem_description())) {
                    hasSys = true;
                    informations = sys.getInformation();
                    if (informations == null) {
                        informations = new ArrayList<>();
                    }
                    informations.add(information);
                    sys.setInformation(informations);
                    break;
                }
            }
            if (!hasSys) {
                informations = new ArrayList<>();
                informations.add(information);
                VehicleSystem sys = new VehicleSystem();
                sys.setSystem_description(strSys);
                sys.setInformation(informations);
                systems.add(sys);
            }
        } else {
            informations = new ArrayList<>();
            informations.add(information);
            VehicleSystem sys = new VehicleSystem();
            sys.setSystem_description(strSys);
            sys.setInformation(informations);
            systems.add(sys);
        }
    }


    /*-----------------------------------------------------------------------------
    功能：添加诊断就绪未完成项目
    参数说明：strName名称
              strStatus状态
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void AddReadinessStatusItems(int objKey, String strName, String strStatus, int iQualify) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport AddReadinessStatusItems 方法:" + objKey);
        Readiness readiness = new Readiness();
        readiness.setReadiness_name(strName);
        readiness.setReadiness_status(strStatus);
        readiness.setReadiness_qualify(iQualify);
        List<Readiness> readinesses = sCDispOBDReportBeanEvent.getInspectionObdresultsBean().getReadiness();
        readinesses.add(readiness);
    }

    /*-----------------------------------------------------------------------------
    功能：添加车载排放诊断系统实际监测频率 (IUPR状态)
    参数说明：strName名称
              strStatus状态
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void AddIUPRStatusItems(int objKey, String strName, String strStatus) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport AddIUPRStatusItems 方法:" + objKey);
        Iupr iupr = new Iupr();
        iupr.setIupr_name(strName);
        iupr.setIupr_status(strStatus);
        List<Iupr> iuprs = sCDispOBDReportBeanEvent.getInspectionObdresultsBean().getIupr();
        iuprs.add(iupr);
    }

    /*-----------------------------------------------------------------------------
    功能：添加实时数据流值
    参数说明：strName名称
              strValue值
              strUnit单位
    返回值：无
    说明：无
    -----------------------------------------------------------------------------*/
    public static void AddLiveDataItems(int objKey, String strName, String strValue, String strUnit, String strMin, String strMax, int iQualify) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport AddLiveDataItems 方法:" + objKey);
        Livedata livedata = new Livedata();
        livedata.setLivedata_name(strName);
        livedata.setLivedata_value(strValue);
        livedata.setLivedata_unit(strUnit);
        livedata.setLivedata_minvalue(strMin);
        livedata.setLivedata_maxvalue(strMax);
        livedata.setLivedata_qualify(iQualify);
        List<Livedata> livedatas = sCDispOBDReportBeanEvent.getInspectionObdresultsBean().getLivedata();
        livedatas.add(livedata);
    }


    /*-----------------------------------------------------------------------------
    功   能：显示（模态(阻塞)的界面）
    参数说明：bIsShow true 显示 false 不显示
    返回值：int 返回用户按的键（包含上边定义的返回值宏）
    说   明：可以不显示界面生成报告
    -----------------------------------------------------------------------------*/
    public static int Show(int objKey, boolean bIsShow) {
        ELogUtils.e(EDiagUtils.get().isOpenLogCat(),"-- CDispOBDReport Show 方法:" + objKey);
        sCDispOBDReportBeanEvent.setObjKey(objKey);
        DiagnoseEvent.mCDispOBDReportBeanEvent = sCDispOBDReportBeanEvent;

        if (!bIsShow) {
            sCDispOBDReportBeanEvent.event_what = HIDE;
            sendCmd(sCDispOBDReportBeanEvent);
                return CDispConstant.PageButtonType.DF_ID_BACK;
        }

        // 记录路径
        EDiagUtils.get().addPath(objKey, PageType.Type_ObdReport);
        // 记录执行过show()方法,
        IsExcuteShow = true;
        // [首先判断]判断线程状态 结束
        if (EDiagUtils.get().isThreadEnd()) {
            sCDispOBDReportBeanEvent.setBackFlag(CDispConstant.PageButtonType.DF_ID_BACK);
            sCDispOBDReportBeanEvent.lockAndSignalAll();
            IsExcuteShow = false;
            return sCDispOBDReportBeanEvent.getBackFlag();
        }
        sCDispOBDReportBeanEvent.event_what = SHOW;
        sendCmd(sCDispOBDReportBeanEvent);
        sCDispOBDReportBeanEvent.lockAndWait();
        return DiagnoseEvent.mCDispOBDReportBeanEvent.getBackFlag();
    }

    private static void sendCmd(CDispOBDReportBeanEvent bean) {
        EventBus.getDefault().post(bean);
    }

}
