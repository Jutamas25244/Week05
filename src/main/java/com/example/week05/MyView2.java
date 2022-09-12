package com.example.week05;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

import static com.vaadin.flow.component.notification.Notification.Position.BOTTOM_START;

@Route(value = "index2")
public class MyView2 extends HorizontalLayout {
    private TextField tf1, tf2;
    private Button btn1, btn2, btn3, btn4;
    private TextArea ta1, ta2;
    private ComboBox<String> cb1, cb2;
    private VerticalLayout vl1, vl2;
    private Notification nf;

    public MyView2(){
        tf1 = new TextField("Add Word");
        tf2 = new TextField("Add Sentence");
        btn1 = new Button("Add Good Word");
        btn2 = new Button("Add Bad Word");
        btn3 = new Button("Add Sentence");
        btn4 = new Button("Show Sentence");
        ta1 = new TextArea("Good Sentence");
        ta2 = new TextArea("Bad Sentence");
        cb1 = new ComboBox<String>("Good Words");
        cb2 = new ComboBox<String>("Bad Words");
        vl1 = new VerticalLayout();
        vl2 = new VerticalLayout();

        tf1.setWidthFull();
        tf2.setWidthFull();
        btn1.setWidthFull();
        btn2.setWidthFull();
        btn3.setWidthFull();
        btn4.setWidthFull();
        ta1.setWidthFull();
        ta2.setWidthFull();
        cb1.setWidthFull();
        cb2.setWidthFull();
        vl1.setWidthFull();
        vl2.setWidthFull();

        cb1.setItems(new Word().goodWords);
        cb2.setItems(new Word().badWords);

        vl1.add(tf1, btn1, btn2, cb1, cb2);
        vl2.add(tf2,btn3, ta1, ta2, btn4);
        this.add(vl1, vl2);

        btn1.addClickListener(event -> {
            String s = tf1.getValue();
            ArrayList<String> result = WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/addGood/"+s)
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            cb1.setItems(result);
        });

        btn2.addClickListener(event -> {
            String s = tf1.getValue();
            ArrayList<String> result = WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/addBad/"+s)
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            cb2.setItems(result);
        });

        btn3.addClickListener(event -> {
            String s = tf2.getValue();
            String result = WebClient
                    .create()
                    .post()
                    .uri("http://localhost:8080/proof/"+s)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            nf = Notification.show(result, 3000, BOTTOM_START);
        });

        btn4.addClickListener(event -> {
            Sentence result = WebClient
                    .create()
                    .get()
                    .uri("http://localhost:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
            ta1.setValue(result.goodSentences+"");
            ta2.setValue(result.badSentences+"");
        });
    }

}
