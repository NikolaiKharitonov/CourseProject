package com.example.accountinggoods.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.accountinggoods.R;

public class ChoiseOperationTypeDialog extends DialogFragment {
    int position = 0 ;

    public interface  ChoiseOperationTypeListener {
        void onPositiveButtonClicked(String[] list, int position);
        void onNegativeButtonClicked();
    }
    ChoiseOperationTypeListener listener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ChoiseOperationTypeListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + "");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] list = getActivity().getResources().getStringArray(R.array.choise_operations);
        builder.setTitle("Выберите операцию")
                .setSingleChoiceItems(list, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        position = i;
                    }
                })
                .setPositiveButton("Принять", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onPositiveButtonClicked(list, position);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onNegativeButtonClicked();
                    }
                });
                return  builder.create();
    }
}
