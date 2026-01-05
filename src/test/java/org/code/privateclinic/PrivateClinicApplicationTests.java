package org.code.privateclinic;

import org.code.privateclinic.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PrivateClinicApplicationTests {

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void contextLoads() {
    }

}
