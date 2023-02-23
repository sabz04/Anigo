package com.example.anigo.UiHelper;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ExpandableTextView extends AppCompatTextView {

    private static final int MAX_COLLAPSED_LINES = 5;
    private static final String ELLIPSIS = "...";

    private boolean isExpanded = true;
    private int collapsedLines;

    public ExpandableTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.collapsedLines = MAX_COLLAPSED_LINES;
        setOnClickListener(v -> toggle());
    }

    public void setCollapsedLines(int lines) {
        this.collapsedLines = lines;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setEllipsize(null);
        setLines(Integer.MAX_VALUE);
        isExpanded = false;
    }

    public void toggle() {
        isExpanded = !isExpanded;
        setEllipsize(isExpanded ? null : TextUtils.TruncateAt.END);
        setMaxLines(isExpanded ? Integer.MAX_VALUE : collapsedLines);
    }

    public boolean isExpanded() {
        return isExpanded;
    }
}




