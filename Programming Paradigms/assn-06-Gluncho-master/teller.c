#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <assert.h>
#include <inttypes.h>

#include "teller.h"
#include "account.h"
#include "error.h"
#include "debug.h"
#include "branch.h"

BranchID
Account_GetBranchID(AccountNumber accountNum)
{
  Y;
  return (BranchID)(accountNum >> 32);
}
/*
 * deposit money into an account
 */
int Teller_DoDeposit(Bank *bank, AccountNumber accountNum, AccountAmount amount)
{
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoDeposit(account 0x%" PRIx64 " amount %" PRId64 ")\n",
                accountNum, amount));

  Account *account = Account_LookupByNumber(bank, accountNum);

  if (account == NULL)
  {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  sem_wait(&account->l);
  BranchID id = Account_GetBranchID(accountNum);
  sem_wait(&bank->branches[id].lock);

  Account_Adjust(bank, account, amount, 1);
  sem_post(&account->l);
  sem_post(&bank->branches[id].lock);
  return ERROR_SUCCESS;
}

/*
 * withdraw money from an account
 */
int Teller_DoWithdraw(Bank *bank, AccountNumber accountNum, AccountAmount amount)
{
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoWithdraw(account 0x%" PRIx64 " amount %" PRId64 ")\n",
                accountNum, amount));

  Account *account = Account_LookupByNumber(bank, accountNum);
  if (account == NULL)
  {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  sem_wait(&account->l);
  BranchID id = Account_GetBranchID(accountNum);
  sem_wait(&bank->branches[id].lock);

  // if (amount > Account_Balance(account))
  // {
  //   sem_post(&account->l);
  //   sem_post(&bank->branches[id].lock);
  //   return ERROR_INSUFFICIENT_FUNDS;
  // }

  Account_Adjust(bank, account, -amount, 1);

  sem_post(&account->l);
  sem_post(&bank->branches[id].lock);
  return ERROR_SUCCESS;
}

/*
 * do a tranfer from one account to another account
 */
int Teller_DoTransfer(Bank *bank, AccountNumber srcAccountNum,
                      AccountNumber dstAccountNum,
                      AccountAmount amount)
{
  assert(amount >= 0);
  DPRINTF('t', ("Teller_DoTransfer(src 0x%" PRIx64 ", dst 0x%" PRIx64
                ", amount %" PRId64 ")\n",
                srcAccountNum, dstAccountNum, amount));

  Account *srcAccount = Account_LookupByNumber(bank, srcAccountNum);
  if (srcAccount == NULL)
  {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  Account *dstAccount = Account_LookupByNumber(bank, dstAccountNum);
  if (dstAccount == NULL)
  {
    return ERROR_ACCOUNT_NOT_FOUND;
  }
  else if (dstAccount == srcAccount)
    return ERROR_SUCCESS;
  if(srcAccountNum < dstAccountNum){
    sem_wait(&srcAccount->l);
    sem_wait(&dstAccount->l);
  }else{
    sem_wait(&dstAccount->l);
    sem_wait(&srcAccount->l);
  }
  int srcID = Account_GetBranchID(srcAccountNum);
  int dstID = Account_GetBranchID(dstAccountNum);
  /*
   * If we are doing a transfer within the branch, we tell the Account module to
   * not bother updating the branch balance since the net change for the
   * branch is 0.
   */
  int updateBranch = !Account_IsSameBranch(srcAccountNum, dstAccountNum);
  if (updateBranch)
  {
    if (srcID < dstID)
    {
      sem_wait(&bank->branches[srcID].lock);
      sem_wait(&bank->branches[dstID].lock);
    }else{
      sem_wait(&bank->branches[dstID].lock);
      sem_wait(&bank->branches[srcID].lock);
    }
    // if (amount > Account_Balance(srcAccount))
    // {
    //   sem_post(&dstAccount->l);
    //   sem_post(&srcAccount->l);
    //   sem_post(&bank->branches[dstID].lock);
    //   sem_post(&bank->branches[srcID].lock);
    //   return ERROR_INSUFFICIENT_FUNDS;
    // }
    Account_Adjust(bank, srcAccount, -amount, updateBranch);
    Account_Adjust(bank, dstAccount, amount, updateBranch);
    sem_post(&dstAccount->l);
    sem_post(&srcAccount->l);
    sem_post(&bank->branches[dstID].lock);
    sem_post(&bank->branches[srcID].lock);
    return ERROR_SUCCESS;
  }
  // if (amount > Account_Balance(srcAccount)){
  //   sem_post(&dstAccount->l);
  //   sem_post(&srcAccount->l);
  //   return ERROR_INSUFFICIENT_FUNDS;
  // }
  Account_Adjust(bank, srcAccount, -amount, updateBranch);
  Account_Adjust(bank, dstAccount, amount, updateBranch);
  sem_post(&dstAccount->l);
  sem_post(&srcAccount->l);
  return ERROR_SUCCESS;
}
