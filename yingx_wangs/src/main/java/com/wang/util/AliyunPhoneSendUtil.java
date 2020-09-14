package com.wang.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.Random;

/*
 * 阿里云 手机验证码
 *  调用getRandCode  设置多长验证码 然后调用  sendPhoneCode(String phones,String code)  输入手机号 和 验证码
 * */
public class AliyunPhoneSendUtil {

    //注：有备注无需修改的位置请勿改动。//    手机号       短信签名 短信模板  验证码
    private static SendSmsResponse getsendPhoneCodes(String phoneNumbers, String signName, String templateCode, String templateParam) throws ClientException {

        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = "LTAI4G9GXGJuWrMhYhtJBynj";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "PswrBWKWBanj06bcqvOnmSH9dKTFkX";//你的accessKeySecret，参考本文档步骤2

        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        //"15566286121,17719807219,13347080578,13347080578"
        request.setPhoneNumbers(phoneNumbers);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        //"{\"name\":\"Tom\", \"code\":\"123\"}"
        request.setTemplateParam(templateParam);
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
        }
        return sendSmsResponse;
    }

    //发送短信验证码  用户调用此方法获取验证码  第二步  调用此方法  输入手机号 和验证码
    public static String sendPhoneCode(String phones, String code) {  //手机号  验证码

        String phoneNumbers = phones;
        String signName = "查叶饼";//阿里云 →短信服务 →国内消息 →签名管理 →签名名称
        String templateCode = "SMS_200723719";  //阿里云 →短信服务 →国内消息 →模板管理 →模版CODE
        String templateParam = "{\"code\":\"" + code + "\"}";

        String message = null;
        try {
            SendSmsResponse response = getsendPhoneCodes(phoneNumbers, signName, templateCode, templateParam);

            //获取状态码
            String messageCode = response.getCode();

            if (messageCode.equals("OK")) {
                message = "发送成功";
            }
            if (messageCode.equals("isv.AMOUNT_NOT_ENOUGH")) {
                message = "账户余额不足";
            }
            if (messageCode.equals("isv.MOBILE_NUMBER_ILLEGAL")) {
                message = "非法手机号";
            }
            if (messageCode.equals("isv.OUT_OF_SERVICE")) {
                message = "业务停机";
            }

        } catch (ClientException e) {
            e.printStackTrace();
            message = "发送失败";
        }
        return message;
    }


    /**
     * 生成相应长度的数字随机数
     *
     * @param size 长度
     * @return String
     */
    public static String getRandCode(int size) {//   第一步   获取指定长度的验证码   
        String temp = "1234567890";
        int length = temp.length();
        int p;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            p = r.nextInt(length);
            sb.append(temp.substring(p, p + 1));
        }
        return sb.toString();
    }

    //测试 案例
    public static void main(String[] args) {
        String phoneNumbers = "15566286121,17719807219,13347080578,13347080578";

        String randCode = getRandCode(6);
        System.out.println("存储随机数：" + randCode);

        String msg = sendPhoneCode(phoneNumbers, randCode);

        System.out.println(msg);
    }

}
