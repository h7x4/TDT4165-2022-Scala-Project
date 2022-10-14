
object Main extends App {
    //task a
    val x = (1 to 50).toList
    println(x)

    //task b
    def find_sum_of_list(list: List[Int]): Int = {
        var sum: Int = 0
        list.foreach (sum += _)
        sum
    }
    println(find_sum_of_list(List(1, 2, 3, 4)))

    //task c
    def find_sum_of_list_rec(list: List[Int]): Int = list match{
        case List() => 0
        case _ => list.head + find_sum_of_list_rec(list.tail)
    }
    println(find_sum_of_list_rec(List(1, 2, 3, 4)))

    //task d
    def nth_fibonacci(n: BigInt): BigInt = n match 
        case 0 => 0
        case 1 => 1
        case _ => nth_fibonacci(n-1) + nth_fibonacci(n-2)
    
    println(nth_fibonacci(10))   
}