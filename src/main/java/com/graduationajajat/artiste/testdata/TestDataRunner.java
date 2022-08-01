package com.graduationajajat.artiste.testdata;

import com.graduationajajat.artiste.model.Authority;
import com.graduationajajat.artiste.model.User;
import com.graduationajajat.artiste.repository.ArtRepository;
import com.graduationajajat.artiste.repository.AuthorityRepository;
import com.graduationajajat.artiste.repository.ExhibitionRepository;
import com.graduationajajat.artiste.repository.UserRepository;
import com.graduationajajat.artiste.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    UserService userService;

    @Autowired
    ExhibitionRepository exhibitionRepository;

    @Autowired
    ArtRepository artRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Authority 생성
        Authority authority1 = new Authority("ROLE_USER");
        Authority authority2 = new Authority("ROLE_ADMIN");

        authority1 = authorityRepository.save(authority1);
        authority2 = authorityRepository.save(authority2);

        // 테스트 User 생성
//        User testUser1 = new User(1L, "admin@gmail.com", "admin123!", "admin", "admin", "2000-05-12", 2, "", true, "autho");
//        User testUser2 = new User(2L, "artiste@gmail.com", "artiste123!", "user1", "artiste", "2000-05-12", 2, "", true, "")
//        testUser1 = userRepository.save(testUser1);
//        testUser2 = userRepository.save(testUser2);


    }
}
