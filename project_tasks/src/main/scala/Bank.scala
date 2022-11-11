class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
      transactionsQueue.push(new Transaction(
        transactionsQueue,
        processedTransactions,
        from,
        to,
        amount,
        10,
      ))

      Main.thread(processTransaction(transactionsQueue.pop()))
    }
                                                // TODO
                                                // project task 2
                                                // create a new transaction object and put it in the queue
                                                // spawn a thread that calls processTransactions

    // There are mixed instructions for this method.
    // It's called `processTransactions`, indicating that it should
    // process all lists, the part in the assigment pdf indicates this as well.
    // However the comment below is written as if there is only one transaction to
    // be processed, and the fact that `addTransactionToQueue` calls this method every
    // time something is added, supports that theory as well.
    // We just went with the most logical option...
    private def processTransactions(trx: Transaction): Unit = {
      // thread = Main.thread(trx)
      // thread.join()
      trx()
      if (trx.status == TransactionStatus.PENDING && trx.attempt < trx.allowedAttemps) {
        processTransactions(trx)
      } else {
        processedTransactions.push(trx);
      }
    }
                                                // TODO
                                                // project task 2
                                                // Function that pops a transaction from the queue
                                                // and spawns a thread to execute the transaction.
                                                // Finally do the appropriate thing, depending on whether
                                                // the transaction succeeded or not

    def addAccount(initialBalance: Double): Account = {
        new Account(this, initialBalance)
    }

    def getProcessedTransactionsAsList: List[Transaction] = {
        processedTransactions.iterator.toList
    }

}
