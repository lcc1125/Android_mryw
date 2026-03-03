package com.example.myapplication.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Question;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * 题目列表适配器
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questions = new ArrayList<>();
    private OnQuestionClickListener listener;

    public interface OnQuestionClickListener {
        void onQuestionClick(Question question, int position);
    }

    public void setOnQuestionClickListener(OnQuestionClickListener listener) {
        this.listener = listener;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_card, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvCategory;
        TextView tvDifficulty;
        TextView tvQuestionContent;
        TextView tvType;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            tvQuestionContent = itemView.findViewById(R.id.tvQuestionContent);
            tvType = itemView.findViewById(R.id.tvType);
        }

        public void bind(Question question) {
            tvCategory.setText(question.getCategoryName() != null ? question.getCategoryName() : "未分类");
            tvDifficulty.setText(question.getDifficultyDisplayName());
            tvQuestionContent.setText(question.getContent());
            tvType.setText("简答题");

            // 设置难度背景色
            int difficultyColor = question.getDifficultyColor();
            tvDifficulty.setBackgroundColor((int) difficultyColor);

            // 点击事件
            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onQuestionClick(question, getAdapterPosition());
                }
            });
        }
    }
}
