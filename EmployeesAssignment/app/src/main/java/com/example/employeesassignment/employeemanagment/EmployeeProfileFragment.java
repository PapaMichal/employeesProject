package com.example.employeesassignment.employeemanagment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.employeesassignment.Employee;
import com.example.employeesassignment.R;
import com.example.employeesassignment.databinding.FragmentEmployeeProfileBinding;

public class EmployeeProfileFragment extends Fragment {

    private Employee selectedEmployee;
    private FragmentEmployeeProfileBinding binding;

    public EmployeeProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeeProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Bundle arguments = getArguments();
        if (!arguments.isEmpty()) {
            selectedEmployee = EmployeeProfileFragmentArgs.fromBundle(getArguments()).getEmployee();
            putEmployeeInfoOnDisplay();
        }
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(R.color.md_theme_light_secondary));
        return view;
    }

    private void putEmployeeInfoOnDisplay() {
        binding.tvEmployeeProfileName.setText("Name: " + selectedEmployee.getName());
        binding.tvEmployeeProfileAddress.setText("Address: " + selectedEmployee.getAddress());
        binding.tvEmployeeProfileEducation.setText("Education: " + selectedEmployee.getEducation());
        binding.tvEmployeeProfileEmploymentRate.setText("Employment Rate: " + selectedEmployee.getEmploymentRate());
        binding.tvEmployeeProfileFieldsOfExpertise.setText("Fields Of Expertise: " + selectedEmployee.getFieldsOfExpertiseAsString());
        binding.tvEmployeeProfileHourlyPay.setText("Hourly pay: " + selectedEmployee.getHourlyPay());
        binding.ivEmployeeProfileImage.setImageBitmap(selectedEmployee.getPicture());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
