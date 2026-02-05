package demo.mcp.repository;

import demo.mcp.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRepoTest {

    @Autowired
    StudentRepo studentRepo;

    @Test
    public void testSaveStudent(){
        Student johnDoe = studentRepo.save(Student.builder()
                .name("John Doe")
                .marks(85)
                .rank(1)
                .build());
        assertNotNull(johnDoe);
        assertNotNull(johnDoe.getId());
        assertEquals("John Doe", johnDoe.getName());
    }

}