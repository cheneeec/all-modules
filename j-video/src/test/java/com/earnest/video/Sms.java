package com.earnest.video;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sms {
    public static void main(String[] arg) throws Exception {
        // 运行登录发送
        LoginGo();

    }

    public static void LoginGo() throws Exception {

        //正式URL
        String Url = "http://112.35.1.155:1992/sms/norsubmit?";


        String Sent_Result = "";
        String success = "";
        String msgGroup = "";
        String rspcod = "";


        for (int i = 0; i < 1; i++) {
            Sent_Result = SentSms(Url,
                    GetSmsParam());
            System.out.println("发送结果:" + Sent_Result);
            success = jsonToBean(Sent_Result, "success");
            System.out.println("success:" + success);

//			if ( Boolean.valueOf(success)) {
//				msgGroup = jsonToBean(Sent_Result, "msgGroup");
//				System.out.println("msgGroup:" + msgGroup);
//			}else {
//				msgGroup = jsonToBean(Sent_Result, "msgGroup");
//				rspcod = jsonToBean(Sent_Result, "rspcod");
//				System.out.println("msgGroup:" + msgGroup + "\n"
//						+ "rspcod:" + rspcod);
//			}

            Thread.sleep(300);
        }
    }

    // SmsParam获取
    public static String GetSmsParam() throws Exception {
//
        Submit submit = new Submit();
        submit.setEcName("政企分公司测试");
        submit.setApId("octsns");
        submit.setSecretKey("passwd@7531");
        submit.setMobiles("18280045913");
        submit.setSign("DWItALe3A");
        submit.setContent("我是007AASDA#@!$%...");
        submit.setAddSerial("");
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(submit.getEcName());
        stringBuffer.append(submit.getApId());
        stringBuffer.append(submit.getSecretKey());
        stringBuffer.append(submit.getMobiles());
        stringBuffer.append(submit.getContent());
        stringBuffer.append(submit.getSign());
        stringBuffer.append(submit.getAddSerial());


        String selfMac = encryptToMD5(stringBuffer.toString());
        System.out.println("selfMac:" + selfMac);
        submit.setMAC(selfMac);
        String param = JSON.toJSONString(submit);
        System.out.println("param:" + param);

        //Base64加密
        String encode = Base64.encodeBase64String(param.getBytes("UTF-8"));
        System.out.println(encode);

        return encode;
    }


    // 短信发送SentSms
    public static String SentSms(String Url, String Param) {
        String s_SentSms = sendPost(Url, Param);
        // System.out.println("【发送结果�?"+s_SentSms);
        return s_SentSms;
    }

    // 发送post请求
    public static String sendPost(String url, String param) {
//		PrintWriter out = null;
        OutputStreamWriter out = null;

        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);

//			out = new PrintWriter(conn.getOutputStream());
//			out.print(param);
//			out.flush();

            out = new OutputStreamWriter(conn.getOutputStream());
            out.write(param);
            out.flush();


            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\n" + line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    // MD5转换
    public static String encryptToMD5(String password) {
        byte[] digesta = null;
        String result = null;
        try {

            // 得到一个MD5的消息摘要
            MessageDigest mdi = MessageDigest.getInstance("MD5");
            // 添加要进行计算摘要的信息
            mdi.update(password.getBytes("utf-8"));
            // 得到该摘要
            digesta = mdi.digest();
            result = byteToHex(digesta);
        } catch (NoSuchAlgorithmException e) {

        } catch (UnsupportedEncodingException e) {
            // TODO 自动生成�?catch �?
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将二进制转化为16进制字符串
     */
    public static String byteToHex(byte[] pwd) {
        StringBuilder hs = new StringBuilder("");
        String temp = "";
        for (int i = 0; i < pwd.length; i++) {
            temp = Integer.toHexString(pwd[i] & 0XFF);
            if (temp.length() == 1) {
                hs.append("0").append(temp);
            } else {
                hs.append(temp);
            }
        }
        return hs.toString().toLowerCase();
    }

    // 取json对应�?
    public static String jsonToBean(String data, String tittle) {
        // System.out.println("data:"+data);
        // System.out.println("tittle:"+tittle);
        try {
            JSONObject json = JSONObject.parseObject(data);
            String result = json.getString(tittle);
            return result;
        } catch (Exception e) {
            return "Json解析失败";
        }
    }
}
class Submit {

    String ecName;
    String apId;
    String secretKey;
    String mobiles;
    String content;
    String sign;
    String addSerial;
    String MAC;

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAddSerial() {
        return addSerial;
    }

    public void setAddSerial(String addSerial) {
        this.addSerial = addSerial;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String mAC) {
        MAC = mAC;
    }

}
