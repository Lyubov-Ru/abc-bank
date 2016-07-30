Total interest paid on all Accounts:

<#list customers as customer>
${customer.name}
    <#list customer.accounts as account>
    ${account.name}: $${account.currentBalance.totalInterestPaid?string["0.00"]}
    </#list>
    Total $${customer.totalInterestPaid?string["0.00"]}

</#list>
Total $${total?string["0.00"]}