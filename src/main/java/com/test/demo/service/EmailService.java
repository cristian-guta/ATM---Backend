package com.test.demo.service;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.test.demo.model.Account;
import com.test.demo.model.Client;
import com.test.demo.model.Operation;
import com.test.demo.repository.AccountRepository;
import com.test.demo.repository.ClientRepository;
import com.test.demo.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;

@Service
public class EmailService {

    public static final String DEST = "results/logs/log.pdf";
    private SenderService senderService;
    private ClientRepository clientRepository;
    private SubscriptionRepository subscriptionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public EmailService(SenderService senderService, ClientRepository clientRepository, SubscriptionRepository subscriptionRepository, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.senderService = senderService;
        this.subscriptionRepository = subscriptionRepository;
        this.clientRepository = clientRepository;
    }

    public void createPDF(Operation operation, Principal principal, Account acc) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();

        LocalDate date = LocalDate.now();

        FileOutputStream fos = new FileOutputStream(DEST);
        PdfWriter writer = new PdfWriter(fos);

        PdfDocument pdf = new PdfDocument(writer);

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        Document document = new Document(pdf);
        Text text = new Text("ATM - Project");


        text.setFont(font);
        text.setFontColor(Color.RED);

        Paragraph paragraph1 = new Paragraph();
        paragraph1.add(text);
        document.add(paragraph1);

        Client client = clientRepository.findByUsername(principal.getName());
        Account account = operation.getAccount();


        document.add(new Paragraph("Tranzaction details"));

        Paragraph paragraph2 = new Paragraph();

        if (operation.getType().toLowerCase() == "deposit") {
            paragraph2.add("Date: " + date + "\n");
            paragraph2.add("Account name: " + account.getName() + "\n"); // add IBAN
            paragraph2.add("Amount deposited: " + operation.getAmount() + "\n");
            paragraph2.add("Current Balance: " + account.getAmount() + "\n");
        }
        if (operation.getType().toLowerCase() == "withdraw") {
            paragraph2.add("Date: " + date + "\n");
            paragraph2.add("Account name: " + account.getName() + "\n"); // add IBAN
            paragraph2.add("Amount withdrawed: " + operation.getAmount() + "\n");
            paragraph2.add("Current Balance: " + account.getAmount() + "\n");
        }
        if (operation.getType().toLowerCase() == "transfer") {
            paragraph2.add("Date: " + date + "\n");
            paragraph2.add("Receiver's account id: " + acc.getId() + "\n"); // add IBAN
            paragraph2.add("Amount transfered: " + operation.getAmount() + "\n");
            paragraph2.add("Current Balance: " + account.getAmount() + "\n");
        }

        document.add(paragraph2);

        document.close();
        senderService.sendMail(DEST, client.getEmail(), client.getFirstName() + " " + client.getLastName());
    }
}
