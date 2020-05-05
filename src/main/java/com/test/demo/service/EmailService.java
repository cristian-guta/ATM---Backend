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
import java.util.logging.Logger;

@Service
public class EmailService {

    public static final String DEST = "results/logs/log.pdf";
    private SenderService senderService;
    private ClientRepository clientRepository;
    private SubscriptionRepository subscriptionRepository;
    private AccountRepository accountRepository;
    private Logger log = Logger.getLogger(EmailService.class.getName());

    @Autowired
    public EmailService(SenderService senderService, ClientRepository clientRepository, SubscriptionRepository subscriptionRepository, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.senderService = senderService;
        this.subscriptionRepository = subscriptionRepository;
        this.clientRepository = clientRepository;
    }

    public void createPDF(Operation operation, Principal principal, Account acc) throws IOException {
        log.info("Creating PDF file...");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        LocalDate date = LocalDate.now();

        FileOutputStream fos = new FileOutputStream(DEST);
        PdfWriter writer = new PdfWriter(fos);

        PdfDocument pdf = new PdfDocument(writer);

        log.info("Setting up font...");
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);

        Document document = new Document(pdf);
        Text text = new Text("ATM - Project");


        text.setFont(font);
        text.setFontColor(Color.RED);

        log.info("Generating paragraphs...");
        Paragraph paragraph1 = new Paragraph();
        paragraph1.add(text);
        document.add(paragraph1);

        log.info("Fetching client...");
        Client client = clientRepository.findByUsername(principal.getName());
        Account account = operation.getAccount();


        document.add(new Paragraph("Transaction details"));

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
        if (operation.getType().toLowerCase() == "payment") {
            paragraph2.add("Date: " + date + "\n");
            paragraph2.add("Account name: " + account.getName() + "\n"); //
            paragraph2.add("Paid amount: " + operation.getAmount() + "\n");
            paragraph2.add("Current Balance: " + account.getAmount() + "\n");
        }

        document.add(paragraph2);

        document.close();
        log.info("Document is generated, starting sendMail(...) procedure...");
        senderService.sendMail(DEST, client.getEmail(), client.getFirstName() + " " + client.getLastName());
    }
}
