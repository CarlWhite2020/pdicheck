# 编译需知
compileSdkVersion 30  需要SDK版本是30
buildToolsVersion "29.0.2" 需要编译工具版本29.0.2

## 调试代码
 Gradle -> project -> app -> Tasks -> install -> install{APP_TYPE}{INSTALL_TYPE}
 installDevDebug 表示将开发环境的应用装到手机或者模拟器

## 编译指令
 Gradle -> project -> app -> Tasks -> build -> assemble{APP_TYPE}
assemble不带后缀表示编译所有类型的版本

assembleLive表示编译线上版本，取/app/build/outputs/apk/live/release/下的.apk文件

## 输出路径
/app/build/outputs/apk/{APP_TYPE}/release/
/app/build/outputs/apk/{APP_TYPE}/debug/  

## APP_TYPE
### DEV 通用接口 测试环境
### UAT 通用接口 验收环境
### LIVE 通用接口 生成环境

## INSTALL_TYPE
### DEBUG 开发版本
报告上传路径 
https://smps-test-xzzx.sgmwsales.com/api/pdi/checkPdf/upload

### RELEASE 正式版本
报告上传路径 
https://quality-oa.sgmwsales.com/api/pdi/checkPdf/upload