package com.eucleia.tabscanap.bean.diag.child;

import android.text.TextUtils;

import java.io.Serializable;

public class ReportBean implements Serializable {


    /**
     * SN : 序列号
     * comment :
     * diagnostic_application_version : apk的版本
     * diagnostic_path : 诊断路径
     * diagnostic_time : 生成诊断报告时间
     * inspection_results : {"faulty_system":[{"faults":[{"fault_code":"P0401","fault_description":"EGR Valve A Flow Insufficient Detected","fault_status":""},{"fault_code":"P1291","fault_description":"Injector High Side Short To GND Or VBATT (Bank1)","fault_status":""}],"system_description":"ECM(Engine Control Module)"},{"faults":[{"fault_code":"P2500","fault_description":"The Transmission Has an Impermissible Transmission Ratio.","fault_status":""},{"fault_code":"P2502","fault_description":"The Gear is Implausible or the Transmission is Slipping.","fault_status":""},{"fault_code":"P2211","fault_description":"The Selector Lever is in an Intermediate Position.","fault_status":""}],"system_description":"TCM(Transmission Control Module)"},{"faults":[{"fault_code":"C1237","fault_description":"Speed wheel rear input signal missing","fault_status":""}],"system_description":"ABS(Anti-lock Braking VehicleSystem)"},{"faults":[{"fault_code":"B1878","fault_description":"Airbag-OFF-Lampe Fehlerart","fault_status":"Current"},{"fault_code":"B1868","fault_description":"The Coding for the Ignition Circuit with Component R12/22(Left Rear Window Airbag Ignition Squib) is Faulty.","fault_status":"Current"},{"fault_code":"B1019","fault_description":"The Ignition Circuit with Component R12/11(Left Rear Side Airbag Ignition Squib) Has Short Circuit to Positive.","fault_status":"Stored"}],"system_description":"SRS(Supplemental Inflatable Restraint VehicleSystem)"},{"faults":[{"fault_code":"B1310","fault_description":"Run / Start Control: Circuit Short to Battery","fault_status":""}],"system_description":"BCM(Body Control Module)"},{"faults":[{"fault_code":"B2775","fault_description":"Interior Verification (to Theft Deterrent ECU / Smart Key ECU )","fault_status":""},{"fault_code":"B2776","fault_description":"Steering Lock Motor","fault_status":""},{"fault_code":"B2795","fault_description":"Unmatched Key Code","fault_status":""}],"system_description":"IMM(Immobilizer)"},{"faults":[{"fault_code":"C1501","fault_description":"Tire Pressure Sensor 1 Internal","fault_status":""},{"fault_code":"C1502","fault_description":"Tire Pressure Sensor 2 Internal","fault_status":""}],"system_description":"TPMS(Tire Pressure Monitoring VehicleSystem)"},{"faults":[{"fault_code":"C1630","fault_description":"SAS Timeout","fault_status":""},{"fault_code":"C2205","fault_description":"SAS Internal Error","fault_status":""},{"fault_code":"C121A","fault_description":"SAS Initialization","fault_status":""}],"system_description":"SAS(Steering Angle VehicleSystem)"}],"healthy_system":[{"system_description":"BMS(Battery Manager VehicleSystem)"}]}
     * job_nb : 用户名
     * notes :
     * postcode :
     * repair_shop_address :
     * repair_shop_email : 用户邮箱
     * repair_shop_name : 用户名
     * repair_shop_phone_number : 用户手机号
     * repair_type :
     * technician_name : 设备名称（productmodel）
     * vehicle_engine_size :
     * vehicle_info ： 车辆信息
     * vehicle_make : 车型品牌
     * vehicle_manufactured_year : 车辆年份
     * vehicle_model : 车型型号
     * vehicle_odometer : 车辆里程
     * vehicle_registration :
     * vehicle_software_version : 诊断软件版本
     * vehicle_vin : Vin码
     */
    private Long id;
    private String SN;
    private String comment;
    private String diagnostic_application_version;
    private String diagnostic_path;
    private String diagnostic_time;
    private InspectionResultsBean inspection_results;
    private String job_nb;
    private String notes;
    private String postcode;
    private String repair_shop_address;
    private String repair_shop_email;
    private String repair_shop_name;
    private String repair_shop_phone_number;
    private String repair_type;
    private String technician_name;
    private String vehicle_engine_size;
    private String vehicle_info;
    private String vehicle_make;
    private String vehicle_manufactured_year;
    private String vehicle_model;
    private String vehicle_odometer;
    private String vehicle_registration;
    private String vehicle_software_version;
    private String vehicle_vin;
    private InspectionObdresultsBean inspection_obdresults;
    private ReportAdditional additional;

    private boolean isUploading = false;
    private boolean needUpload = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isUploading() {
        return isUploading;
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }

    public boolean isNeedUpload() {
        return needUpload;
    }

    public void setNeedUpload(boolean needUpload) {
        this.needUpload = needUpload;
    }

    public ReportAdditional getAdditional() {
        return additional;
    }

    public void setAdditional(ReportAdditional additional) {
        this.additional = additional;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDiagnostic_application_version() {
        return diagnostic_application_version;
    }

    public void setDiagnostic_application_version(String diagnostic_application_version) {
        this.diagnostic_application_version = diagnostic_application_version;
    }

    public String getDiagnostic_path() {
        return diagnostic_path;
    }

    public void setDiagnostic_path(String diagnostic_path) {
        this.diagnostic_path = diagnostic_path;
    }

    public String getDiagnostic_time() {
        return diagnostic_time;
    }

    public void setDiagnostic_time(String diagnostic_time) {
        this.diagnostic_time = diagnostic_time;
    }

    public InspectionResultsBean getInspection_results() {
        return inspection_results;
    }

    public void setInspection_results(InspectionResultsBean inspection_results) {
        this.inspection_results = inspection_results;
    }

    public String getJob_nb() {
        return job_nb;
    }

    public void setJob_nb(String job_nb) {
        this.job_nb = job_nb;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPostcode() {
        return TextUtils.isEmpty(postcode) ? "" : postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getRepair_shop_address() {
        return repair_shop_address;
    }

    public void setRepair_shop_address(String repair_shop_address) {
        this.repair_shop_address = repair_shop_address;
    }

    public String getRepair_shop_email() {
        return repair_shop_email;
    }

    public void setRepair_shop_email(String repair_shop_email) {
        this.repair_shop_email = repair_shop_email;
    }

    public String getRepair_shop_name() {
        return TextUtils.isEmpty(repair_shop_name) ? "" : repair_shop_name;
    }

    public void setRepair_shop_name(String repair_shop_name) {
        this.repair_shop_name = repair_shop_name;
    }

    public String getRepair_shop_phone_number() {
        return repair_shop_phone_number;
    }

    public void setRepair_shop_phone_number(String repair_shop_phone_number) {
        this.repair_shop_phone_number = repair_shop_phone_number;
    }

    public String getRepair_type() {
        return repair_type;
    }

    public void setRepair_type(String repair_type) {
        this.repair_type = repair_type;
    }

    public String getTechnician_name() {
        return technician_name;
    }

    public void setTechnician_name(String technician_name) {
        this.technician_name = technician_name;
    }

    public String getVehicle_engine_size() {
        return vehicle_engine_size;
    }

    public void setVehicle_engine_size(String vehicle_engine_size) {
        this.vehicle_engine_size = vehicle_engine_size;
    }

    public String getVehicle_info() {
        return vehicle_info;
    }

    public void setVehicle_info(String vehicle_info) {
        this.vehicle_info = vehicle_info;
    }

    public String getVehicle_make() {
        if (TextUtils.isEmpty(vehicle_make)) {
            return "";
        }
        return vehicle_make;
    }

    public void setVehicle_make(String vehicle_make) {
        this.vehicle_make = vehicle_make;
    }

    public String getVehicle_manufactured_year() {
        return vehicle_manufactured_year;
    }

    public void setVehicle_manufactured_year(String vehicle_manufactured_year) {
        this.vehicle_manufactured_year = vehicle_manufactured_year;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }

    public String getVehicle_odometer() {
        return vehicle_odometer;
    }

    public void setVehicle_odometer(String vehicle_odometer) {
        this.vehicle_odometer = vehicle_odometer;
    }

    public String getVehicle_registration() {
        return vehicle_registration;
    }

    public void setVehicle_registration(String vehicle_registration) {
        this.vehicle_registration = vehicle_registration;
    }

    public String getVehicle_software_version() {
        if (TextUtils.isEmpty(vehicle_software_version)) return "";
        return vehicle_software_version;
    }

    public void setVehicle_software_version(String vehicle_software_version) {
        this.vehicle_software_version = vehicle_software_version;
    }

    public String getVehicle_vin() {
        return vehicle_vin;
    }

    public void setVehicle_vin(String vehicle_vin) {
        this.vehicle_vin = vehicle_vin;
    }

    public InspectionObdresultsBean getInspection_obdresults() {
        return inspection_obdresults;
    }

    public void setInspection_obdresults(InspectionObdresultsBean inspection_obdresults) {
        this.inspection_obdresults = inspection_obdresults;
    }
}
