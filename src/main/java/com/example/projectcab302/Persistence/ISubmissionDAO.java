package com.example.projectcab302.Persistence;

import com.example.projectcab302.Model.Submission;
import java.util.List;

public interface ISubmissionDAO {
    void createSubmission(Submission submission);
    Submission getSubmissionById(int id);
    List<Submission> getSubmissionsByUser(int userId);
    List<Submission> getSubmissionsByCourse(String courseName);
    List<Submission> getAllSubmissions();
    void deleteSubmission(int id);
    void clearSubmissions();
}
