package com.example.employeesassignment.userlogin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.employeesassignment.InputPatterns;
import com.example.employeesassignment.MainActivity;
import com.example.employeesassignment.R;
import com.example.employeesassignment.SessHelper;
import com.example.employeesassignment.database.SqlHandler;
import com.example.employeesassignment.databinding.FragmentRegisterBinding;


public class Register extends Fragment {

    private EditText etUsername, etPass, etRepeatPass, etEmail;
    private SqlHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentRegisterBinding binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(R.color.colorPrimaryRegister));

        //Get edit text objects of the input fields
        etUsername = binding.etRegisterUsername;
        etPass =  binding.etRegisterPass;
        etRepeatPass = binding.etRegisterRepeatPass;
        etEmail = binding.etRegisterEmail;
        TextView tvRegisterErrorPrompt = binding.tvRegistrationErrorPrompt;
        db = SqlHandler.getInstance(v.getContext());
        EditText[] etArrOfFields = {etUsername, etPass, etRepeatPass, etEmail};
        for (EditText field: etArrOfFields) {
            field.addTextChangedListener(new AfterTextChangeValidator(field));
            field.setOnEditorActionListener(new EmptyTextListener(field));
        }

        binding.btnRegister.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String pass = etPass.getText().toString();
            if (!isAnyRequiredFieldEmpty() && areAllFieldsValid()) {
                if (db.register(username, pass, email)) {
                    Navigation.findNavController(view).navigate(R.id.action_register_to_employeeList); //Success
                    SessHelper.getInstance(getActivity()).login(username, email);
                    ((MainActivity)(getActivity())).setNavHeaderUserAndEmail();
                    return;
                }
            }
            tvRegisterErrorPrompt.setText("Registration failed");
        });

        v.findViewById(R.id.tv_login_referer).setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_register_to_login));
        return v;
    }

    private boolean isUsernameTaken() { return db.doesUserExist(etUsername.getText().toString()); }
    private boolean isEmailTaken() { return db.doesEmailExist(etEmail.getText().toString()); }
    public boolean isPasswordConfirmationValid() {return etRepeatPass.getText().toString().compareTo(etPass.getText().toString()) == 0; }

    static boolean isTextViewEmpty(TextView tv) {
        return tv.getText().toString().isEmpty();
    }

    private boolean isAnyRequiredFieldEmpty() {
        boolean emptyFound = false;
        EditText[] etArr = {etUsername, etPass, etRepeatPass, etEmail};
        for (EditText et: etArr) {
            if (isTextViewEmpty(et)) {
                et.setError("This field is required");
                emptyFound = true;
            }
        }
        return emptyFound;
    }

    private boolean areAllFieldsValid() {
        boolean areAllFieldsValid = InputPatterns.USERNAME.validateAndDisplay(etUsername) &&
                InputPatterns.PASSWORD.validateAndDisplay(etPass) &&
                InputPatterns.EMAIL.validateAndDisplay(etEmail) &&
                isPasswordConfirmationValid() &&
                !isUsernameTaken() &&
                !isEmailTaken();
        if (!isPasswordConfirmationValid()) {
            etRepeatPass.setError("Passwords do not match");
        }
        return areAllFieldsValid;
    }

    private class AfterTextChangeValidator implements TextWatcher {
        private final EditText et;
        AfterTextChangeValidator(EditText et) {
            this.et = et;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s)  {
            switch (et.getId()) {
                case R.id.et_register_username:
                    if (InputPatterns.USERNAME.validateAndDisplay(et)) {
                        if (isUsernameTaken()) {
                            et.setError("This user is already registered.");
                        }
                    }
                    break;
                case R.id.et_register_email:
                    if (InputPatterns.EMAIL.validateAndDisplay(et)) {
                        if (isEmailTaken()) {
                            et.setError("This email is already taken.");
                        }
                    }
                    break;
                case R.id.et_register_pass:
                    InputPatterns.PASSWORD.validateAndDisplay(et);
                    break;

                case R.id.et_register_repeat_pass:
                    if (!isPasswordConfirmationValid()) {
                        et.setError("Passwords do not match");
                }
            }
        }
    }
    private static class EmptyTextListener implements TextView.OnEditorActionListener {
        private final EditText et;

        public EmptyTextListener(EditText editText) {
            this.et = editText;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (isTextViewEmpty(et)) {
                    et.setError("This field is required.");
                }
            }
            return false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}