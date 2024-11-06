package demo.HotelManagement.service;

import jakarta.mail.MessagingException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private TemplateEngine templateEngine;

    private static final String PATH_OF_IMAGE = "classpath:/static/img/";

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    private final ResourceLoader resourceLoader;


    public EmailService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private String readImageToBase64(String pathOfImage) throws IOException {
        Resource resource = resourceLoader.getResource(PATH_OF_IMAGE.concat(pathOfImage));//full path resources/static/img/logo.png

        File imageFile = new File(String.valueOf(resource.getFile()));
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        return Base64.encodeBase64String(imageBytes);//encode into base 64
    }

    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> templateModel) throws IOException {
        MimeMessage message = emailSender.createMimeMessage();
        String imageBase64 = readImageToBase64("logo.PNG");//dont forget to load image into template using basecode64
        try {
            templateModel.put("images", imageBase64);
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(templateModel);
            String emailContent = templateEngine.process(templateName, context);

            helper.setText(emailContent, true);

            emailSender.send(message);
        } catch (javax.mail.MessagingException e) {
            LOG.info("error info {}", e);
        }
    }
}