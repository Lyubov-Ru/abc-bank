Customer Summary
<#list customers as customer>
${customer.name} (${customer.numberOfAccounts} <#if customer.numberOfAccounts == 1>account<#else>accounts</#if>)
</#list>