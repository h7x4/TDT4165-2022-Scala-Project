class Bank(val allowedAttempts: Integer = 3) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()
    private var processingThreadsStarted = false;
    private val processingThreads: List[Thread] =
      (1 to 1).map(_ => new Thread {
        override def run = processTransactions
      }).toList

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
      printf("[%s]: Added transaction to queue\n", Thread.currentThread().toString())
      transactionsQueue.push(new Transaction(
        transactionsQueue,
        processedTransactions,
        from,
        to,
        amount,
        10,
      ))

      if (!processingThreadsStarted) {
        processingThreads.foreach(t => {
          t.start
          print("Starting processing thread\n")
        })
        processingThreadsStarted = true;
      }
    }
    // TODO
    // project task 2
    // create a new transaction object and put it in the queue
    // spawn a thread that calls processTransactions

    // This function is a worker that continuously
    // pops elements from the queue and processes them.
    // Multiple of these can be run on separate threads.
    private def processTransactions: Unit = {
      if (transactionsQueue.isEmpty) {
        Thread.sleep(50)
      } else {
        val trx = transactionsQueue.pop

        Main.thread(trx.run).join()

        if (trx.status == TransactionStatus.PENDING) {
          transactionsQueue.push(trx);
        } else {
          processedTransactions.push(trx);
        }
      }

      processTransactions
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
