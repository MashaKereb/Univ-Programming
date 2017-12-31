import java.io.IOException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailActions
{

    Properties props;
    private final String username;
    private final String password;

    public MailActions()
    {
        username = "somejavacodingtests@gmail.com";
        password = "21021997aa";

        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        props.put("mail.store.protocol", "imaps");
    }

    public void sendMail(Properties params)
    {
        Session session = Session.getInstance(props, new javax.mail.Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(username, password);
            }
        });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("somejavacodingtests@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(params.getProperty("recipient")));
            message.setSubject(params.getProperty("subject"));
            message.setContent(params.getProperty("message"), "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public Properties[] checkMail()
    {
        Properties[] propsRecieved = null;

        try
        {
            Session session = Session.getInstance(props, null);
            Store store = session.getStore();
            store.connect("imap.gmail.com", username, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            propsRecieved = new Properties[inbox.getMessageCount()];
            for (int i = 1; i <= inbox.getMessageCount(); i++)
            {
                propsRecieved[i-1] = new Properties();
                
                Message msg = inbox.getMessage(i);
                Address[] in = msg.getFrom();
                for (Address address : in)
                {
                    propsRecieved[i - 1].put("sender", address.toString());
                }
                Multipart mp = (Multipart) msg.getContent();
                BodyPart bp = mp.getBodyPart(0);
                propsRecieved[i - 1].put("topic", msg.getSubject());
                propsRecieved[i - 1].put("message", bp.getContent());
            }
        } catch (MessagingException | IOException mex)
        {
        }
        return propsRecieved;
    }
}


/*
Message msg = inbox.getMessage(inbox.getMessageCount()); //////
                Address[] in = msg.getFrom();
                for (Address address : in)
                {
                    System.out.println("FROM:" + address.toString());
                }
                Multipart mp = (Multipart) msg.getContent();
                BodyPart bp = mp.getBodyPart(0);
                System.out.println("SENT DATE:" + msg.getSentDate());
                System.out.println("SUBJECT:" + msg.getSubject());
                System.out.println("CONTENT:" + bp.getContent());*/
