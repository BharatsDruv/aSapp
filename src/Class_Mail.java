import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * asappapplication@gmail.com
 * shrivastava
 */

/**
 *
 * 
 */
public class Class_Mail {
    
    public static boolean connection_error=false; public static String from,pass;
    public static void sendFromGMail( String[] to, String subject, String body) {
        try{
            from="asappapplication@gmail.com"; // from email(my)
            pass="windowsphone"; // from pass 
            Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            System.out.print("Successfully Sent"+"\n");
            transport.close();
        
    }

    
    
    catch(Exception e)
    {
        connection_error=true;
        
    JOptionPane.showMessageDialog(null, "Unable to connect.\nCheck your details and make sure\n you are connected "
            + "with your Network connection."+"\n");
    }
    }
    
    
        }
    
    
