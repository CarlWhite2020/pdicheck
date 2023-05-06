package com.eucleia.pdicheck.listener;

import android.view.View;
import android.widget.AdapterView;

public interface ChooseSpinnerListener  {

    void modelSelected(AdapterView<?> parent, View view, int position, long id);

    void yearSelected(AdapterView<?> parent, View view, int position, long id);

}
