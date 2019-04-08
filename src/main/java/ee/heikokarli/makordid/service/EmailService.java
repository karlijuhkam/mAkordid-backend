package ee.heikokarli.makordid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String from, String to, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
        String htmlMsg = "<body style=\"max-width: 600px; margin: 0 auto; text-align: center; margin-bottom: 30px; font-family: 'Montserrat', sans-serif; background-color: white; border: 1px solid #00A861;\">" +
                "<div style=\"text-align: center; max-width: 600px; color: white; background-color: #00A861; margin: 0 auto; padding: 25px 0px 25px 0px;\">" +
                "<h1 style=\"margin: 0;\">" + subject + "</h1></div>" +
                "<p>" + body +
                "<br><a href=\"https://www.makordid.ee\">www.makordid.ee</a><span style=\"color: black; font-size 12px;\"></span></p></body>";
        mimeMessage.setContent(htmlMsg, "text/html");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(from);

        javaMailSender.send(mimeMessage);
    }

}
