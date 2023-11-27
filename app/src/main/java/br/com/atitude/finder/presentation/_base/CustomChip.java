package br.com.atitude.finder.presentation._base;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.chip.Chip;

abstract public class CustomChip<T> extends Chip {

    private T data;
    public CustomChip(Context context) {
        super(context);
        init();
    }

    public CustomChip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public T getData() {
        return data;
    }

    private void init() {
        this.setCheckable(true);
    }

    public void setData(T data) {
        this.setText(handleDataToDisplay(data));
        this.data = data;
    }

    abstract String handleDataToDisplay(T data);
}
