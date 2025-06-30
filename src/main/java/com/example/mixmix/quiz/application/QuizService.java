package com.example.mixmix.quiz.application;

import com.example.mixmix.ai.dto.AiResponseDto;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.MemberNotFoundException;
import com.example.mixmix.quiz.api.dto.response.QuizDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final ChatClient chatClient;
    private final MemberRepository memberRepository;

    @Value("${questions.korea}")
    private String koreaQuestions;

    @Value("${questions.global}")
    private String globalQuestions;

    @Transactional
    public QuizDto askForAdvice(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        String selectedQuestions = member.getNationality().equalsIgnoreCase("korea") ? koreaQuestions : globalQuestions;

        ChatResponse response = callChat(selectedQuestions);
        if (response == null) {
            response = callChat(selectedQuestions);
        }

        return parseQuiz(response.getResult().getOutput().getContent());
    }

    private ChatResponse callChat(String prompt) {
        return chatClient.call(
                new Prompt(
                        prompt,
                        OpenAiChatOptions.builder()
                                .withTemperature(0.4F)
                                .withFrequencyPenalty(0.7F)
                                .withModel("gpt-4o")
                                .build()
                ));
    }

    private QuizDto parseQuiz(String quizText) {
        // 문자열을 줄바꿈으로 분리
        String[] lines = quizText.split("\n");

        // 문제, 선택지, 정답을 파싱
        String question = lines[0].replace("문제: ", "").trim();
        String option1 = lines[2].replace("1. ", "").trim();
        String option2 = lines[3].replace("2. ", "").trim();
        String option3 = lines[4].replace("3. ", "").trim();
        String option4 = lines[5].replace("4. ", "").trim();
        String answer = lines[6].replace("정답: ", "").trim();

        // DTO 생성 및 반환
        return QuizDto.of(question, option1, option2, option3, option4, answer);
    }

    @Transactional
    public boolean chooseTheCorrectAnswer(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        member.incrementStreak();

        return true;
    }

    public boolean chooseTheIncorrectAnswer() {
        return false;
    }
}