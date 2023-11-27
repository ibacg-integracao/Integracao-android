package br.com.atitude.finder.presentation._base;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.chip.Chip;

import br.com.atitude.finder.domain.WeekDay;

public class WeekDayChip extends CustomChip<WeekDay> {

    public WeekDayChip(Context context) {
        super(context);
    }

    public WeekDayChip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WeekDayChip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    String handleDataToDisplay(WeekDay data) {
        return getContext().getString(data.getLocalization());
    }
}
