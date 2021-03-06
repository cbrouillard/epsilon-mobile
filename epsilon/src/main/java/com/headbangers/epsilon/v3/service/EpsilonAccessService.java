package com.headbangers.epsilon.v3.service;

import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.model.Category;
import com.headbangers.epsilon.v3.model.Scheduled;
import com.headbangers.epsilon.v3.model.Wish;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.model.Tiers;

import java.util.List;
import java.util.Map;

public interface EpsilonAccessService {

    void refreshServerUrl();

    List<Account> findAccounts();

    Account getAccount(String accountId);

    List<Category> findCategories();

    Category getCategory (String categoryId);

    List<Tiers> findTiers();

    Tiers getTiers (String tiersId);

    List<String> findCategoriesName();

    List<String> findTiersName();

    SimpleResult addDepense(String accountId, String amount,
                            String category, String tiers, String latitude, String longitude);

    SimpleResult addRevenue(String accountId, String amount,
                            String category, String tiers);

    SimpleResult addVirement(String accountTo,
                             String accountFrom, String amount, String category);

    List<Operation> findMonthOperations(String account);

    List<Operation> findCategoriesOperations(String categoryId);

    List<Operation> findTiersOperations(String tiersId);

    List<Scheduled> findScheduleds();

    SimpleResult register(String server, String login, String pass);

    List<Budget> findBudgets();
    
    Budget getBudget(String budgetId);

    SimpleResult editOperation(String operationId, String categoryName, String tiersName, String amount);

    SimpleResult deleteOperation(String operationId);

    ChartData retrieveChartByCategoryData();

    ChartData retrieveAccountFutureData (String accountId);

    Map<String, Double> retrieveSoldStats();

    SimpleResult setAccountDefault(String accountId, String isDefault);

    List<Wish> findWishes ();

    SimpleResult addWish(String accountId, String name, String price, String category, String photoPath);

    ChartData retrieveCategoriesOperationChart(String categoryId);

    ChartData retrieveTiersesOperationChart(String tiersId);

    ChartData retrieveBudgetOperationChart(String budgetId);
}
