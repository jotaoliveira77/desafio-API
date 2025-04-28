package com.projetoapi.api_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String remetente;

    @Autowired
    private JavaMailSender mailSender;

    public String sendEmail(String destinatario, String assunto, String mensagem) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(destinatario);
            message.setSubject(assunto);
            message.setText(mensagem);

            mailSender.send(message);
            System.out.println("Email enviado com sucesso para " + destinatario);
            return "Email enviado";

        } catch (Exception e) {
            System.out.println("Erro ao enviar email: " + e.getMessage());
            return "Erro ao enviar email: " + e.getMessage();
        }
    }
}
