package com.example.mytodo.modals;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mytodo.R;
import com.example.mytodo.model.Todo;

public class TodoModal extends Dialog {




    public interface ModalClickListeners {
        void saveListener(Todo todo);
    }
    public interface ModalDeleteListener {
        void deleteListener();
    }
    private Todo todo;
    private EditText inputContent;
    private ImageButton btnSave;
    private ImageButton btnCancel;
    private ImageButton btnDelete;
    private boolean isEditModal;
    private LinearLayout todoLayout;
    private int width;
    private ModalClickListeners listeners;
    private ModalDeleteListener editListener;
    public TodoModal(@NonNull Context context, int width, ModalClickListeners listeners) {
        super(context);
        this.listeners = listeners;
        this.width = width;
        todo = new Todo();
        isEditModal = false;
    }
    public static TodoModal editModal(Context context, int width, Todo todo,
                                      ModalClickListeners listeners, ModalDeleteListener editListener) {
        TodoModal modal = new TodoModal(context, width, listeners);
        modal.todo = todo;
        modal.editListener = editListener;
        modal.isEditModal = true;
        return modal;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_editor_layout);
        setupViews();
        setupClickListeners();
    }

    private void setupViews(){
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
        inputContent = findViewById(R.id.inputTODOContent);
        inputContent.setText(todo.getContent());
        if (isEditModal) {
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }
        todoLayout = findViewById(R.id.todoLayout);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        todoLayout.setLayoutParams(params);
    }
    private void setupClickListeners(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputContent.getText().toString();
                todo.update(content);
                listeners.saveListener(todo);
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editListener.deleteListener();
                dismiss();
            }
        });
    }

}
