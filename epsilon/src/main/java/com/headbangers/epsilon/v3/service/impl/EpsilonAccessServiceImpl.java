package com.headbangers.epsilon.v3.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.model.Category;
import com.headbangers.epsilon.v3.model.Scheduled;
import com.headbangers.epsilon.v3.model.Wish;
import com.headbangers.epsilon.v3.model.chart.ChartData;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.model.Tiers;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EBean
public class EpsilonAccessServiceImpl extends WebService implements
        EpsilonAccessService {

    @StringRes(R.string.ws_accounts)
    String allAccountsUrl;
    @StringRes(R.string.ws_register)
    String registerUrl;
    @StringRes(R.string.ws_bymonth)
    String opByMonthUrl;
    @StringRes(R.string.ws_bycategory)
    String opByCategoryUrl;
    @StringRes(R.string.ws_bytiers)
    String opByTiersUrl;
    @StringRes(R.string.ws_categories_name)
    String categoriesNameUrl;
    @StringRes(R.string.ws_tiers_name)
    String tiersNameUrl;
    @StringRes(R.string.ws_add_revenue)
    String addRevenueUrl;
    @StringRes(R.string.ws_add_depense)
    String addDepenseUrl;
    @StringRes(R.string.ws_add_virement)
    String addVirementUrl;
    @StringRes(R.string.ws_one_account)
    String oneAccountUrl;
    @StringRes(R.string.ws_scheduleds)
    String scheduledsUrl;
    @StringRes(R.string.ws_budgets)
    String budgetsUrl;
    @StringRes(R.string.ws_one_budget)
    String oneBudgetUrl;
    @StringRes(R.string.ws_edit_operation)
    String editOperationUrl;
    @StringRes(R.string.ws_delete_operation)
    String deleteOperationUrl;
    @StringRes(R.string.ws_chart_category)
    String retrieveChartByCategoryUrl;
    @StringRes(R.string.ws_categories)
    String allCategoriesUrl;
    @StringRes(R.string.ws_tierses)
    String allTiersUrl;
    @StringRes(R.string.ws_solds_stats)
    String retrieveSoldStats;
    @StringRes(R.string.ws_set_account_default)
    String setAccountDefaultUrl;
    @StringRes(R.string.ws_chart_account_future)
    String retrieveAccountFutureDataUrl;
    @StringRes(R.string.ws_wishes)
    String allWishesUrl;
    @StringRes(R.string.ws_add_wish)
    String addWishUrl;

    // from SharedPrefs
    private String server;

    @AfterInject
    public void refreshServerUrl() {
        this.server = epsilonPrefs.server().get();
    }

    @Override
    public List<Account> findAccounts() {

        String completeUrl = this.server + this.allAccountsUrl;
        String json = get(completeUrl);

        if (json != null) {
            List<Account> accounts = this.<List<Account>>parseJson(json,
                    new TypeReference<List<Account>>() {
                    });


            return accounts;
        }

        return null;
    }

    @Override
    public List<Budget> findBudgets() {

        String completeUrl = this.server + this.budgetsUrl;
        String json = get(completeUrl);

        if (json != null) {
            List<Budget> budgets = this.<List<Budget>>parseJson(json,
                    new TypeReference<List<Budget>>() {
                    });
            return budgets;
        }

        return null;
    }

    @Override
    public Budget getBudget(String budgetId) {
        String completeUrl = this.server + this.oneBudgetUrl.replace("{id}", budgetId);
        String json = get(completeUrl);

        if (json != null) {
            Budget budget = this.<Budget>parseJson(json,
                    new TypeReference<Budget>() {
                    });
            return budget;
        }

        return null;
    }

    @Override
    public ChartData retrieveChartByCategoryData() {

        String completeUrl = this.server + this.retrieveChartByCategoryUrl;
        String json = get(completeUrl);

        if (json != null) {
            ChartData data = this.<ChartData>parseJson(json,
                    new TypeReference<ChartData>() {
                    });
            return data;
        }

        return null;
    }

    @Override
    public ChartData retrieveAccountFutureData(String accountId) {
        String completeUrl = this.server + this.retrieveAccountFutureDataUrl.replace("{id}", accountId);
        String json = get(completeUrl);

        if (json != null) {
            ChartData data = this.<ChartData>parseJson(json,
                    new TypeReference<ChartData>() {
                    });
            return data;
        }

        return null;
    }

    @Override
    public Map<String, Double> retrieveSoldStats() {
        String completeUrl = this.server + this.retrieveSoldStats;
        String json = get(completeUrl);

        if (json != null) {
            Map<String, Double> data = this.<Map<String, Double>>parseJson(json,
                    new TypeReference<Map<String, Double>>() {
                    });
            return data;
        }
        return null;
    }

    @Override
    public SimpleResult setAccountDefault(String accountId, String isDefault) {
        String completeUrl = this.server + this.setAccountDefaultUrl
                .replace("{id}", accountId).replace("{value}", isDefault);
        String json = post(completeUrl, new HashMap<String, String>());

        if (json != null) {
            SimpleResult result = this.<SimpleResult>parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public List<Wish> findWishes() {
        String completeUrl = this.server + this.allWishesUrl;
        String json = get(completeUrl);

        if (json != null) {
            List<Wish> wishes = this.<List<Wish>>parseJson(json,
                    new TypeReference<List<Wish>>() {
                    });
            return wishes;
        }

        return null;
    }

    @Override
    public SimpleResult addWish(String accountId, String name, String price, String category, String photoPath) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("account", accountId);
        params.put("category", category);
        params.put("price", price);

        String completeUrl = this.server + this.addWishUrl;

        String json;
        if (photoPath != null) {
            File file = new File(URI.create(photoPath));
            json = post(completeUrl, params);//, "photo", file);
        } else {
            json = post(completeUrl, params);
        }

        if (json != null) {
            SimpleResult result = this.<SimpleResult>parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public Account getAccount(String accountId) {
        String completeUrl = this.server + this.oneAccountUrl.replace("{id}", accountId);
        String json = get(completeUrl);

        if (json != null) {
            Account account = this.<Account>parseJson(json,
                    new TypeReference<Account>() {
                    });
            return account;
        }

        return null;
    }

    @Override
    public List<Category> findCategories() {
        String completeUrl = this.server + this.allCategoriesUrl;
        String json = get(completeUrl);

        if (json != null) {
            List<Category> categories = this.<List<Category>>parseJson(json,
                    new TypeReference<List<Category>>() {
                    });
            return categories;
        }

        return null;
    }

    @Override
    public List<Tiers> findTiers() {
        String completeUrl = this.server + this.allTiersUrl;
        String json = get(completeUrl);

        if (json != null) {
            List<Tiers> tierses = this.<List<Tiers>>parseJson(json,
                    new TypeReference<List<Tiers>>() {
                    });
            return tierses;
        }

        return null;
    }

    @Override
    public List<String> findCategoriesName() {

        String completeUrl = this.server + this.categoriesNameUrl;
        String json = get(completeUrl);

        if (json != null) {
            List<String> names = this.<List<String>>parseJson(json,
                    new TypeReference<List<String>>() {
                    });
            return names;
        }

        return null;
    }

    @Override
    public List<String> findTiersName() {

        String completeUrl = this.server + this.tiersNameUrl;
        String json = get(completeUrl);

        if (json != null) {
            List<String> names = this.<List<String>>parseJson(json,
                    new TypeReference<List<String>>() {
                    });
            return names;
        }

        return null;
    }

    @Override
    public SimpleResult addDepense(String accountId,
                                   String amount, String category, String tiers, String latitude, String longitude) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("amount", amount);
        params.put("tiers", tiers);
        params.put("category", category);
        params.put("latitude", latitude);
        params.put("longitude", longitude);

        String completeUrl = this.server + this.addDepenseUrl.replace("{id}", accountId);
        String json = post(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult>parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public SimpleResult addRevenue(String accountId,
                                   String amount, String category, String tiers) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("account", accountId);
        params.put("amount", amount);
        params.put("tiers", tiers);
        params.put("category", category);

        String completeUrl = this.server + this.addRevenueUrl.replace("{id}", accountId);
        String json = post(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult>parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public SimpleResult addVirement(String accountTo,
                                    String accountFrom, String amount, String category) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("accountFrom", accountFrom);
        params.put("accountTo", accountTo);
        params.put("amount", amount);
        params.put("category", category);

        String completeUrl = this.server + this.addVirementUrl.replace("{idFrom}", accountFrom).replace("{idTo}", accountTo);
        String json = post(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult>parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public List<Operation> findMonthOperations(String account) {
        return findOperations(this.opByMonthUrl.replace("{id}", account));
    }

    private List<Operation> findOperations(String url) {
        String completeUrl = this.server + url;
        String json = get(completeUrl);

        if (json != null) {
            List<Operation> operations = this.<List<Operation>>parseJson(json,
                    new TypeReference<List<Operation>>() {
                    });
            return operations;
        }

        return null;
    }

    @Override
    public List<Operation> findCategoriesOperations(String categoryId) {
        return findOperations(
                this.opByCategoryUrl.replace("{id}", categoryId));
    }

    @Override
    public List<Operation> findTiersOperations(String tiersId) {
        return findOperations(this.opByTiersUrl.replace("{id}", tiersId));
    }

    @Override
    public List<Scheduled> findScheduleds() {

        String completeUrl = this.server + this.scheduledsUrl;
        String json = get(completeUrl);

        if (json != null) {
            List<Scheduled> operations = this.<List<Scheduled>>parseJson(json,
                    new TypeReference<List<Scheduled>>() {
                    });
            return operations;
        }

        return null;
    }

    @Override
    public SimpleResult register(String server, String login, String pass) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("login", login);
        params.put("pass", pass);

        String completeUrl = server + this.registerUrl;
        String json = post(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult>parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public SimpleResult editOperation(String operationId, String categoryName, String tiersName, String amount) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("amount", amount);
        params.put("tiers", tiersName);
        params.put("category", categoryName);

        String completeUrl = this.server + this.editOperationUrl.replace("{id}", operationId);
        String json = post(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult>parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public SimpleResult deleteOperation(String operationId) {

        String completeUrl = this.server + this.deleteOperationUrl.replace("{id}", operationId);
        String json = delete(completeUrl);

        if (json != null) {
            SimpleResult result = this.<SimpleResult>parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }
}
