package com.internship.flow_appointment_scheduling.infrastructure.mail_service;

import com.internship.flow_appointment_scheduling.features.appointments.entity.Appointment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;
  @Value("${front-end.url}")
  private String frontEndUrl;

  @Override
  public void sendApprovedAppointmentNotification(Appointment appointment) {
    String clientEmail = appointment.getClient().getEmail();
    String staffEmail = appointment.getStaff().getEmail();
    String subject = "Appointment Created Successfully";

    Context context = extractContext(appointment);
    context.setVariable("staffName", appointment.getStaff().getFirstName());

    String clientNotification = templateEngine.process("client-approved-appointment-notification",
        context);
    String staffNotification = templateEngine.process("staff-approved-appointment-notification",
        context);

    try {
      sendEmail(clientEmail, subject, clientNotification);
      sendEmail(staffEmail, subject, staffNotification);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sendNotApprovedAppointmentNotification(Appointment appointment) {
    String clientEmail = appointment.getClient().getEmail();
    String staffEmail = appointment.getStaff().getEmail();
    String subject = "Appointment Request Created Successfully";

    Context context = extractContext(appointment);
    context.setVariable("staffName", appointment.getStaff().getFirstName());

    String clientNotification = templateEngine.process("client-created-appointment-notification",
        context);
    String staffNotification = templateEngine.process("staff-created-appointment-notification",
        context);

    try {
      sendEmail(clientEmail, subject, clientNotification);
      sendEmail(staffEmail, subject, staffNotification);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sendCanceledAppointmentNotificationToClient(Appointment appointment) {
    String clientEmail = appointment.getClient().getEmail();
    String subject = "Appointment Canceled";

    Context context = extractContext(appointment);

    String clientNotification = templateEngine.process("client-canceled-appointment-notification",
        context);

    try {
      sendEmail(clientEmail, subject, clientNotification);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void sendResetPasswordEmail(String jwtToken, String userEmail) {
    String subject = "Password Reset Request";
    String userName = "User";
    String resetLink = frontEndUrl + "/resetPassword?token=" + jwtToken;

    Context context = new Context();
    context.setVariable("userName", userName);
    context.setVariable("resetLink", resetLink);

    String htmlContent = templateEngine.process("password-reset", context);

    try {
      sendEmail(userEmail, subject, htmlContent);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  private void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);
    mailSender.send(message);
  }

  private Context extractContext(Appointment appointment) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    Context context = new Context();
    context.setVariable("clientName", appointment.getClient().getFirstName());
    context.setVariable("serviceName", appointment.getService().getName());
    context.setVariable("startDate", appointment.getStartDate().format(formatter));
    context.setVariable("endDate", appointment.getEndDate().format(formatter));

    return context;
  }
}