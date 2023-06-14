package com.example.PlaceAR.ApplicationListener;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import scala.collection.Seq;

@Component
public class OpenKoreanTextInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // OpenKoreanTextProcessorJava 초기화 코드 작성
        CharSequence normalized1 = OpenKoreanTextProcessorJava.normalize("초기화 테스트1");
        CharSequence normalized2 = OpenKoreanTextProcessorJava.normalize("초기화 테스트2");
        Seq<KoreanTokenizer.KoreanToken> tokens1 = OpenKoreanTextProcessorJava.tokenize(normalized1);
        Seq<KoreanTokenizer.KoreanToken> tokens2 = OpenKoreanTextProcessorJava.tokenize(normalized2);

        System.out.println(tokens1);
        System.out.println(tokens2);
    }
}


