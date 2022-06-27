package com.example.accountinggoods.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;


public class ExampleDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Справка")
                .setMessage("Эту операцию необходимо делать, когда в торговую точку поступают новые товары от поставщика. " +
                        "В операции задается сумма поступления по закупочным ценам и сумма поступления по ценам продажи.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();
    }

}
