class Bank(val allowedAttempts: Integer = 3, val workers: Integer = 5) {

    private val transactionsQueue: TransactionQueue = new TransactionQueue()
    private val processedTransactions: TransactionQueue = new TransactionQueue()
    private var processingThreadsStarted = false;
    private val processingThreads: List[Thread] =
      (1 to workers).map(_ => new Thread {
        override def run = processTransactions
      }).toList
    
    private var addedTransactions = 0

    def addTransactionToQueue(from: Account, to: Account, amount: Double): Unit = {
      transactionsQueue.push(new Transaction(
        transactionsQueue,
        processedTransactions,
        from,
        to,
        amount,
        allowedAttempts,
      ))

      addedTransactions += 1

      processingThreadsStarted.synchronized({
        if (!processingThreadsStarted) {
          processingThreads.foreach(t => {
            t.start
          })
          processingThreadsStarted = true;
        }
      })

      
    }
    // TODO
    // project task 2
    // create a new transaction object and put it in the queue
    // spawn a thread that calls processTransactions

    // This function is a worker that continuously
    // pops elements from the queue and processes them.
    // Multiple of these can be run on separate threads.
    private def processTransactions: Unit = {
      val maybeTrx = transactionsQueue.synchronized(
        if (transactionsQueue.isEmpty) None else Some(transactionsQueue.pop)
      )

      maybeTrx match {
        case Some(trx) => {
          Main.thread(trx.run).join()

          if (trx.status == TransactionStatus.PENDING) {
            transactionsQueue.push(trx)
          } else {
            processedTransactions.push(trx)
          }
        }
        case None => Thread.sleep(50)
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
