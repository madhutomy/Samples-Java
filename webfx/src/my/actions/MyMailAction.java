package my.actions;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.PreencodedMimeBodyPart;

import com.stimulsoft.base.mail.StiMailProperties;
import com.stimulsoft.flex.StiMailAction;
import com.stimulsoft.flex.interactionObject.StiMailData;

/**
 * MyMailAction.
 * 
 * Copyright Stimulsoft
 * 
 */
public class MyMailAction extends StiMailAction {

    @Override
    public void init(StiMailData mailData, StiMailProperties mailConf) {
        System.out.println("must override this method to specify your own init");
        this.mailData = mailData;
        this.mailConf = mailConf;
        session = getSession();
    }

    @Override
    protected Session getSession() {
        System.out.println("must override this method to specify your own Session");
        Properties props = getProperties();
        return Session.getInstance(props);
    }

    @Override
    protected Properties getProperties() {
        System.out.println("must override this method to specify your own mail Properties");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }

    @Override
    protected Message getMessage() throws MessagingException {
        System.out.println("must override this method to specify your own mail Message");
        Message message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailConf.getFrom()));
        message.setRecipients(Message.RecipientType.CC,
                InternetAddress.parse(mailConf.getRecipients()));

        message.setSubject(mailConf.getSubject());

        BodyPart text = getTextPart();
        BodyPart body = getFilePart();

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(text);
        mp.addBodyPart(body);

        message.setContent(mp);
        return message;
    }

    @Override
    protected BodyPart getTextPart() throws MessagingException {
        System.out.println("must override this method to specify your own mail TextPart");
        MimeBodyPart text = new MimeBodyPart();
        text.setText(mailConf.getBody(), "UTF-8", "plain");
        return text;
    }

    @Override
    protected BodyPart getFilePart() throws MessagingException {
        System.out.println("must override this method to specify your own mail FilePart");
        PreencodedMimeBodyPart body = new PreencodedMimeBodyPart("base64");
        body.setFileName(mailData.getFileName());
        body.setContent(mailData.getData(), mailData.getMIMEType());
        return body;
    }

    private Transport getTransport() throws MessagingException {
        System.out.println("must override this method to specify your own mail Transport");
        Transport transport = session.getTransport("smtp");
        transport.connect(mailConf.getHost(), mailConf.getSmtpPort(), mailConf.getUserName(),
                mailConf.getPassword());
        return transport;
    }

    @Override
    public void sendMessage() throws MessagingException {
        System.out.println("must override this method to specify your own send Message");
        Message message = getMessage();
        Transport transport = getTransport();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

}
