package com.example.accountinggoods.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.accountinggoods.R;

public class AddSupplierDialog extends AppCompatDialogFragment {
    private EditText titleET;
    private EditText phoneET;
    private EditText addressET;
    private AddSupplierDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_suppliers_dialog, null);
        builder.setView(view)
                .setTitle("Создание поставщика")
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Принять", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String title = titleET.getText().toString();
                        String phone = phoneET.getText().toString();
                        String address = addressET.getText().toString();
                        listener.applyText(title, phone, address);
                    }
                });
        titleET = view.findViewById(R.id.supNameET);
        phoneET = view.findViewById(R.id.supPhoneET);
        addressET = view.findViewById(R.id.supAddressET);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (AddSupplierDialogListener) context;
        }catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must");
        }

    }

    public interface AddSupplierDialogListener{
        void applyText(String title, String phone, String address);
    }
}
