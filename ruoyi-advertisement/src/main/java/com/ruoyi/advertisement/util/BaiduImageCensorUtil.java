package com.ruoyi.advertisement.util;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 百度AI图像审核工具类
 * 使用HTTP方式直接调用百度AI内容审核API
 * 
 * @author ruoyi
 */
@Component
public class BaiduImageCensorUtil {

    private static final Logger log = LoggerFactory.getLogger(BaiduImageCensorUtil.class);

    /** 审核结果 - 合法 */
    public static final String RESULT_COMPLIANT = "合法";
    
    /** 审核结果 - 违规 */
    public static final String RESULT_NON_COMPLIANT = "违规";
    
    /** 审核结果 - 疑似违规 */
    public static final String RESULT_SUSPECTED = "疑似违规";
    
    /** 审核结果 - 审核异常 */
    public static final String RESULT_ERROR = "审核异常";

    /** 百度AI Access Token获取地址 */
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    
    /** 百度AI图像审核API地址 */
    private static final String IMAGE_CENSOR_URL = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";

    @Value("${baidu.ai.api-key}")
    private String apiKey;

    @Value("${baidu.ai.secret-key}")
    private String secretKey;

    /**
     * 审核图片（使用图片URL）
     * 
     * @param imageUrl 图片URL地址
     * @return 审核结果
     */
    public String censorImageByUrl(String imageUrl) {
        return censorImageByUrl(apiKey, secretKey, imageUrl);
    }

    /**
     * 审核图片（使用图片URL）
     * 
     * @param apiKey    API Key
     * @param secretKey Secret Key
     * @param imageUrl  图片URL地址
     * @return 审核结果
     */
    public String censorImageByUrl(String apiKey, String secretKey, String imageUrl) {
        long startTime = System.currentTimeMillis();
        log.info("========== 开始百度AI图像审核(URL方式) ==========");
        log.info("图片URL: {}", imageUrl);

        try {
            // 1. 获取Access Token
            String accessToken = getAccessToken(apiKey, secretKey);
            if (accessToken == null) {
                log.error("获取Access Token失败，使用降级方案判定为合法");
                log.info("========== 百度AI图像审核结束(耗时: {}ms) ==========", System.currentTimeMillis() - startTime);
                return RESULT_COMPLIANT;
            }
            log.info("获取Access Token成功");

            // 2. 构建请求URL和参数
            String apiUrl = IMAGE_CENSOR_URL + "?access_token=" + accessToken;
            String params = "image=" + URLEncoder.encode(imageUrl, "UTF-8") +
                           "&imgType=1" +
                           "&detect_type=porn,terror,politics,abuse,contraband,text";

            log.info("请求参数: imgType=1, detect_type=porn,terror,politics,abuse,contraband,text");

            // 3. 发送POST请求
            String response = sendPostRequest(apiUrl, params);
            log.info("百度AI API返回原始JSON: {}", response);

            // 4. 解析审核结果
            String result = parseCensorResult(response);

            log.info("图片审核完成，结果: {}, 耗时: {}ms", result, System.currentTimeMillis() - startTime);
            log.info("========== 百度AI图像审核结束 ==========");
            return result;

        } catch (Exception e) {
            log.error("百度AI图像审核异常: {}, 使用降级方案判定为合法", e.getMessage(), e);
            log.info("图片审核异常，耗时: {}ms", System.currentTimeMillis() - startTime);
            log.info("========== 百度AI图像审核结束 ==========");
            return RESULT_COMPLIANT;
        }
    }

    /**
     * 审核图片（使用本地文件路径）
     * 
     * @param imagePath 本地图片文件路径
     * @return 审核结果
     */
    public String censorImageByFile(String imagePath) {
        return censorImageByFile(apiKey, secretKey, imagePath);
    }

    /**
     * 审核图片（使用本地文件路径）
     * 
     * @param apiKey    API Key
     * @param secretKey Secret Key
     * @param imagePath 本地图片文件路径
     * @return 审核结果
     */
    public String censorImageByFile(String apiKey, String secretKey, String imagePath) {
        long startTime = System.currentTimeMillis();
        log.info("========== 开始百度AI图像审核(文件方式) ==========");
        log.info("图片文件路径: {}", imagePath);

        try {
            // 1. 获取Access Token
            String accessToken = getAccessToken(apiKey, secretKey);
            if (accessToken == null) {
                log.error("获取Access Token失败，使用降级方案判定为合法");
                log.info("========== 百度AI图像审核结束(耗时: {}ms) ==========", System.currentTimeMillis() - startTime);
                return RESULT_COMPLIANT;
            }
            log.info("获取Access Token成功");

            // 2. 读取图片文件
            File file = new File(imagePath);
            if (!file.exists() || !file.isFile()) {
                log.error("图片文件不存在: {}，使用降级方案判定为合法", imagePath);
                return RESULT_COMPLIANT;
            }

            byte[] imageBytes = readFileToBytes(imagePath);
            if (imageBytes == null) {
                log.error("读取图片文件失败: {}，使用降级方案判定为合法", imagePath);
                return RESULT_COMPLIANT;
            }
            log.info("图片文件大小: {} bytes", imageBytes.length);

            // 3. 构建请求URL和参数
            String apiUrl = IMAGE_CENSOR_URL + "?access_token=" + accessToken;
            String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            String params = "image=" + URLEncoder.encode(imageBase64, "UTF-8") +
                           "&imgType=0" +
                           "&detect_type=porn,terror,politics,abuse,contraband,text";

            log.info("请求参数: imgType=0, detect_type=porn,terror,politics,abuse,contraband,text");

            // 4. 发送POST请求
            String response = sendPostRequest(apiUrl, params);
            log.info("百度AI API返回原始JSON: {}", response);

            // 5. 解析审核结果
            String result = parseCensorResult(response);

            log.info("图片审核完成，结果: {}, 耗时: {}ms", result, System.currentTimeMillis() - startTime);
            log.info("========== 百度AI图像审核结束 ==========");
            return result;

        } catch (Exception e) {
            log.error("百度AI图像审核异常: {}, 使用降级方案判定为合法", e.getMessage(), e);
            log.info("图片审核异常，耗时: {}ms", System.currentTimeMillis() - startTime);
            log.info("========== 百度AI图像审核结束 ==========");
            return RESULT_COMPLIANT;
        }
    }

    /**
     * 获取百度AI Access Token
     * 
     * @param apiKey    API Key
     * @param secretKey Secret Key
     * @return Access Token
     */
    private String getAccessToken(String apiKey, String secretKey) {
        try {
            String params = "grant_type=client_credentials" +
                           "&client_id=" + URLEncoder.encode(apiKey, "UTF-8") +
                           "&client_secret=" + URLEncoder.encode(secretKey, "UTF-8");

            String response = sendPostRequest(ACCESS_TOKEN_URL, params);
            log.debug("获取Access Token响应: {}", response);

            JSONObject json = new JSONObject(response);
            if (json.has("access_token")) {
                return json.getString("access_token");
            } else {
                log.error("获取Access Token失败，响应: {}", response);
                return null;
            }
        } catch (Exception e) {
            log.error("获取Access Token异常: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 发送HTTP POST请求
     * 
     * @param url    请求URL
     * @param params 请求参数
     * @return 响应内容
     */
    private String sendPostRequest(String url, String params) throws Exception {
        URL apiUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(60000);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        // 写入请求参数
        try (OutputStream out = conn.getOutputStream()) {
            out.write(params.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }

        // 读取响应
        StringBuilder response = new StringBuilder();
        int responseCode = conn.getResponseCode();

        if (responseCode == 200) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            log.error("HTTP请求失败，响应码: {}", responseCode);
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        }

        conn.disconnect();
        return response.toString();
    }

    /**
     * 解析百度AI审核结果
     * 
     * @param response API响应内容
     * @return 审核结果
     */
    private String parseCensorResult(String response) {
        try {
            log.info("========== 开始解析百度AI响应 ==========");
            log.info("完整响应内容: {}", response);
            
            if (response == null || response.trim().isEmpty()) {
                log.error("百度AI API响应为空，使用降级方案判定为合法");
                return RESULT_COMPLIANT;
            }

            JSONObject json = new JSONObject(response);

            if (json.has("error_code")) {
                int errorCode = json.getInt("error_code");
                String errorMsg = json.optString("error_msg", "未知错误");
                log.error("百度AI API错误: error_code={}, error_msg={}，使用降级方案判定为合法", errorCode, errorMsg);
                return RESULT_COMPLIANT;
            }

            if (!json.has("conclusionType")) {
                log.error("百度AI API响应中缺少conclusionType字段，使用降级方案判定为合法");
                return RESULT_COMPLIANT;
            }

            int conclusionType = json.getInt("conclusionType");
            String conclusion = json.optString("conclusion", "");

            log.info("百度AI审核结论: conclusionType={}, conclusion={}", conclusionType, conclusion);

            String result;
            switch (conclusionType) {
                case 1:
                    log.info("【重要】判定为: 合法");
                    result = RESULT_COMPLIANT;
                    break;
                case 2:
                    log.info("【重要】判定为: 违规");
                    logCensorDetails(json);
                    result = RESULT_NON_COMPLIANT;
                    break;
                case 3:
                    log.info("【重要】判定为: 疑似违规");
                    logCensorDetails(json);
                    result = RESULT_NON_COMPLIANT; // 疑似违规按违规处理
                    break;
                case 4:
                    log.info("【重要】判定为: 审核失败，使用降级方案判定为合法");
                    result = RESULT_COMPLIANT;
                    break;
                default:
                    log.warn("未知的审核结论类型: {}, 使用降级方案判定为合法", conclusionType);
                    result = RESULT_COMPLIANT;
            }
            
            log.info("========== 解析完成，最终结果: {} ==========", result);
            return result;
        } catch (org.json.JSONException e) {
            log.error("解析百度AI响应JSON失败: {}, 错误: {}, 使用降级方案判定为合法", response, e.getMessage(), e);
            return RESULT_COMPLIANT;
        } catch (Exception e) {
            log.error("解析审核结果异常: {}, 使用降级方案判定为合法", e.getMessage(), e);
            return RESULT_COMPLIANT;
        }
    }

    /**
     * 记录审核详情信息
     * 
     * @param json 审核结果JSON
     */
    private void logCensorDetails(JSONObject json) {
        try {
            if (json.has("data")) {
                org.json.JSONArray dataArray = json.getJSONArray("data");
                StringBuilder details = new StringBuilder();

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject item = dataArray.getJSONObject(i);
                    String type = item.optString("type", "");
                    String msg = item.optString("msg", "");
                    String subType = item.optString("subType", "");
                    double probability = item.optDouble("probability", 0);

                    if (!type.isEmpty() || !msg.isEmpty()) {
                        if (details.length() > 0) {
                            details.append("; ");
                        }
                        details.append(type);
                        if (!subType.isEmpty()) {
                            details.append("(").append(subType).append(")");
                        }
                        if (!msg.isEmpty()) {
                            details.append(": ").append(msg);
                        }
                        if (probability > 0) {
                            details.append(" (概率: ").append(String.format("%.2f", probability)).append(")");
                        }
                    }
                }

                if (details.length() > 0) {
                    log.info("审核违规详情: {}", details.toString());
                }
            }
        } catch (Exception e) {
            log.warn("记录审核详情失败: {}", e.getMessage());
        }
    }

    /**
     * 读取文件为字节数组
     * 
     * @param filePath 文件路径
     * @return 字节数组
     */
    private byte[] readFileToBytes(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("读取文件失败: {}", filePath, e);
            return null;
        }
    }
}
