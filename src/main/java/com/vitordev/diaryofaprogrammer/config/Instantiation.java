package com.vitordev.diaryofaprogrammer.config;

import com.vitordev.diaryofaprogrammer.domain.Post;
import com.vitordev.diaryofaprogrammer.domain.User;
import com.vitordev.diaryofaprogrammer.dto.AuthorDTO;
import com.vitordev.diaryofaprogrammer.repository.PostRepository;
import com.vitordev.diaryofaprogrammer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

@Configuration
public class Instantiation implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTF"));

        userRepository.deleteAll();
        postRepository.deleteAll();

        User u1 = new User(null, "Joao", "Joao@gmail.com", "123", sdf.parse("31/01/2005"), new Date());
        User u2 = new User(null, "Maria", "Maria@gmail.com", "456", sdf.parse("15/06/1998"), new Date());
        User u3 = new User(null, "Pedro", "Pedro@gmail.com", "789", sdf.parse("22/11/2000"), new Date());

        userRepository.saveAll(Arrays.asList(u1, u2, u3));

        Post p1 = new Post(null, "Os impactos da IA", "Texto de conteudo", sdf.parse("12/03/2024"), 12, new AuthorDTO(u1));
        Post p2 = new Post(null, "A revolução da tecnologia", "Texto sobre os avanços tecnológicos.", sdf.parse("25/05/2023"), 20, new AuthorDTO(u1));
        Post p3 = new Post(null, "Sustentabilidade no século XXI", "Discussão sobre práticas sustentáveis.", sdf.parse("10/08/2024"), 15, new AuthorDTO(u1));

        postRepository.saveAll(Arrays.asList(p1, p2, p3));

        u1.getPosts().addAll(Arrays.asList(p1, p2, p3));

        userRepository.saveAll(Arrays.asList(u1, u2, u3));

    }
}