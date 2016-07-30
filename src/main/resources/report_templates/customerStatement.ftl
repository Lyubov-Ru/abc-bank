Statement for ${customer.name}

<#list customer.accounts as account>
${account.name}
    <#list account.transactions as transaction>
        <#if transaction.isWithdrawalFromAccount(account)>withdrawal<#else>deposit</#if> $${transaction.amount?string["0.00"]}
    </#list>
Total $${account.currentBalance.totalBalance?string["0.00"]}

</#list>
Total In All Accounts ${customer.totalBalance?string["0.00"]}