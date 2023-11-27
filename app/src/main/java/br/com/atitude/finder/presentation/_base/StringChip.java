package br.com.atitude.finder.presentation._base;

import android.content.Context;
import android.util.AttributeSet;

public class StringChip extends CustomChip<String> {

    public StringChip(Context context) {
        super(context);
    }

    public StringChip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StringChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    String handleDataToDisplay(String data) {
        return data;
    }
}
