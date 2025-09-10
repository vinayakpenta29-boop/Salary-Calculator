package com.example.salarycalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText baseSalary, taxAmount, medicalAmount, year, month, leaveDays, dabbaUnits, schemeAmount;
    Button calculate;
    TextView netSalaryDescription, netSalaryAmount;
    LinearLayout resultLinesLayout;
    RadioGroup pfRadioGroup, schemeRadioGroup;
    RadioButton pfYes, pfNo, schemeYes, schemeNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseSalary = findViewById(R.id.baseSalary);
        taxAmount = findViewById(R.id.taxAmount);
        medicalAmount = findViewById(R.id.medicalAmount);
        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        leaveDays = findViewById(R.id.leaveDays);
        dabbaUnits = findViewById(R.id.dabbaUnits);

        pfRadioGroup = findViewById(R.id.pfRadioGroup);
        pfYes = findViewById(R.id.pfYes);
        pfNo = findViewById(R.id.pfNo);

        schemeRadioGroup = findViewById(R.id.schemeRadioGroup);
        schemeYes = findViewById(R.id.schemeYes);
        schemeNo = findViewById(R.id.schemeNo);
        schemeAmount = findViewById(R.id.schemeAmount);

        schemeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.schemeYes) {
                schemeAmount.setVisibility(View.VISIBLE);
            } else {
                schemeAmount.setVisibility(View.GONE);
                schemeAmount.setText("");
            }
        });

        calculate = findViewById(R.id.calculateButton);
        resultLinesLayout = findViewById(R.id.resultLinesLayout);
        netSalaryDescription = findViewById(R.id.netSalaryDescription);
        netSalaryAmount = findViewById(R.id.netSalaryAmount);

        calculate.setOnClickListener(v -> {
            try {
                double base_salary = Double.parseDouble(baseSalary.getText().toString());
                double tax_amount = Double.parseDouble(taxAmount.getText().toString());
                double medical_amount = Double.parseDouble(medicalAmount.getText().toString());
                int yr = Integer.parseInt(year.getText().toString());
                int mnth = Integer.parseInt(month.getText().toString());
                double leave_days = Double.parseDouble(leaveDays.getText().toString());
                int dabba_units = Integer.parseInt(dabbaUnits.getText().toString());
                int dabba_unit_cost = 30;

                // PF logic
                double pf_deduction = 0.0;
                if (pfYes.isChecked()) {
                    pf_deduction = base_salary * 0.10;
                }

                // Scheme logic
                double scheme_add = 0.0;
                if (schemeYes.isChecked()) {
                    String schemeVal = schemeAmount.getText().toString();
                    if (!schemeVal.isEmpty()) {
                        scheme_add = Double.parseDouble(schemeVal);
                    }
                }

                double per_day_salary = base_salary / 30.0;
                double fifth_monday_bonus = hasFifthMonday(yr, mnth) ? per_day_salary : 0;
                double salary_with_bonus = base_salary + fifth_monday_bonus;
                double salary_with_bonus_and_scheme = salary_with_bonus + scheme_add;
                double dabba_deduction = dabba_units * dabba_unit_cost;

                // ----- Leave Logic -----
                double leave_income = 0.0;
                double leave_deduction = 0.0;
                String leaveDisplayLine = "";
                if (leave_days == 4.0) {
                    leave_income = 0.0;
                    leave_deduction = 0.0;
                    leaveDisplayLine = "No Leave Adjustment";
                } else if (leave_days < 4.0) {
                    double reward_days = 4.0 - leave_days;
                    leave_income = per_day_salary * reward_days;
                    leave_deduction = 0.0;
                    leaveDisplayLine = String.format("Leave Income : ₹%.2f", leave_income);
                } else {
                    double deduction_days = leave_days - 4.0;
                    leave_income = 0.0;
                    leave_deduction = per_day_salary * deduction_days;
                    leaveDisplayLine = String.format("Leave Deduction : ₹%.2f", leave_deduction);
                }
                // ----- End Leave Logic -----

                double total_deductions = tax_amount + medical_amount + pf_deduction + dabba_deduction + leave_deduction;
                double net_monthly = salary_with_bonus_and_scheme + leave_income - total_deductions;

                String[] lines = {
                        String.format("Base Monthly Salary : ₹%.2f", base_salary),
                        String.format("Per Day Salary : ₹%.2f", per_day_salary),
                        String.format("Leave Days : %s", leave_days),
                        leaveDisplayLine,
                        String.format("5th Monday Bonus : ₹%.2f", fifth_monday_bonus),
                        String.format("Scheme Amount : ₹%.2f", scheme_add),
                        String.format("Salary with Bonus : ₹%.2f", salary_with_bonus_and_scheme),
                        String.format("Tax Deduction : ₹%.2f", tax_amount),
                        String.format("Medical Deduction : ₹%.2f", medical_amount),
                        String.format("PF Deduction (10%%) : ₹%.2f", pf_deduction),
                        String.format("Dabba Deduction : ₹%.2f", dabba_deduction),
                        String.format("Total Deductions : ₹%.2f", total_deductions)
                };

                resultLinesLayout.removeAllViews();
                for (int i = 0; i < lines.length; i++) {
                    TextView lineView = new TextView(this);
                    lineView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    lineView.setText(lines[i]);
                    lineView.setTextColor(getResources().getColor(R.color.white));
                    lineView.setTextSize(18);
                    lineView.setPadding(0, 8, 0, 8);
                    lineView.setTypeface(lineView.getTypeface(), android.graphics.Typeface.BOLD);
                    resultLinesLayout.addView(lineView);

                    if (i < lines.length - 1) {
                        View divider = new View(this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, 2);
                        params.setMargins(0, 4, 0, 4);
                        divider.setLayoutParams(params);
                        divider.setBackgroundColor(getResources().getColor(R.color.text_hint));
                        resultLinesLayout.addView(divider);
                    }
                }

                netSalaryDescription.setText("You will receive Net Monthly Salary");
                netSalaryAmount.setText("₹" + String.format("%.2f", net_monthly));

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Please fill all fields correctly!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean hasFifthMonday(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        int mondayCount = 0;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month - 1, day);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                mondayCount++;
            }
        }
        return mondayCount >= 5;
    }
}
