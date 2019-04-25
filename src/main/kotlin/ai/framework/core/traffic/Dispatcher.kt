package ai.framework.core.traffic

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class Dispatcher {
    companion object {
        private val jobs = LinkedList<Job>()

        fun add(f: () -> Unit) {
            jobs.add(GlobalScope.launch { f() })
        }

        fun join() {
            runBlocking {
                for (job in jobs) {
                    job.join()
                }
            }
        }
    }
}