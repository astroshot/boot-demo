package com.boot.web.controller;

import com.boot.common.web.controller.BaseController;
import com.boot.common.web.helper.CaptchaHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


@RestController
public class CaptchaController extends BaseController {

    private static final int width = 60;

    private static final int height = 30;

    @GetMapping("/captcha")
    public String generate(HttpServletRequest request, HttpServletResponse response) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 获取图形上下文
        Graphics g = image.getGraphics();

        // 设定背景颜色
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);

        // 画边框
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);

        // 随机产生的验证码
        String strEnsure = CaptchaHelper.createCaptchaStr(request);

        // 将验证码显示在图像中，如果要生成更多位的验证码，增加 drawString 语句
        g.setColor(Color.black);
        g.setFont(new Font("Atlantic Inline", Font.PLAIN, 18));
        g.drawString(strEnsure, 9, 22);

        // 随机产生 10 个干扰点
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 1, 1);
        }

        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        OutputStream out;
        try {
            String sessionId = request.getSession().getId();
            logger.info(sessionId);
            out = response.getOutputStream();
            g.dispose();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
