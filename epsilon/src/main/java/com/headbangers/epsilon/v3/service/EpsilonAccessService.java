package com.headbangers.epsilon.v3.service;

import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.model.Category;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.model.Tiers;

import java.util.List;

public interface EpsilonAccessService {

    void refreshServerUrl();

    List<Account> findAccounts(String token);

    Account getAccount(String token, String accountId);

    List<Category> findCategories(String token);

    List<Tiers> findTiers(String token);

    List<String> findCategoriesName(String token);

    List<String> findTiersName(String token);

    SimpleResult addDepense(String token, String accountId, String amount,
                            String category, String tiers);

    SimpleResult addRevenue(String token, String accountId, String amount,
            String category, String tiers);

    SimpleResult addVirement(String token, String accountTo,
            String accountFrom, String amount, String category);

    List<Operation> findMonthOperations(String token, String account);

    List<Operation> findCategoriesOperations(String token, String categoryId);

    List<Operation> findTiersOperations(String token, String tiersId);

    List<Operation> findScheduleds(String token);

    SimpleResult checkToken(String token);

    SimpleResult register(String server, String login, String pass);

    List<Budget> findBudgets(String token);
    
    Budget getBudget (String token, String budgetId);

    SimpleResult editOperation(String token, String operationId, String categoryName, String tiersName, String amount);

    SimpleResult deleteOperation (String token, String operationId);

    ChartData retrieveChartByCategoryData (String token);
}
