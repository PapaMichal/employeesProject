package com.example.employeesassignment.employeelist;

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
import com.example.employeesassignment.databinding.FragmentAddEmployeeBinding;
import com.example.employeesassignment.databinding.FragmentEmployeeListBinding;
import java.util.ArrayList;
import java.util.Collections;

public class EmployeeListFragment extends Fragment {
    private FragmentEmployeeListBinding binding;
    public EmployeeListFragment() {
        // Required empty public constructor
    }
    public static ArrayList<Employee> employeesList = new ArrayList<>();

    private RecyclerView listView;

    private Button btnSortByName, btnSortByEmploymentRate, btnSortByHourlyPay;

    private ArrayList<String> selectedFilters = new ArrayList<>();
    private String currentSearchText = "";
    private SearchView searchView;
    private SortFilterMultiChoiceSpinnerListener fieldsFilterSelectorListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeListBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        initSearchWidgets();
        initWidgets();
        setUpList();
        lookSelected(btnSortByName);
        TextView sp = binding.addEmpSpExpertise;
        setAdapter(employeesList);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] fields = {"Digital Health", "IVD", "V&V", "Regulation", "Quality"};
        fieldsFilterSelectorListener = new SortFilterMultiChoiceSpinnerListener(
                sp,
                fields,
                "Filter By Expertise",
                getContext()
                , new FilterOnClick() {
            @Override
            public void filterList() {
                checkForFilter();
            }
        }
        );
        setOnClickListeners();
        sp.setOnClickListener(fieldsFilterSelectorListener);
        getActivity().findViewById(R.id.toolbar).setBackgroundColor(getResources().getColor(R.color.md_theme_light_primary));
        return v;
    }

    private void unSelectAllSortButtons()
    {
        lookUnSelected(btnSortByName);
        lookUnSelected(btnSortByEmploymentRate);
        lookUnSelected(btnSortByHourlyPay);
    }

    private void lookSelected(Button parsedButton)
    {
        /*
        parsedButton.setTextColor(white);
        parsedButton.setBackgroundColor(red);
         */
    }

    private void lookUnSelected(Button parsedButton)
    {
        /*
        parsedButton.setTextColor(red);
        parsedButton.setBackgroundColor(darkGray);
         */
    }

    private void initWidgets()
    {
        btnSortByName =  binding.btnSortByName;
        btnSortByEmploymentRate = binding.btnSortEmploymentRate;
        btnSortByHourlyPay  = binding.btnSortByHourlyPay;
    }

    private void initSearchWidgets()
    {
        searchView = binding.employeesListSearchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s)
            {
                currentSearchText = s;
                ArrayList<Employee> filteredEmployees = new ArrayList<Employee>();
                checkForFilter();
                return false;
            }
        });
    }

    private void setupData()
    {
        employeesList = SqlHandler.getInstance(getContext()).getEmployees();
    }

    private void setUpList()
    {
        listView = binding.rvEmployeesList;
        setAdapter(employeesList);
    }

    public void setBtnSortByEmploymentRate()
    {
        Collections.sort(employeesList, Employee.employmentRateDsc);
        Collections.reverse(employeesList);
        checkForFilter();
        unSelectAllSortButtons();
        lookSelected(btnSortByEmploymentRate);
    }

    public void sortBtnByName()
    {
        Collections.sort(employeesList, Employee.nameAsc);
        checkForFilter();
        unSelectAllSortButtons();
        lookSelected(btnSortByName);
    }

    public void setBtnSortByHourlyPay()
    {
        Collections.sort(employeesList, Employee.hourlyPayDsc);
        Collections.reverse(employeesList);
        checkForFilter();
        unSelectAllSortButtons();
        lookSelected(btnSortByHourlyPay);
    }

    private boolean doesEmployeeMatchSearch(Employee employee) {
        return currentSearchText.compareTo("") == 0 ||
                employee.getName().toLowerCase().contains(currentSearchText.toLowerCase());
    }

    private boolean doesEmployeeMatchFilters(Employee employee) {
        return !Collections.disjoint(employee.getFieldsOfExpertise(), selectedFilters);
    }

    public void checkForFilter()
    {
        selectedFilters = fieldsFilterSelectorListener.getChosenItemsList();
        boolean allFieldsAreChecked = fieldsFilterSelectorListener.areAllFieldsChecked();
        boolean isSearchTextEmpty = currentSearchText.compareTo("") == 0;
        ArrayList<Employee> filteredEmployees = new ArrayList<>();

        if(allFieldsAreChecked && isSearchTextEmpty) {
            filteredEmployees = new ArrayList<>(employeesList);
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

    private void setAdapter(ArrayList<Employee> employeesList)
    {
        EmployeeAdapter adapter = new EmployeeAdapter(employeesList);
        listView.setAdapter(adapter);
    }


    private void setOnClickListeners() {
        btnSortByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBtnByName();
            }
        });
        btnSortByHourlyPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnSortByHourlyPay();
            }
        });
        btnSortByEmploymentRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnSortByEmploymentRate();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}