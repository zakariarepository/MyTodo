package com.example.mytodo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.R;

public class EmptyTodoListAdapter extends RecyclerView.Adapter<EmptyTodoListAdapter.ViewHolder>{

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                 parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.todo_empty_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int
            position) {
    }
    @Override
    public int getItemCount() {
        return 1;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
