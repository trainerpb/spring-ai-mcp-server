package demo.mcp.service;

import demo.mcp.model.Student;
import demo.mcp.repository.StudentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentCrudTool {

    private final StudentRepo studentRepo;

    @McpTool(description = "Create a new student with name, marks and rank")
    public Student createStudent(
            @McpToolParam(description = "Name of the student")
            String name,
            @McpToolParam(description = "Marks obtained by the student")
            int marks,
            @McpToolParam(description = "Rank secured by the student")
            int rank){
        Student student = Student.builder()
                .name(name)
                .marks(marks)
                .rank(rank)
                .build();
        return studentRepo.save(student);
    }

    @McpTool(description = "Get all students")
    public List<Student> getAllStudents(){
        return studentRepo.findAll();
    }
}
