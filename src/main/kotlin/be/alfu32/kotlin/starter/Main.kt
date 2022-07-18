package github.alfu32.klog

fun main(args: Array<String>){
    println("it works with args ${args.toList()}")
    args.forEachIndexed {
        i,arg -> println("""arg[$i]='$arg'""")
    }
    val c = MyCalculator()
    val list = listOf(1,2,3,4,5)
    val group = list.groupBy{
        v -> if(v%2 == 0){"even"}else{"odd"}
    }
    val m = list.mapIndexed{ i,v -> "a=$v" }
    println("""m=$m""")
    println("grouped list ${group}")
    println("""MyCalculator.sum($list) = ${c.sum(list)}""")
}