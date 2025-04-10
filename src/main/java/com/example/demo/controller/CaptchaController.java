package com.example.demo.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @GetMapping("/generate")
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        response.setHeader("Cache-Control", "no-store");
        response.setContentType("image/jpeg");


        String captchaText = captchaProducer.createText();
        session.setAttribute("captcha", captchaText);

        BufferedImage image = captchaProducer.createImage(captchaText);
        ServletOutputStream outputStream = response.getOutputStream();

        ImageIO.write(image, "jpg", outputStream);
        outputStream.flush();
        outputStream.close();

    }
}
