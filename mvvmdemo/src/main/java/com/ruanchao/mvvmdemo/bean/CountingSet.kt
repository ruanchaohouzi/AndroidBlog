package com.ruanchao.mvvmdemo.bean

//委托，将MutableCollection委托给innerSet，这样就具有了MutableCollection中的方法
class CountingSet<T>(private val innerSet:MutableCollection<T> = HashSet())
    : MutableCollection<T> by innerSet{

    private var count: Int = 0
    override fun add(element: T): Boolean {
        count++
        return innerSet.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        count+=elements.size
        return innerSet.addAll(elements)
    }
}