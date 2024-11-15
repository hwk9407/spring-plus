package org.example.expert.domain.todo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchTodoResponse {
    private String title;
    private long managerCount;
    private long commentCount;

    public SearchTodoResponse(Todo todo) {
        this.title = todo.getTitle();
        this.managerCount = todo.getManagers().size();
        this.commentCount = todo.getComments().size();
    }
}
