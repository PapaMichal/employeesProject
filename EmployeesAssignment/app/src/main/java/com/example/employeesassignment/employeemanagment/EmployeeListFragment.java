package com.example.employeesassignment.employeemanagment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeesassignment.Employee;
import com.example.employeesassignment.R;
import com.example.employeesassignment.database.SqlHandler;
import com.example.employeesassignment.databinding.FragmentEmployeeListBinding;
import com.example.employeesassignment.spinner.SortFilterMultiChoiceSpinnerListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EmployeeListFragment extends Fragment {
    private FragmentEmployeeListBinding binding;
    //Views
    private RecyclerView listView;
    private Button btnSortByName, btnSortByEmploymentRate, btnSortByHourlyPay;
    private TextView expertiseSpinner;
    private SearchView searchView;

    public ArrayList<Employee> employeesList = new ArrayList<>();
    private EmployeeFilter employeeFilter;

    public EmployeeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEmployeeListFromDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeListBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        btnSortByName =  binding.btnSortByName;
        btnSortByEmploymentRate = binding.btnSortEmploymentRate;
        btnSortByHourlyPay  = binding.btnSortByHourlyPay;
        expertiseSpinner = binding.addEmpSpExpertise;
        searchView = binding.employeesListSearchView;
        listView = binding.rvEmployeesList;

        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        employeeFilter = new EmployeeFilter();
        setAdapter(employeesList);
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(R.color.md_theme_light_primary));
        return v;
    }

    private void setEmployeeListFromDatabase()
    {
        employeesList = SqlHandler.getInstance(getContext()).getEmployees();
    }

    private void setAdapter(ArrayList<Employee> employeesList)
    {
        EmployeeAdapter adapter = new EmployeeAdapter(employeesList);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class EmployeeFilter {
        private ArrayList<String> selectedExpertiseFilters;
        SortFilterMultiChoiceSpinnerListener expertiseFieldsSpinnerListener;
        private String currentSearchText;

        public EmployeeFilter() {
            currentSearchText = "";
            setOnClickListeners();
            selectedExpertiseFilters = new ArrayList<>();
        }

        private void unSelectAllSortButtons()
        {
            lookUnSelected(btnSortByName);
            lookUnSelected(btnSortByEmploymentRate);
            lookUnSelected(btnSortByHourlyPay);
        }

        private void lookSelected(Button button)
        {
            button.setTextColor(getResources().getColor(R.color.md_theme_dark_onBackground));
            button.setBackgroundColor(getResources().getColor(R.color.md_theme_dark_tertiaryContainer));
        }

        private void lookUnSelected(Button button)
        {
            button.setTextColor(Color.parseColor("#006A60"));
            button.setBackgroundColor(-1);
        }

        private void applyFilterAndDisplayBtnSelect(Button button, Comparator<Employee> comparator) {
            Collections.sort(employeesList, comparator);
            filterEmployees();
            unSelectAllSortButtons();
            lookSelected(button);
        }

        private boolean doesEmployeeMatchSearch(Employee employee) {
            return currentSearchText.compareTo("") == 0 ||
                    employee.getName().toLowerCase().contains(currentSearchText.toLowerCase());
        }

        private boolean doesEmployeeMatchFilters(Employee employee) {
            return !Collections.disjoint(employee.getFieldsOfExpertise(), selectedExpertiseFilters);
        }

        private void filterEmployees()
        {
            selectedExpertiseFilters = expertiseFieldsSpinnerListener.getChosenItemsList();
            boolean allFieldsAreChecked = expertiseFieldsSpinnerListener.areAllFieldsChecked();
            boolean isSearchTextEmpty = currentSearchText.compareTo("") == 0;
            ArrayList<Employee> filteredEmployees = new ArrayList<>();

            if(allFieldsAreChecked && isSearchTextEmpty) {
                filteredEmployees = employeesList;
            }
            else {
                for (Employee employee : employeesList) {
                    if (doesEmployeeMatchSearch(employee) && doesEmployeeMatchFilters(employee)) {
                        filteredEmployees.add(employee);
                    }
                }
            }
            setAdapter(filteredEmployees);
        }

        private void setOnClickListeners() {
            btnSortByName.setOnClickListener(view -> applyFilterAndDisplayBtnSelect(btnSortByName, Employee.nameAsc));
            btnSortByHourlyPay.setOnClickListener(view -> applyFilterAndDisplayBtnSelect(btnSortByHourlyPay, Employee.hourlyPayDsc));
            btnSortByEmploymentRate.setOnClickListener(view -> applyFilterAndDisplayBtnSelect(btnSortByEmploymentRate, Employee.employmentRateDsc));
            String[] fields = SqlHandler.getInstance(getContext()).getFieldsOfExpertise();
            expertiseFieldsSpinnerListener = new SortFilterMultiChoiceSpinnerListener(
                    expertiseSpinner,
                    fields,
                    "Filter By Expertise",
                    getContext()
                    , () -> employeeFilter.filterEmployees());
            expertiseSpinner.setOnClickListener(expertiseFieldsSpinnerListener);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s)
                {
                    currentSearchText = s;
                    employeeFilter.filterEmployees();
                    return false;
                }
            });
        }
    }
}