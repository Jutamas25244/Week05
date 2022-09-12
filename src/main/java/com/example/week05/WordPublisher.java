package com.example.week05;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class WordPublisher {
    protected Word words = new Word();
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/addBad/{word}", method = RequestMethod.POST )
    public ArrayList<String> addBadWord(@PathVariable("word") String s){
        words.badWords.add(s);
        return words.badWords;
    }
    @RequestMapping(value = "/delBad/{word}", method = RequestMethod.GET )
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s){
        words.badWords.remove(String.valueOf(s));
        return words.badWords;
    }
    @RequestMapping(value = "/addGood/{word}", method = RequestMethod.POST )
    public ArrayList<String> addGoodWord(@PathVariable("word") String s){
        words.goodWords.add(s);
        return words.goodWords;
    }
    @RequestMapping(value = "/delGood/{word}", method = RequestMethod.GET )
    public ArrayList<String> deleteGoodWord(@PathVariable("word") String s){
        words.goodWords.remove(String.valueOf(s));
        return words.goodWords;
    }
    @RequestMapping(value = "/proof/{sentence}", method = RequestMethod.POST)
    public String proofSentence(@PathVariable("sentence") String s){
        String count = "";
        for(String i : words.goodWords){
            if(s.indexOf(i) !=-1){
                count+="g";
                break;
            }
        }

        for(String i : words.badWords){
            if(s.indexOf(i) !=-1){
                count+="b";
                break;
            }
        }
        if(count.equals("g")){
            rabbitTemplate.convertAndSend("Direct", "good", s);
            return "Found Good Word";
        } else if (count.equals("b")) {
            rabbitTemplate.convertAndSend("Direct", "bad", s);
            return "Found Bad Word";
        } else if (count.equals("gb")) {
            rabbitTemplate.convertAndSend("Fanout", "", s);
            return "Found Good Word and Bad Word";
        }
        return "";
    }
    @RequestMapping(value = "/getSentence", method = RequestMethod.GET)
    public Sentence getSentence() {
        Object sentences = rabbitTemplate.convertSendAndReceive("Direct", "", "");
        return ((Sentence) sentences);
    }
}
