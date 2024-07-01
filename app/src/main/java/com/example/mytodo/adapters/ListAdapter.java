package com.example.mytodo.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.R;
import com.example.mytodo.model.Todo;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private final ArrayList<Todo> todoList;
    private boolean isEditMode;
    private final TodoListener listeners;
    public interface TodoListener {
        void oncheckChange(Todo todo, int pos, boolean checked);
        void onLongClick(Todo todo, int pos);
        void onDelete(int pos);
    }

    public void addNewTodo(Todo todo) {
        todoList.add(0, todo);
        notifyItemInserted(0);
        notifyItemRangeChanged(0, todoList.size());
    }
    public void removeTodo(int pos) {
        todoList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, todoList.size());
    }
    public void updateTodo(int pos, Todo todo) {
        todoList.remove(pos);
        todoList.add(0, todo);
        notifyItemChanged(pos);
        notifyItemRangeChanged(0, todoList.size());
    }


    public ListAdapter(ArrayList<Todo> todoList, boolean isEditMode, TodoListener listeners) {
        super();
        this.todoList = todoList;
        this.isEditMode = isEditMode;
        this.listeners = listeners;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                 parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.todocard, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int
            position) {
        Todo todo = todoList.get(position);
        holder.textTODO.setText(todo.getContent());
        displaySuitedButtons(holder, todo);
        checkUncheck(todo.isCompleted(), holder);
        holder.textTimeAgo.setText(new
                PrettyTime().format(todo.getDate()));
        holder.btnCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                listeners.oncheckChange(todo, position,
                        !todo.isCompleted());
                notifyItemChanged(position);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                listeners.onDelete(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, todoList.size());
            }
        });
        holder.card.setOnLongClickListener(new  View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listeners.onLongClick(todo, position);
                return false;
            }
        });
    }
    private void displaySuitedButtons(ViewHolder holder, Todo
            todo) {
        if (!isEditMode) {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnCheck.setVisibility(View.VISIBLE);
            checkUncheck(todo.isCompleted(), holder);
        } else {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnCheck.setVisibility(View.GONE);
        }
    }
    private void checkUncheck(boolean completed, ViewHolder
            holder) {
        if (completed) {
            holder.btnCheck.setImageResource(R.drawable.ic_check_full);
            holder.textTODO.setTextColor(Color.rgb(67, 160, 71));
        } else {
            holder.btnCheck.setImageResource(R.drawable.ic_check_empty);
            holder.textTODO.setTextColor(Color.BLACK);
        }
    }
    public void changedDisplay(boolean mode) {
        this.isEditMode = mode;
        notifyItemRangeChanged(0, todoList.size());
    }
    @Override
    public int getItemCount() {
        return todoList.size() ;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView card;
        private ImageButton btnCheck, btnDelete;
        private TextView textTODO;
        private TextView textTimeAgo;
        private boolean isTimeShown = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            btnCheck = itemView.findViewById(R.id.btnCheck);
            textTODO = itemView.findViewById(R.id.textTODO);
            textTimeAgo = itemView.findViewById(R.id.textTimeAgo);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isTimeShown) {
                        textTODO.setMaxLines(Integer.MAX_VALUE);
                        textTimeAgo.setVisibility(View.VISIBLE);
                    } else {
                        textTODO.setMaxLines(2);
                        textTimeAgo.setVisibility(View.GONE);
                    }
                    isTimeShown = !isTimeShown;
                }
            });
        }
    }

}
