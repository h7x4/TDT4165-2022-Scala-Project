import exceptions._
import scala.collection.mutable
import scala.collection.mutable._

object TransactionStatus extends Enumeration {
  val SUCCESS, PENDING, FAILED = Value
}

class TransactionQueue {

    // TODO
    // project task 1.1
    // Add datastructure to contain the transactions
    var queue: Queue[Transaction] = Queue()
    // Remove and return the first element from the queue
    def pop: Transaction = {queue.synchronized(queue.dequeue())}

    // Return whether the queue is empty
    def isEmpty: Boolean = {queue.synchronized(queue.isEmpty)}

    // Add new element to the back of the queue
    def push(t: Transaction): Unit = {queue.synchronized(queue.enqueue(t))}

    // Return the first element from the queue without removing it
    def peek: Transaction = {queue.synchronized(queue.front)}

    // Return an iterator to allow you to iterate over the queue
    def iterator: Iterator[Transaction] = {queue.synchronized(queue.iterator)}
}

class Transaction(val transactionsQueue: TransactionQueue,
                  val processedTransactions: TransactionQueue,
                  val from: Account,
                  val to: Account,
                  val amount: Double,
                  val allowedAttemps: Int) extends Runnable {

  var status: TransactionStatus.Value = TransactionStatus.PENDING
  var attempt = 0

  override def run: Unit = {
    def doTransaction(): Unit = {
      // TODO - project task 3
      // Extend this method to satisfy requirements.
      if (from.withdraw(amount).isRight) {
        return
      }

      if (to.deposit(amount).isRight) {
        if (from.deposit(amount).isRight) print("oof")
        return
      }

      status = TransactionStatus.SUCCESS
    }

    attempt += 1
    if (status != TransactionStatus.PENDING) {
      return
    }
    from.synchronized(to.synchronized(doTransaction))

    Thread.sleep(50)

    if (attempt >= allowedAttemps) {
      status = TransactionStatus.FAILED
    }
  }
}
