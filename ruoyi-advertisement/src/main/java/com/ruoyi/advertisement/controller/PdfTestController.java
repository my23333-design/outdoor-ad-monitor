package com.ruoyi.advertisement.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;

import com.ruoyi.common.annotation.Anonymous;

@RestController
@RequestMapping("/test/pdf")
public class PdfTestController {

    private static final Logger log = LoggerFactory.getLogger(PdfTestController.class);

    @Anonymous
    @GetMapping("/simple")
    public void testSimplePdf(HttpServletResponse response) {
        log.info("开始生成测试PDF");
        try {
            BaseFont bfChinese = null;
            
            String[] fontPaths = {
                "C:\\Windows\\Fonts\\simsun.ttc",
                "C:/Windows/Fonts/simsun.ttc",
                "C:\\Windows\\Fonts\\simhei.ttf",
                "C:/Windows/Fonts/simhei.ttf"
            };

            for (String fontPath : fontPaths) {
                File fontFile = new File(fontPath);
                log.info("检查字体文件: " + fontPath + ", 存在: " + fontFile.exists());
                if (!fontFile.exists()) {
                    continue;
                }
                try {
                    bfChinese = BaseFont.createFont(fontPath + ",0", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    log.info("成功加载字体: " + fontPath);
                    break;
                } catch (Exception e) {
                    log.warn("加载字体失败: " + fontPath + ", 错误: " + e.getMessage());
                    continue;
                }
            }

            if (bfChinese == null) {
                log.info("尝试使用 STSong-Light 字体");
                try {
                    bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
                    log.info("成功加载 STSong-Light 字体");
                } catch (Exception e) {
                    log.error("所有字体加载失败: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("字体加载失败: " + e.getMessage());
                    return;
                }
            }

            log.info("字体加载成功，开始生成PDF内容");
            Font titleFont = new Font(bfChinese, 20, Font.BOLD, BaseColor.BLACK);
            Font contentFont = new Font(bfChinese, 12, Font.NORMAL, BaseColor.BLACK);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            Paragraph title = new Paragraph("测试中文PDF生成", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph content = new Paragraph();
            content.add(Chunk.NEWLINE);
            content.add(new Chunk("这是一个测试PDF文件，用于验证中文字体是否正确显示。", contentFont));
            content.add(Chunk.NEWLINE);
            content.add(Chunk.NEWLINE);
            content.add(new Chunk("行业分类：医疗健康行业", contentFont));
            content.add(Chunk.NEWLINE);
            content.add(new Chunk("媒体类型：电子屏广告", contentFont));
            content.add(Chunk.NEWLINE);
            content.add(new Chunk("违法类别：禁用词类", contentFont));
            content.add(Chunk.NEWLINE);
            content.add(new Chunk("处理结果：已罚款", contentFont));
            document.add(content);

            document.close();

            log.info("PDF生成成功，大小: " + baos.size() + " bytes");
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=test_chinese.pdf");
            response.setContentLength(baos.size());
            response.getOutputStream().write(baos.toByteArray());
            response.getOutputStream().flush();

        } catch (Exception e) {
            log.error("生成PDF失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("生成PDF失败: " + e.getMessage());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }
}
