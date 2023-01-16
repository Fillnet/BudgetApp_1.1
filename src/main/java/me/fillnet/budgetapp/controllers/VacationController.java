package me.fillnet.budgetapp.controllers;

import me.fillnet.budgetapp.services.BudgetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vacation")
public class VacationController {
    private final BudgetService budgetService;

    public VacationController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public int vacationBonus(@RequestParam int vacationDays) {
        return budgetService.getVacationBonus(vacationDays);
    }

    @GetMapping("/salary")
    public int salaryWithVacation(@RequestParam int vacationDays, @RequestParam int workingDays, @RequestParam int vacationWorkingDays) {
        return budgetService.getSalaryWithVacation(vacationDays, workingDays,vacationWorkingDays);
    }
}
