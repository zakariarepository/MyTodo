package com.example.mytodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mytodo.Database.DbController;
import com.example.mytodo.Database.SQLExecution;
import com.example.mytodo.adapters.EmptyTodoListAdapter;
import com.example.mytodo.adapters.ListAdapter;
import com.example.mytodo.modals.TodoModal;
import com.example.mytodo.model.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnEditList;
    private RecyclerView todoRecyclerView;
    private ListAdapter listAdapter;
    private RecyclerView.LayoutManager manager;
    private FloatingActionButton btnAddTODO;
    private ArrayList<Todo> todoList;
    private boolean isEditMode = false;
    private int width;
    private ListAdapter.TodoListener todoListener;

    /**/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        clickListeners();
        setupAndFillList();
    }

    public void initVariables() {
        btnEditList = findViewById(R.id.btnEditList);
        todoRecyclerView = findViewById(R.id.recyclerView);
        btnAddTODO = findViewById(R.id.btnAddTODO);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(
                displayMetrics);
        width = displayMetrics.widthPixels;

        todoListener = new ListAdapter.TodoListener() {
            @Override
            public void oncheckChange(Todo todo, int pos, boolean checked) {
                todoList.get(pos).setCompleted(checked);
            }
            @Override
            public void onLongClick(Todo todo, int pos) {
                TodoModal.editModal(MainActivity.this, (int) (width * 0.8), todo, new
                        TodoModal.ModalClickListeners() {
                            @Override
                            public void saveListener(Todo todo) {
                                updateInDatabase(todo,pos);
                            }
                        }, new TodoModal.ModalDeleteListener() {
                    @Override
                    public void deleteListener() {
                        deleteFromDatabase(pos);
                    }
                }).show();
            }
            @Override
            public void onDelete(int pos) {
                deleteFromDatabase(pos);
            }
        };
    }

    public void clickListeners() {
        btnEditList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditMode = !isEditMode;
                if (isEditMode) {
                    btnEditList.setImageResource(R.drawable.ic_done);
                } else {
                    btnEditList.setImageResource(R.drawable.ic_edit);
                }
                if(listAdapter!=null)
                    listAdapter.changedDisplay(isEditMode);
            }
        });
        btnAddTODO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TodoModal(MainActivity.this, (int) (width * 0.8), new
                        TodoModal.ModalClickListeners() {
                            @Override
                            public void saveListener(Todo todo) {
                                saveToDatabase(todo);
                            }
                        }).show();
            }
        });

    }
    public void setupAndFillList(){
        new SQLExecution(this).runWithResult(new SQLExecution.SQLWithReturn() {
            @Override
            public void execute() {
                todoList = new
                        DbController(MainActivity.this).getAllTodos();
            }
            @Override
            public void executeOnUI() {
                manager = new
                        LinearLayoutManager(getApplicationContext());
                todoRecyclerView.setLayoutManager(manager);
                listAdapter = new ListAdapter(todoList, isEditMode, todoListener);
                if(todoList.size() == 0){
                    todoRecyclerView.setAdapter(new EmptyTodoListAdapter());
                    return;
                }
                todoRecyclerView.setAdapter(listAdapter);
            }
        });
    }
    private void deleteFromDatabase(int pos) {
        new SQLExecution(this).runWithResult(new SQLExecution.SQLWithReturn() {
            @Override
            public void execute() {
                new
                        DbController(MainActivity.this).deleteTodo(todoList.get(pos).getId());
            }
            @Override
            public void executeOnUI() {
                listAdapter.removeTodo(pos);
                if(todoList.size()==0) todoRecyclerView.setAdapter(new
                        EmptyTodoListAdapter());
            }
        });
    }
    private void updateInDatabase(Todo todo, int pos) {
        new SQLExecution(this).runWithResult(new SQLExecution.SQLWithReturn() {
            @Override
            public void execute() {
                new DbController(MainActivity.this).updateTodo(todo);
            }
            @Override
            public void executeOnUI() {
                listAdapter.updateTodo(pos, todo);
                todoRecyclerView.smoothScrollToPosition(0);
            }
        });
    }
    private void saveToDatabase(Todo todo) {
        new SQLExecution(this).runWithResult(new SQLExecution.SQLWithReturn() {
            @Override
            public void execute() {
                int id = new DbController(MainActivity.this).saveToDatabase(todo);
                if (id != -1) {
                    todo.setId(id);
                }
            }
            @Override
            public void executeOnUI() {
                if(todoList.size() == 0) todoRecyclerView.setAdapter(listAdapter);
                listAdapter.addNewTodo(todo);
                todoRecyclerView.smoothScrollToPosition(0);
            }
        });
    }
}
