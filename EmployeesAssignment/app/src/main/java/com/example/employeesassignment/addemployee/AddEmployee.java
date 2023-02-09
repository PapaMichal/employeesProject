package com.example.employeesassignment.addemployee;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.employeesassignment.R;
import com.example.employeesassignment.database.SqlHandler;
import com.example.employeesassignment.databinding.FragmentAddEmployeeBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class AddEmployee extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private final static String[] EMPLOYMENT_RATES_ARR = {"100", "80", "60", "40", "20"};
    private EditText etFullName, etHourlyPay, etAddress, etEducation;
    private TextView tvSelectExpertiseFields, tvSelectEmploymentRate;
    private Button btnSubmit, btnImage;
    private ImageView ivImage;
    private Bitmap bitmapImage;
    private ActivityResultLauncher<String> pickImageLauncher;
    private SqlHandler db;
    private FragmentAddEmployeeBinding binding;

    public String tvToStr(TextView tv) {
        return tv.getText().toString();
    }

    public int tvToInt(TextView tv) {
        return Integer.parseInt(tvToStr(tv));
    }

    public AddEmployee() {
    }

    boolean isTextViewEmpty(TextView tv) {
        return tv.getText().toString().isEmpty();
    }

    private void setTextInputLayoutError(EditText childEt, String errorMsg) {
        ((TextInputLayout)childEt.getParent().getParent()).setError(errorMsg);
    }

    private boolean isAnyRequiredFieldEmpty() {
        boolean emptyFound = false;
        EditText[] etArr = {etFullName, etAddress, etHourlyPay, etEducation};
        for (EditText et: etArr) {
            if (isTextViewEmpty(et)) {
                setTextInputLayoutError(et, "This field is required");
                emptyFound = true;
            }
        }
        return emptyFound || isTextViewEmpty(tvSelectEmploymentRate);
    }

    private boolean areAllFieldsValid() {
        return InputPatterns.NAME.validateAndDisplay(etFullName) &&
                InputPatterns.ADDRESS.validateAndDisplay(etFullName) &&
                InputPatterns.HOURLY_PAY.validateAndDisplay(etHourlyPay) &&
                InputPatterns.EDUCATION.validateAndDisplay(etEducation) &&
                !db.doesEmployeeExist(etFullName.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickImageLauncher = registerForActivityResult(new
                        ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            try {
                                bitmapImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                                ivImage.setImageBitmap(bitmapImage);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }


    class AfterTextChangeValidator implements TextWatcher {
        private EditText et;
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
            String str = et.getText().toString();
            switch (et.getId()) {
                case R.id.add_emp_et_full_name:
                    if (InputPatterns.NAME.validateAndDisplay(et)) {
                        if (db.doesEmployeeExist(et.getText().toString())) {
                            setTextInputLayoutError(et, "This employee is already registered.");
                        }
                    }
                    break;
                case R.id.add_emp_et_hourly_pay:
                    InputPatterns.HOURLY_PAY.validateAndDisplay(et);
                    break;
                case R.id.add_emp_et_address:
                    InputPatterns.ADDRESS.validateAndDisplay(et);
                    break;
                case R.id.add_emp_et_education:
                    InputPatterns.EDUCATION.validateAndDisplay(et);
            }
        }
    }

    private class EmptyTextListener implements TextView.OnEditorActionListener {
        private final EditText et;

        public EmptyTextListener(EditText editText) {
            this.et = editText;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                if (isTextViewEmpty(et)) {
                    ((TextInputLayout)et.getParent().getParent()).setError("This field is required.");
                }
            }
            return false;
        }
    }

    public void showSuccessfulRegister(View v) {
        new AlertDialog.Builder(v.getContext())
                .setTitle("Sucess!\n")
                .show();
    }

    public void showFailedRegister(View v) {
        new AlertDialog.Builder(v.getContext())
                .setTitle("Registration failed")
                .show();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddEmployeeBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        etFullName = binding.addEmpEtFullName;
        etAddress = binding.addEmpEtAddress;
        etHourlyPay = binding.addEmpEtHourlyPay;
        etEducation = binding.addEmpEtEducation;
        btnSubmit = binding.addEmpBtnSubmit;
        tvSelectExpertiseFields = binding.addEmpSpExpertise;
        tvSelectEmploymentRate = binding.addEmpSpEmploymentRate;
        ivImage = binding.addEmpIvImage;
        btnImage = binding.addEmpImageBtn;
        db = SqlHandler.getInstance(v.getContext());
        EditText etArrOfFields[] = {etFullName, etAddress, etHourlyPay, etEducation};
        for (EditText field: etArrOfFields) {
            field.addTextChangedListener(new AfterTextChangeValidator(field));
            field.setOnEditorActionListener(new EmptyTextListener(field));
        }

        //Initialize item arrays
        String[] fieldsOfExpertiseArr = db.getFieldsOfExpertise();
        tvSelectEmploymentRate.setOnClickListener(new SingleChoiceSpinnerListener(tvSelectEmploymentRate,
                EMPLOYMENT_RATES_ARR,"Select Employment Rate", getContext()));
        MultiSChoiceLSpinnerListener selectExpertiseListener = new MultiSChoiceLSpinnerListener(tvSelectExpertiseFields,
                fieldsOfExpertiseArr, "Select Expertise Fields", getContext());
        tvSelectExpertiseFields.setOnClickListener(selectExpertiseListener);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageLauncher.launch("image/*");
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isAnyRequiredFieldEmpty() && areAllFieldsValid()) {
                    if (db.addEmployee(
                            tvToStr(binding.addEmpEtFullName),
                            tvToStr(etAddress),
                            tvToStr(etEducation),
                            tvToInt(etHourlyPay),
                            tvToInt(tvSelectEmploymentRate),
                            selectExpertiseListener.getChosenOptions(),
                            bitmapImage)
                    ) {
                        showSuccessfulRegister(v);
                    }
                    else {
                        showFailedRegister(v);
                    }
                }
            }
        });
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(R.color.md_theme_light_primary));
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}