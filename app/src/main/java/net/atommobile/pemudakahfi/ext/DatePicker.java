package net.atommobile.pemudakahfi.ext;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

/**
 * Created by faizlidwibrido on 9/21/16.
 */

public class DatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    EditText _editText;
    private int _day;
    private int _month;
    private int _birthYear;
    private Context _context;

    public DatePicker(Context context, EditText editTextL)
    {
        this._editText = editTextL;
        this._editText.setOnClickListener(this);
        this._context = context;
    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        String day = "";
        String month = "";

        if(_day < 10){
            day = "0" + String.valueOf(_day);
        } else {
            day = String.valueOf(_day);
        }

        if(_month < 10){
            month = "0" + String.valueOf(_month);
        } else {
            month = String.valueOf(_month);
        }

        _editText.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(_birthYear).append("-").append(month).append("-").append(day));
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int i, int i1, int i2) {
        _birthYear = i;
        _month = i1 + 1;
        _day = i2;
        updateDisplay();
    }

    @Override
    public void onClick(View view) {

        String editText = _editText.getText().toString();
        if(editText.equals("") || editText.equals("0000-00-00")){

            _day = 01;
            _month = 01;
            _birthYear = 1990;

        } else {

            String[] separated = editText.split("-");
            _birthYear = Integer.parseInt(separated[0]);
            _month = Integer.parseInt(separated[1]);
            _day = Integer.parseInt(separated[2]);

        }

        DatePickerDialog dialog = new DatePickerDialog(_context, this,
                _birthYear, (_month - 1), _day);
        dialog.show();
    }

}