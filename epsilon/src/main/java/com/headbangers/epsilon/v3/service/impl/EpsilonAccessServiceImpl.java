package com.headbangers.epsilon.v3.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.headbangers.epsilon.v3.R;
import com.headbangers.epsilon.v3.model.Account;
import com.headbangers.epsilon.v3.model.Budget;
import com.headbangers.epsilon.v3.model.Category;
import com.headbangers.epsilon.v3.model.Operation;
import com.headbangers.epsilon.v3.model.SimpleResult;
import com.headbangers.epsilon.v3.model.Tiers;
import com.headbangers.epsilon.v3.preferences.EpsilonPrefs_;
import com.headbangers.epsilon.v3.service.EpsilonAccessService;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;

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

    // from SharedPrefs
    private String server;

    @Pref
    EpsilonPrefs_ epsilonPrefs;

    @AfterInject
    public void refreshServerUrl(){
        this.server = epsilonPrefs.server().get();
    }

    @Override
    public List<Account> findAccounts(String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);

        String completeUrl = this.server + this.allAccountsUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            List<Account> accounts = this.<List<Account>> parseJson(json,
                    new TypeReference<List<Account>>() {
                    });
            return accounts;
        }

        return null;
    }
    
    @Override
    public List<Budget> findBudgets(String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);

        String completeUrl = this.server + this.budgetsUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            List<Budget> budgets = this.<List<Budget>> parseJson(json,
                    new TypeReference<List<Budget>>() {
                    });
            return budgets;
        }

        return null;
    }
    
    @Override
    public Budget getBudget(String token, String budgetId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("budget", budgetId);

        String completeUrl = this.server + this.oneBudgetUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            Budget budget = this.<Budget> parseJson(json,
                    new TypeReference<Budget>() {
                    });
            return budget;
        }

        return null;
    }

    @Override
    public SimpleResult editOperation(String token, String operationId, String categoryName, String tiersName, String amount) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("oId", operationId);
        params.put("amount", amount);
        params.put("tiers", tiersName);
        params.put("category", categoryName);

        String completeUrl = this.server + this.editOperationUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult> parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public SimpleResult deleteOperation(String token, String operationId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("oId", operationId);

        String completeUrl = this.server + this.deleteOperationUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult> parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public Account getAccount(String token, String accountId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("account", accountId);

        String completeUrl = this.server + this.oneAccountUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            Account account = this.<Account> parseJson(json,
                    new TypeReference<Account>() {
                    });
            return account;
        }

        return null;
    }

    @Override
    public List<Category> findCategories(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Tiers> findTiers(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> findCategoriesName(String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);

        String completeUrl = this.server + this.categoriesNameUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            List<String> names = this.<List<String>> parseJson(json,
                    new TypeReference<List<String>>() {
                    });
            return names;
        }

        return null;
    }

    @Override
    public List<String> findTiersName(String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);

        String completeUrl = this.server + this.tiersNameUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            List<String> names = this.<List<String>> parseJson(json,
                    new TypeReference<List<String>>() {
                    });
            return names;
        }

        return null;
    }

    @Override
    public SimpleResult addDepense(String token, String accountId,
            String amount, String category, String tiers) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("account", accountId);
        params.put("amount", amount);
        params.put("tiers", tiers);
        params.put("category", category);
        
        String completeUrl = this.server + this.addDepenseUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult> parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public SimpleResult addRevenue(String token, String accountId,
                                   String amount, String category, String tiers) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("account", accountId);
        params.put("amount", amount);
        params.put("tiers", tiers);
        params.put("category", category);
        
        String completeUrl = this.server + this.addRevenueUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult> parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

    @Override
    public SimpleResult addVirement(String token, String accountTo,
            String accountFrom, String amount, String category) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put("accountFrom", accountFrom);
        params.put("accountTo", accountTo);
        params.put("amount", amount);
        params.put("category", category);
        
        String completeUrl = this.server + this.addVirementUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult> parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }
    
    @Override
    public List<Operation> findMonthOperations(String token, String account) {
        return findOperations(token, account, "account", this.opByMonthUrl);
    }

    private List<Operation> findOperations(String token, String param,
            String paramName, String url) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        params.put(paramName, param);

        String completeUrl = this.server + url;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            List<Operation> operations = this.<List<Operation>> parseJson(json,
                    new TypeReference<List<Operation>>() {
                    });
            return operations;
        }

        return null;
    }

    @Override
    public List<Operation> findCategoriesOperations(String token,
            String categoryId) {
        return findOperations(token, categoryId, "category",
                this.opByCategoryUrl);
    }

    @Override
    public List<Operation> findTiersOperations(String token, String tiersId) {
        return findOperations(token, tiersId, "tiers", this.opByTiersUrl);
    }

    @Override
    public List<Operation> findScheduleds(String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("token", token);

        String completeUrl = this.server + this.scheduledsUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            List<Operation> operations = this.<List<Operation>> parseJson(json,
                    new TypeReference<List<Operation>>() {
                    });
            return operations;
        }

        return null;
    }

    @Override
    public SimpleResult checkToken(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SimpleResult register(String server, String login, String pass) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("login", login);
        params.put("pass", pass);

        String completeUrl = server + this.registerUrl;
        String json = callHttp(completeUrl, params);

        if (json != null) {
            SimpleResult result = this.<SimpleResult> parseJson(json,
                    new TypeReference<SimpleResult>() {
                    });
            return result;
        }

        return null;
    }

}
